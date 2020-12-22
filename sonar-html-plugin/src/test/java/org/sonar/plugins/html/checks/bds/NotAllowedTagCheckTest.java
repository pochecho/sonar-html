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
package org.sonar.plugins.html.checks.bds;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.api.utils.log.LogTester;
import org.sonar.plugins.html.Utils;
import org.sonar.plugins.html.checks.HtmlIssue;
import org.sonar.plugins.html.checks.bds.NotAllowedTagCheck;
import org.sonar.plugins.html.visitor.HtmlSourceCode;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class NotAllowedTagCheckTest {
    @Rule
    public LogTester logTester = new LogTester();


    
    @Test
    public void shouldHandleInvalidTagIssue() throws IOException {
        // Arrange
        HtmlSourceCode source = getSource("invalid-tag.html");
        NotAllowedTagCheck check = new NotAllowedTagCheck();
        check.setSourceCode(source);
        check.classStyleReg = "bc-card=card;app-album-card";
        // Act
        check.startDocument(new ArrayList<>());
        for (HtmlIssue issue : source.getIssues()) {
            System.out.println(issue);
        }
        // Assert
        assertEquals(1, source.getIssues().size());
    }

    

    private HtmlSourceCode getSource(String filename) throws IOException {
        return new HtmlSourceCode(Utils.getInputFile("invalid-tag/" + filename));
    }
}
