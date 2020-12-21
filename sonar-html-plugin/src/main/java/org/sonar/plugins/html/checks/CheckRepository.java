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

import java.util.Arrays;
import java.util.List;

public class CheckRepository {
    public static final String REPOSITORY_KEY = "web";
    public static final String REPOSITORY_NAME = "HTML";
    private static final List<Class<? extends AbstractHtmlCheck>> CHECK_CLASSES = Arrays.asList(
            NotAllowedClassStyleCheck.class
            );

    private static final List<String> TEMPLATE_RULE_KEYS = Arrays.asList(
            NotAllowedClassStyleCheck.class.getSimpleName()
    );

    private CheckRepository() {
    }

    public static List<Class<? extends AbstractHtmlCheck>> getCheckClasses() {
        return CHECK_CLASSES;
    }

    public static List<String> getTemplateRuleKeys() {
        return TEMPLATE_RULE_KEYS;
    }
}
