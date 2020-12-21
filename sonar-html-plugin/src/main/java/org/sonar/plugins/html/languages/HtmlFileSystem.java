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
package org.sonar.plugins.html.languages;

import org.sonar.api.batch.ScannerSide;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;

import java.util.ArrayList;
import java.util.List;

@ScannerSide
public class HtmlFileSystem {
    private final FileSystem fileSystem;
    private final FilePredicates predicates;
    private final FilePredicate isHtmlLanguage;
    private final FilePredicate isMainTypeFile;

    public HtmlFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.predicates = fileSystem.predicates();
        this.isHtmlLanguage = predicates.hasLanguage(HtmlLanguage.KEY);
        this.isMainTypeFile = predicates.hasType(Type.MAIN);
    }

    public boolean hasHtmlFiles() {
        return fileSystem.hasFiles(isHtmlLanguage);
    }

    public List<InputFile> sourceInputFiles() {
        Iterable<InputFile> files = fileSystem.inputFiles(predicates.and(isHtmlLanguage, isMainTypeFile));
        List<InputFile> list = new ArrayList<>();
        files.iterator().forEachRemaining(list::add);
        return list;
    }
}
