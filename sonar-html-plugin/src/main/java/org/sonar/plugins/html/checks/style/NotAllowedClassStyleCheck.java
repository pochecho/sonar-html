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

package org.sonar.plugins.html.checks.style;

import java.util.List;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.html.checks.AbstractPageCheck;
import org.sonar.plugins.html.checks.HtmlIssue;
import org.sonar.plugins.html.models.ConfigInvalidElementCheck;
import org.sonar.plugins.html.node.Node;

@Rule(key = "NotAllowedClassStyleCheck")
public class NotAllowedClassStyleCheck extends AbstractPageCheck {

    private static final Logger LOGGER = Loggers.get(NotAllowedClassStyleCheck.class);
    public static final String ISSUE_FOUNDED_MESSAGE = "The usage of %s class is not allowed, you have to use %s instead.";

    @RuleProperty(key = "not-allowed-class", description = "Sentence like the following structure: valid-tag=invalid-tag1;invalid-tag2;...")
    String classStyleReg;

    NotAllowedGenericElementCheck notAllowedElementCheck;


    @Override
    public void startDocument(List<Node> nodes) {
        ConfigInvalidElementCheck config = new ConfigInvalidElementCheck(
                "class[ ]{0,1}=[ ]{0,1}[\"|']{1}([\r\n \ta-zA-Z0-9-_]+)[\"|']{1}", ISSUE_FOUNDED_MESSAGE, getHtmlSourceCode().inputFile(),
                getRuleKey(), true, this.classStyleReg, LOGGER);
        this.notAllowedElementCheck = new NotAllowedGenericElementCheck(config);
        List<HtmlIssue> issues = this.notAllowedElementCheck.validate();
        for (HtmlIssue issue : issues) {
            getHtmlSourceCode().addIssue(issue);
        }
    }

}
