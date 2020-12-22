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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.plugins.html.checks.HtmlIssue;
import org.sonar.plugins.html.models.*;

public class NotAllowedGenericElementCheck {

    private static final String BADLY_RULER_EXCEPTION = "There is a problem with the ruler,it is badly formed.";

    private final ConfigInvalidElementCheck config;

    private Replacement replacement;

    public NotAllowedGenericElementCheck(ConfigInvalidElementCheck config) {
        this.config = config;
    }

    public List<HtmlIssue> validate() {
        if (this.config.htmlSource == null) {
            throw new IllegalStateException("Source code not set, cannot validate anything");
        }
        this.getReplacements();
        List<HtmlIssue> issues;
        String content = null;
        try {
            content = this.config.htmlSource.contents();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.config.isMultiline) {
            issues = this.analyzeMultiline(content);
        } else {

            issues = this.analyzeInline(content);
        }
        return issues;
    }

    private List<HtmlIssue> analyzeMultiline(String content) {
        List<HtmlIssue> issues = new ArrayList<>();
        List<ClassPropertyWithChunks> issuesClass = getIssuesPerContent(content);
        int previousLines = 0;
        String contentTemp = content;

        for (ClassPropertyWithChunks classProperty : issuesClass) {

            int index = contentTemp.indexOf(classProperty.getClassValue());

            String preContent = contentTemp.substring(0, index);

            String[] preContentFragments = preContent.split("\n");

            if (classProperty.getChunks().length > 0) {
                for (ChunkReplacement issueClass : classProperty.getChunks()) {

                    HtmlIssue issue = generateIssue(
                            content,
                            previousLines + preContentFragments.length,
                            issueClass);
                    issues.add(issue);
                }
                previousLines += preContentFragments.length;
                contentTemp = contentTemp.substring(index + classProperty.getClassValue().length());
            }
        }
        return issues;
    }

    private List<HtmlIssue> analyzeInline(String content) {
        List<HtmlIssue> issues = new ArrayList<>();
        String[] fragments = content.split("\n");

        int i = 1;
        for (String line : fragments) {
            ChunkReplacement[] issuesClass = getIssuesPerLine(line);
            if (issuesClass.length > 0) {
                for (ChunkReplacement issueClass : issuesClass) {
                    HtmlIssue issue = generateIssue(line, i, issueClass);
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
            System.out.println("linea"+ line);
            System.out.println("matcher"+ matcher.group());
            String[] classes = this.getFormattedClasses(matcher.group(1));
            intersection = this.getIntersection(classes, this.replacement.getReplacements());
        }
        return intersection;
    }


    private List<ClassPropertyWithChunks> getIssuesPerContent(String content) {
        final String patterString = this.config.pattern;

        Pattern pattern = Pattern.compile(patterString);
        Matcher matcher = pattern.matcher(content);
        List<ClassPropertyWithChunks> response = new ArrayList<ClassPropertyWithChunks>();

        List<ChunkReplacement[]> intersection = new ArrayList<ChunkReplacement[]>();
        while (matcher.find()) {
            String[] classes = this.getFormattedClasses(matcher.group(1));

            ChunkReplacement[] inter = this.getIntersection(classes, this.replacement.getReplacements());
            response.add(new ClassPropertyWithChunks(inter, matcher.group(1)));
            intersection.add(inter);
        }
        return response;
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

    private HtmlIssue generateIssue(String line, int lineNumber, ChunkReplacement issueClass) {

        String message = String.format(this.config.issueFoundedMessage, issueClass.getMatchedValue(),
                generateSuggestedClass(issueClass));
        this.config.LOGGER.info(message);
        String group = issueClass.getMatchedValue();
        int column = line.indexOf(group);
        Location location = new Location(lineNumber, column);
        HtmlIssue issue = new HtmlIssue(this.config.ruleKey, lineNumber, message);
        return issue;
    }
}
