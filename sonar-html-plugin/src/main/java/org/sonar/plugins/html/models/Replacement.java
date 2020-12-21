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

public class Replacement {

    private final ChunkReplacement pattern;
    private final ChunkReplacement[] replacements;

    public Replacement(ChunkReplacement pattern, ChunkReplacement[] replacements) {
        this.pattern = pattern;
        this.replacements = replacements;
    }

    public ChunkReplacement getPattern() {
        return pattern;
    }

    public ChunkReplacement[] getReplacements() {
        return replacements;
    }

    @Override
    public String toString() {
        String response = "";
        response += "Pattern: \n\t" + this.pattern.toString();
        response += "\nReplacement: \n\t";
        for (ChunkReplacement chunkReplacement : this.replacements) {
            response +=  chunkReplacement.toString()+"\n\t";
        }
        return response;
    }
}
