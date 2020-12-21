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

import org.sonar.api.rule.RuleKey;

public class Issue {
    private final RuleKey ruleKey;
    private final Location location;
    private final String message;

    public Issue(RuleKey ruleKey, Location location, String message) {
        this.ruleKey = ruleKey;
        this.location = location;
        this.message = message;
    }

    public RuleKey getRuleKey() {
        return ruleKey;
    }

    public int getLine() {
        return this.location.getLine();
    }

    public int getColumn() {
        return this.location.getColumn();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Line: " + this.getLine() + ", Column: " + this.getColumn() + ": " + this.getMessage();
    }
}
