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

import org.sonar.plugins.html.models.ChunkReplacement;
import org.sonar.plugins.html.models.ConfigInvalidElementCheck;
import org.sonar.plugins.html.models.Issue;
import org.sonar.plugins.html.models.Location;
import org.sonar.plugins.html.models.Replacement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.check.Rule;

@Rule(key = "NotAllowedClassStyleCheck")
public class NotAllowedGenericElementCheck {

    private static final String BADLY_RULER_EXCEPTION = "There is a problem with the ruler,it is badly formed.";

    private final ConfigInvalidElementCheck config;

    private Replacement replacement;

    public NotAllowedGenericElementCheck(ConfigInvalidElementCheck config) {
        this.config = config;
    }

    public List<Issue> validate() {
        if (this.config.htmlSource == null) {
            throw new IllegalStateException("Source code not set, cannot validate anything");
        }
        this.getReplacements();
        List<Issue> issues = new ArrayList<>();
        String content = this.config.htmlSource.getContents();

        if (this.config.isMultiline) {
            issues = this.analyzeMultiline(content);
        } else {
            issues = this.analyzeInline(content);

        }

        return issues;
    }

    private List<Issue> analyzeMultiline(String content) {
        List<Issue> issues = new ArrayList<>();
        String[] fragments = content.split("\n");

        int i = 1;
        List<ChunkReplacement[]> issuesClass = getIssuesPerContent(content);

        // for (ChunkReplacement chunkReplacement : issuesClass) {

        // System.out.println("--------- "+chunkReplacement);

        // }
        String contentTemp = content;
        int increment = 0;
        for (ChunkReplacement[] chunkReplacements : issuesClass) {
            System.out.println("upa");
            if (chunkReplacements.length > 0) {
                for (ChunkReplacement issueClass : chunkReplacements) {
                    Issue issue = generateIssue(contentTemp, i, issueClass,increment);
                    contentTemp = content.substring(issue.getColumn() + issueClass.getMatchedValue().length(),
                            content.length());
                    issues.add(issue);
                    increment+=issue.getColumn();
                }
            }
        }
        i++;
        return issues;
    }

    private List<Issue> analyzeInline(String content) {
        List<Issue> issues = new ArrayList<>();

        String[] fragments = content.split("\n");

        int i = 1;
        for (String line : fragments) {
            ChunkReplacement[] issuesClass = getIssuesPerLine(line);
            if (issuesClass.length > 0) {
                for (ChunkReplacement issueClass : issuesClass) {
                    Issue issue = generateIssue(line, i, issueClass,0);
                    issues.add(issue);
                }
            }
            i++;
        }
        return issues;
    }

    private String[] getChunks(String classDefinition) {
        Pattern pattern = Pattern.compile("([\\{]{1}[0-9]+[\\}]{1})", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(classDefinition);
        List<String> response = new ArrayList<String>();
        while (matcher.find()) {
            response.add(matcher.group());
        }
        return (String[]) response.toArray(new String[0]);
    }

    private void getReplacements() {
        String[] mainFragments = this.config.classStyleReg.split("=");
        if (mainFragments.length == 2 && mainFragments[0] != "" && mainFragments[1] != "") {

            String[] classes = this.getFormattedClasses(mainFragments[1]);
            ChunkReplacement[] chunks = new ChunkReplacement[classes.length];
            int i = 0;
            for (String classe : classes) {

                ChunkReplacement chunkReplacement = new ChunkReplacement(classe, null);
                chunks[i] = chunkReplacement;
                i++;
            }

            ChunkReplacement chunkReplacementPattern = new ChunkReplacement(mainFragments[0],
                    getChunks(mainFragments[0]));

            this.replacement = new Replacement(chunkReplacementPattern, chunks);

        } else {
            throw new IllegalStateException(BADLY_RULER_EXCEPTION);
        }
    }

    private String generateSuggestedClass(ChunkReplacement replacement) {
        String response = this.replacement.getPattern().getPattern();
        if (this.replacement.getPattern().getChunks().length > 0) {

            for (String string : this.replacement.getPattern().getChunks()) {
                String temp = string.substring(1);
                temp = temp.substring(0, temp.length() - 1);
                int group = Integer.parseInt(temp);
                if (group < replacement.getChunks().length) {

                    response = response.replaceAll("\\{" + group + "\\}", replacement.getChunks()[group]);
                }
            }
        } else {
            response = this.replacement.getPattern().getPattern();
        }
        return response;
    }

    private String[] getFormattedClasses(String classesString) {
        String[] classes = classesString.split(";| |\n");
        List<String> response = new ArrayList<String>();
        for (int i = classes.length - 1; i >= 0; i--) {
            String classWithNoSpaces = classes[i].trim().replace("\n", "").replace("\t", "").replace("\r", "");
            if (classWithNoSpaces.length() > 0 && classWithNoSpaces != "" && classWithNoSpaces != " "
                    && classWithNoSpaces != "\n" && classWithNoSpaces != "\t") {
                response.add(classWithNoSpaces);
            }
        }
        return (String[]) response.toArray(new String[0]);
    }

    private ChunkReplacement[] getIssuesPerLine(String line) {
        final String patterString = this.config.pattern;

        Pattern pattern = Pattern.compile(patterString);
        Matcher matcher = pattern.matcher(line);

        ChunkReplacement[] intersection = new ChunkReplacement[0];
        if (matcher.find()) {
            String[] classes = this.getFormattedClasses(matcher.group(1));
            intersection = this.getIntersection(classes, this.replacement.getReplacements());
        }
        return intersection;
    }

    private List<ChunkReplacement[]> getIssuesPerContent(String line) {
        final String patterString = this.config.pattern;

        Pattern pattern = Pattern.compile(patterString);
        Matcher matcher = pattern.matcher(line);

        System.out.println("La linea es la siguiente::: " + line);
        List<ChunkReplacement[]> intersection = new ArrayList<ChunkReplacement[]>();
        while (matcher.find()) {
            System.out.println("Encontró => " + matcher.group(1));
            String[] classes = this.getFormattedClasses(matcher.group(1));
            intersection.add(this.getIntersection(classes, this.replacement.getReplacements()));
        }
        return intersection;
    }

    private ChunkReplacement[] getIntersection(String[] classesRulers, ChunkReplacement[] classesInline) {

        HashSet<ChunkReplacement> set = new HashSet<>();
        for (ChunkReplacement patternString : classesInline) {
            for (String classRuler : classesRulers) {
                Pattern pattern = Pattern.compile("^" + patternString.getPattern() + "$");
                Matcher matcher = pattern.matcher(classRuler);
                if (matcher.find()) {
                    String[] chunks = new String[matcher.groupCount()];
                    for (int i = 0; i < matcher.groupCount(); i++) {
                        chunks[i] = matcher.group(i + 1);
                    }

                    patternString.setChunks(chunks);
                    patternString.setMatchedValue(matcher.group());
                    set.add(patternString);
                }
            }
        }

        ChunkReplacement[] intersection = {};
        intersection = set.toArray(intersection);
        return intersection;
    }

    private Issue generateIssue(String line, int lineNumber, ChunkReplacement issueClass,int increment) {

        String message = String.format(this.config.issueFoundedMessage, issueClass.getMatchedValue(),
                generateSuggestedClass(issueClass));
        this.config.LOGGER.info(message);
        String group = issueClass.getMatchedValue();
        int column = line.indexOf(group)+increment;
        Location location = new Location(lineNumber, column);
        Issue issue = new Issue(this.config.ruleKey, location, message);
        return issue;
    }
}
