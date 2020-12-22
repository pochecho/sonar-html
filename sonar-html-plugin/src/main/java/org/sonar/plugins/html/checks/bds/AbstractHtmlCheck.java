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

import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.html.models.Source;

public abstract class AbstractHtmlCheck {
    protected RuleKey ruleKey = null;

    protected Source htmlSource = null;

    public RuleKey getRuleKey() {
        return ruleKey;
    }

    public void setRuleKey(RuleKey ruleKey) {
        this.ruleKey = ruleKey;
    }

    public Source getHtmlSource() {
        return htmlSource;
    }

    public void setHtmlSource(Source source) {
        this.htmlSource = source;
    }

    
    public abstract void validate();
}
