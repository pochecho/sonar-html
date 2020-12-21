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
package org.sonar.plugins.html.models;

import org.sonar.api.batch.fs.InputFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Source {
    private InputFile inputFile;
    private String contents;
    private List<Issue> issues = new ArrayList<>();

    public Source(InputFile inputFile) throws IOException {
        this.inputFile = inputFile;
        processContent();
    }

    private void processContent() throws IOException {
        this.contents = inputFile.contents();
    }

    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    public InputFile getInputFile() {
        return inputFile;
    }

    public String getContents() {
        return contents;
    }


    public List<Issue> getIssues() {
        return issues;
    }

}
