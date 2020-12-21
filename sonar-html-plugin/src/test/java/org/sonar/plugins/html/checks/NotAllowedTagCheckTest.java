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

import org.sonar.plugins.html.Utils;
import org.sonar.plugins.html.models.Issue;
import org.sonar.plugins.html.models.Source;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class NotAllowedTagCheckTest {
    @Rule
    public LogTester logTester = new LogTester();

    @Test
    public void testFailedValidateNoSource() {
        NotAllowedTagCheck check = new NotAllowedTagCheck();
        try {
            check.validate();
            fail("No source code should raise an exception");
        } catch (IllegalStateException e) {
            assertEquals("Source code not set, cannot validate anything", e.getMessage());
        }
    }

    // @Test
    // public void shouldHandleIssue() throws IOException {
    // // Arrange
    // Source source = getSource("app-component.html");
    // NotAllowedTagCheck check = new NotAllowedTagCheck();
    // check.setHtmlSource(source);
    // check.classStyleReg = "bc-accordions-group=accordion;mat-accordion";
    // // Act
    // check.validate();
    // // Assert
    // assertEquals(2, source.getIssues().size());
    // }

    @Test
    public void shouldHandleInvalidTagIssue() throws IOException {
        // Arrange
        Source source = getSource("invalid-tag.html");
        NotAllowedTagCheck check = new NotAllowedTagCheck();
        check.setHtmlSource(source);
        check.classStyleReg = "bc-accordions-group=mat-accordion";
        // Act
        check.validate();
        for (Issue issue : source.getIssues()) {
            System.out.println(issue);
        }
        // Assert
        assertEquals(1, source.getIssues().size());
    }

    // @Test
    // public void shouldHandleIssueWithLineBreaker() throws IOException {
    // // Arrange
    // Source source = getSource("app-component.html");
    // NotAllowedTagCheck check = new NotAllowedTagCheck();
    // check.setHtmlSource(source);
    // check.classStyleReg = "bc-accordions-group=mat-accordion";
    // String message =
    // String.format(NotAllowedTagCheck.ISSUE_FOUNDED_MESSAGE,"mat-accordion","bc-accordions-group");
    // // Act
    // check.validate();
    // // Assert
    // assertEquals(1, logTester.logs(LoggerLevel.INFO).size());
    // assertThat(logTester.logs(LoggerLevel.INFO).get(0), containsString(message));
    // assertEquals(1, source.getIssues().size());
    // }

    // @Test
    // public void shouldHandleIssue() throws IOException {
    // // Arrange
    // Source source = getSource("app-component.html");
    // NotAllowedTagCheck check = new NotAllowedTagCheck();
    // check.setHtmlSource(source);
    // check.classStyleReg = "bc-accordions-group=accordion,mat-accordion";
    // String message =
    // String.format(NotAllowedTagCheck.ISSUE_FOUNDED_MESSAGE,"mat-accordion","bc-accordions-group");
    // // Act
    // check.validate();
    // // Assert
    // assertEquals(1, logTester.logs(LoggerLevel.INFO).size());
    // assertThat(logTester.logs(LoggerLevel.INFO).get(0), containsString(message));
    // assertEquals(1, source.getIssues().size());
    // }

    private Source getSource(String filename) throws IOException {
        return new Source(Utils.getInputFile("invalid-tag/" + filename));
    }
}
