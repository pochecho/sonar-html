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
package org.sonar.plugins.html;

import org.sonar.plugins.html.languages.HtmlFileSystem;
import org.sonar.plugins.html.languages.HtmlLanguage;
import org.sonar.plugins.html.languages.HtmlQualityProfile;
import org.sonar.plugins.html.common.utils.DSSettings;
import org.sonar.plugins.html.rules.HtmlRulesDefinition;
import org.sonar.api.Plugin;

public class DSPlugin implements Plugin {

    @Override
    public void define(Context context) {
        context.addExtensions(
                HtmlLanguage.class,
                HtmlFileSystem.class,
                HtmlSensor.class,
                HtmlMetrics.class,
                HtmlQualityProfile.class,
                HtmlRulesDefinition.class
        );
        context.addExtensions(DSSettings.getProperties());
    }
}
