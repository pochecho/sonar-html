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

public class ChunkReplacement {
    private final String pattern;
    private String[] chunks;
    private String matchedValue;

    public ChunkReplacement(String pattern, String[] chunks) {
        this.pattern = pattern;
        this.chunks = chunks;
    }

    public String getMatchedValue() {
        return matchedValue;
    }

    public void setMatchedValue(String matchedValue) {
        this.matchedValue = matchedValue;
    }

    public String[] getChunks() {
        return chunks;
    }

    public String getPattern() {
        return pattern;
    }

    public void setChunks(String[] chunks) {
        this.chunks = chunks;

    }

    @Override
    public String toString() {
        String response = "Pattern: " + this.pattern + "\n";
        if(this.matchedValue != null){

            response += "Matched: "+ this.matchedValue + "\n";
        }
        if(this.chunks != null){

            for (String string : chunks) {
                
                response += "\t"+string+" - ";
            }
        }
        return response;
    }
}
