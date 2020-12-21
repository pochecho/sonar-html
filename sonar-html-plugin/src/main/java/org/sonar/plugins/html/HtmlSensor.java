
/*
 * SonarSource HTML analyzer :: Sonar Plugin
 * Copyright (c) 2010-2020 SonarSource SA and Matthijs Galesloot
 * sonarqube@googlegroups.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sonar.plugins.html;


import org.sonar.plugins.html.checks.AbstractHtmlCheck;
import org.sonar.plugins.html.checks.CheckRepository;
import org.sonar.plugins.html.languages.HtmlFileSystem;
import org.sonar.plugins.html.languages.HtmlLanguage;
import org.sonar.plugins.html.models.Issue;
import org.sonar.plugins.html.models.Source;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.util.List;

public class HtmlSensor implements Sensor {
    private static final Logger LOGGER = Loggers.get(HtmlSensor.class);

    private final HtmlFileSystem htmlFileSystem;
    private final Checks<Object> checks;

    public HtmlSensor(CheckFactory checkFactory, FileSystem fileSystem) {
        this.htmlFileSystem = new HtmlFileSystem(fileSystem);
        this.checks = checkFactory
                .create(CheckRepository.REPOSITORY_KEY)
                .addAnnotatedChecks((Iterable<?>) CheckRepository.getCheckClasses());
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.onlyOnLanguage(HtmlLanguage.KEY).name(this.toString());
    }

    @Override
    public void execute(SensorContext context) {
        try {
            if (htmlFileSystem.hasHtmlFiles()) {
                scanFiles(context);
            }
        } catch (Exception e) {
            LOGGER.warn("Error executing sensor ", e);
        }
    }

    private void scanFiles(SensorContext context) {

        List<InputFile> inputFiles = htmlFileSystem.sourceInputFiles();
        for (InputFile file : inputFiles) {
            LOGGER.debug("file language: " + file.language() + " file name: " + file.filename());
                        checkInputFile(context, file);
            }
        }

    private void checkInputFile(SensorContext context, InputFile file) {
        try {
            Source source = new Source(file);
            runChecks(context, source);
        } catch (IOException e) {
            LOGGER.warn("Error reading source file " + file.filename(), e);
        }
    }

    private void runChecks(SensorContext context, Source sourceCode) {
        for (Object check : checks.all()) {
            try {
                ((AbstractHtmlCheck) check).setRuleKey(checks.ruleKey(check));
                ((AbstractHtmlCheck) check).setHtmlSource(sourceCode);
                LOGGER.debug("Checking rule: " + ((AbstractHtmlCheck) check).getRuleKey());
                ((AbstractHtmlCheck) check).validate();
            } catch (Exception e) {
                LOGGER.warn("Error checking html rule", e);
            }
        }
        saveIssues(context, sourceCode);
    }

    private void saveIssues(SensorContext context, Source sourceCode) {
        for (Issue issue : sourceCode.getIssues()) {
            try {
                LOGGER.debug("Saving issue: " + issue.getMessage());
                NewIssue newIssue = context.newIssue().forRule(issue.getRuleKey());
                NewIssueLocation location = newIssue.newLocation()
                        .on(sourceCode.getInputFile())
                        .message(issue.getMessage())
                        .at(sourceCode.getInputFile().selectLine(issue.getLine()));
                newIssue.at(location).save();
            } catch (Exception e) {
                LOGGER.warn("Error saving issue", e);
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
