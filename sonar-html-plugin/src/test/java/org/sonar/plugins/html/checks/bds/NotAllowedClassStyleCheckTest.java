package co.com.bancolombia.sonar.plugins.design.system.checks;

import co.com.bancolombia.sonar.plugins.design.system.Utils;
import co.com.bancolombia.sonar.plugins.design.system.models.Issue;
import co.com.bancolombia.sonar.plugins.design.system.models.Source;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class NotAllowedClassStyleCheckTest {
    @Rule
    public LogTester logTester = new LogTester();

    @Test
    public void testFailedValidateNoSource() {
        NotAllowedClassStyleCheck check = new NotAllowedClassStyleCheck();
        try {
            check.validate();
            fail("No source code should raise an exception");
        } catch (IllegalStateException e) {
            assertEquals("Source code not set, cannot validate anything", e.getMessage());
        }
    }

    @Test
    public void shouldHandleIssue() throws IOException {
        // Arrange
        Source source = getSource("invalid-class-inline.html");
        NotAllowedClassStyleCheck check = new NotAllowedClassStyleCheck();
        check.setHtmlSource(source);
        check.classStyleReg = "bc-accordions-group=accordion;mat-accordion";
        // Act
        check.validate();
        // Assert
       
        assertEquals(2, source.getIssues().size());
    }

    @Test
    public void shouldHandleRegexIssue() throws IOException {
        // Arrange
        Source source = getSource("invalid-class-layout.html");
        NotAllowedClassStyleCheck check = new NotAllowedClassStyleCheck();
        check.setHtmlSource(source);
        check.classStyleReg = "bc-{0}-{1}-{2}=(col)-([xssmmdlgxl]{0,2})-([0-9]{1,2});(offset)-([xssmmdlgxl]{0,2})-([0-9]{1,2})";
        // Act
        check.validate();
        // Assert
        assertEquals(1, source.getIssues().size());
    }



    @Test
    public void shouldHandleIssueWithLineBreaker() throws IOException {
        // Arrange
        Source source = getSource("invalid-class-multiline.html");
        NotAllowedClassStyleCheck check = new NotAllowedClassStyleCheck();
        check.setHtmlSource(source);
        check.classStyleReg = "bc-accordions-group=accordion;mat-accordion";
        String message = String.format(NotAllowedClassStyleCheck.ISSUE_FOUNDED_MESSAGE,"mat-accordion","bc-accordions-group");
        // Act
        check.validate();

        for (Issue issue : source.getIssues()) {
            System.out.println(issue);
        }

        // Assert
        assertEquals(2, logTester.logs(LoggerLevel.INFO).size());
        assertThat(logTester.logs(LoggerLevel.INFO).get(0), containsString(message));
        assertEquals(2, source.getIssues().size());
    }
    


    private Source getSource(String filename) throws IOException {
        return new Source(Utils.getInputFile("invalid-class/" + filename));
    }
}
