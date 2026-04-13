package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class HelpWindowTest {

    private BufferedReader readerOf(String content) {
        return new BufferedReader(new StringReader(content));
    }

    // Happy cases
    @Test
    public void extractUserGuide_startFoundEndNull_returnsFromStartToEof() throws IOException {
        String input = "## Intro\nhello\n## Features\ncontent here\nmore content";
        UserGuideParser parser = new UserGuideParser();
        String result = parser.extractUserGuide(readerOf(input), "## Features", null);
        assertEquals("## Features\ncontent here\nmore content", result);
    }

    @Test
    public void extractUserGuide_startFoundEndFound_returnsFromStartToEnd() throws IOException {
        String input = "## Intro\nhello\n## Features\ncontent here\n## Next\nignore this";
        UserGuideParser parser = new UserGuideParser();
        String result = parser.extractUserGuide(readerOf(input), "## Features", "## Next");
        assertEquals("## Features\ncontent here", result);
    }

    @Test
    public void extractUserGuide_startAtFirstLine_returnsFromFirstLine() throws IOException {
        String input = "## Features\ncontent here\n## Next\nignore";
        UserGuideParser parser = new UserGuideParser();
        String result = parser.extractUserGuide(readerOf(input), "## Features", "## Next");
        assertEquals("## Features\ncontent here", result);
    }

    @Test
    public void extractUserGuide_startAtLastLine_returnsSingleLine() throws IOException {
        String input = "some content\n## Features";
        UserGuideParser parser = new UserGuideParser();
        String result = parser.extractUserGuide(readerOf(input), "## Features", null);
        assertEquals("## Features", result);
    }

    @Test
    public void extractUserGuide_adjacentHeadings_returnsStartHeadingOnly() throws IOException {
        String input = "## Features\n## Next\nignore";
        UserGuideParser parser = new UserGuideParser();
        String result = parser.extractUserGuide(readerOf(input), "## Features", "## Next");
        assertEquals("## Features", result);
    }

    @Test
    public void extractUserGuide_blankLinesBetweenHeadings_preservesBlanks() throws IOException {
        String input = "## Features\n\nsome content\n\n## Next";
        UserGuideParser parser = new UserGuideParser();
        String result = parser.extractUserGuide(readerOf(input), "## Features", "## Next");
        assertEquals("## Features\n\nsome content\n", result);
    }

    @Test
    public void extractUserGuide_endNotFound_returnsFromStartToEof() throws IOException {
        String input = "## Features\ncontent here\nmore content";
        UserGuideParser parser = new UserGuideParser();
        String result = parser.extractUserGuide(readerOf(input), "## Features", "## Missing");
        assertEquals("## Features\ncontent here\nmore content", result);
    }

    // Failure cases
    @Test
    public void extractUserGuide_startNotFound_throwsIoException() {
        String input = "## Intro\nhello\n## Other\ncontent";
        UserGuideParser parser = new UserGuideParser();
        try {
            parser.extractUserGuide(readerOf(input), "## Features", null);
            fail();
        } catch (IOException e) {
            assertEquals("startString not found: ## Features", e.getMessage());
        }
    }

    @Test
    public void extractUserGuide_emptyFile_throwsIoException() {
        UserGuideParser parser = new UserGuideParser();
        try {
            parser.extractUserGuide(readerOf(""), "## Features", null);
            fail();
        } catch (IOException e) {
            assertEquals("startString not found: ## Features", e.getMessage());
        }
    }

    @Test
    public void loadFromStream_validStream_returnsSection() throws IOException {
        String input = "## Features\ncontent here";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        UserGuideParser parser = new UserGuideParser();
        String result = parser.loadFromStream(stream, "## Features", null);
        assertEquals("## Features\ncontent here", result);
    }

    @Test
    public void loadFromStream_nullStream_throwsIoException() {
        UserGuideParser parser = new UserGuideParser();
        try {
            parser.loadFromStream(null, "## Features", null);
            fail();
        } catch (IOException e) {
            assertEquals("UserGuide.md not found in resources", e.getMessage());
        }
    }
}
