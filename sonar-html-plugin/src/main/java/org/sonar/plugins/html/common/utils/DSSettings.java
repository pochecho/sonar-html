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
package org.sonar.plugins.html.common.utils;

import org.sonar.plugins.html.languages.HtmlLanguage;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import java.util.List;

import static java.util.Arrays.asList;

public class DSSettings {
    public static final String FILE_SUFFIXES_KEY = "sonar.ds.file.suffixes";
    public static final String DEFAULT_FILE_SUFFIXES = ".html";
    private DSSettings() {
    }

    public static List<PropertyDefinition> getProperties() {
        return asList(
                PropertyDefinition.builder(FILE_SUFFIXES_KEY)
                        .name("File Suffixes")
                        .description("Comma-separated list of suffixes for files to analyze." +
                                " To not filter, leave the list empty.")
                        .defaultValue(DEFAULT_FILE_SUFFIXES)
                        .multiValues(true)
                        .category(HtmlLanguage.NAME)
                        .onQualifiers(Qualifiers.PROJECT)
                .build());

    }
}
