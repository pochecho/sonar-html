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

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

public class HtmlMetrics implements Metrics {
    private static final Metric<Double> NOT_ALLOWED_CLASS_STYLE = new Metric.Builder(
            "not_allowed_class_style",
            "Number of classes not allowed in html files",
            Metric.ValueType.FLOAT)
            .setDescription("Shows the number of classes that should not be used defined by design system")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(true)
            .setDomain(CoreMetrics.DOMAIN_RELIABILITY)
            .create();

    @SuppressWarnings("rawtypes")
    @Override
    public List<Metric> getMetrics() {
        return Arrays.asList(NOT_ALLOWED_CLASS_STYLE);
    }
}
