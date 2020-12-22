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

public class NotAllowedTagCheckTest {
    @Rule
    public LogTester logTester = new LogTester();

    @Test
    public void testFailedValidateNoSource() {
        NotAllowedTagCheck check = new NotAllowedTagCheck();
        try {
            check.validate();
            fail("No source code should raise an exception");
        } catch (IllegalStateException e) {
            assertEquals("Source code not set, cannot validate anything", e.getMessage());
        }
    }

    
    @Test
    public void shouldHandleInvalidTagIssue() throws IOException {
        // Arrange
        Source source = getSource("invalid-tag.html");
        NotAllowedTagCheck check = new NotAllowedTagCheck();
        check.setHtmlSource(source);
        check.classStyleReg = "bc-accordions-group=mat-accordion";
        // Act
        check.validate();
        for (Issue issue : source.getIssues()) {
            System.out.println(issue);
        }
        // Assert
        assertEquals(1, source.getIssues().size());
    }

    


    private Source getSource(String filename) throws IOException {
        return new Source(Utils.getInputFile("invalid-tag/" + filename));
    }
}
