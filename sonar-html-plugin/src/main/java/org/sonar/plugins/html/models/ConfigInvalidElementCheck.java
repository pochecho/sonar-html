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
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;

public class ConfigInvalidElementCheck {
    public final String pattern;
    public final String issueFoundedMessage;
    public final InputFile htmlSource;
    public final RuleKey ruleKey;
    public final boolean isMultiline;

    public final String classStyleReg;
    public final Logger LOGGER;

    public ConfigInvalidElementCheck(String pattern, String issueFoundedMessage, InputFile htmlSource, RuleKey ruleKey,
                                     boolean isMultiline, String classStyleReg, Logger LOGGER) {
        this.pattern = pattern;
        this.issueFoundedMessage = issueFoundedMessage;
        this.htmlSource = htmlSource;
        this.ruleKey = ruleKey;
        this.isMultiline = isMultiline;
        this.classStyleReg = classStyleReg;
        this.LOGGER = LOGGER;
    }

}
