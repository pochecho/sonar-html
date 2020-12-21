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
package org.sonar.plugins.html.checks;

import org.sonar.plugins.html.models.ConfigInvalidElementCheck;
import org.sonar.plugins.html.models.Issue;

import java.util.List;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = "NotAllowedTagCheck")
public class NotAllowedTagCheck extends AbstractHtmlCheck {

    private static final Logger LOGGER = Loggers.get(NotAllowedTagCheck.class);
    public static final String ISSUE_FOUNDED_MESSAGE = "The usage of %s tag/element is not allowed, you have to use %s instead.";

    @RuleProperty(key = "not-allowed-tag ", description = "Sentence like the following structure: bc-accordions-group=accordion,mat-accordion")
    String classStyleReg;

    NotAllowedGenericElementCheck notAllowedElementCheck;

    @Override
    public void validate() {
        ConfigInvalidElementCheck config = new ConfigInvalidElementCheck("<[ ]*([a-zA-Z0-9-_]+)", ISSUE_FOUNDED_MESSAGE,
                getHtmlSource(), getRuleKey(), false,this.classStyleReg, LOGGER);

        this.notAllowedElementCheck = new NotAllowedGenericElementCheck(config);
        List<Issue> issues = this.notAllowedElementCheck.validate();
        for (Issue issue : issues) {
            getHtmlSource().addIssue(issue);
        }
    }

}
