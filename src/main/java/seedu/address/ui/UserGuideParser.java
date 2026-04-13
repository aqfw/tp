package seedu.address.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * The Class UserGuideParser uses the method extractUserGuide to parse a markdown file. It is
 * used by HelpWindow.java to parse a markdown file to show the user to provide help.
 *
 * <p>It reads all lines from a {@link BufferedReader} and extracts the
 * portion of text starting from a specified heading line up to but not including
 * an optional end heading line. If no end heading is specified, the parser reads
 * to the end of the file.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     BufferedReader reader = new BufferedReader(new FileReader("UserGuide.md"));
 *     UserGuideParser parser = new UserGuideParser();
 *     String section = parser.extractUserGuide(reader, "## Features", "## FAQ");
 * </pre>
 *
 * It is a separate class from HelpWindow.java to permit easier testing via the JUnit framework, by obviating
 * the need to instantiate JavaFX.
 */
public class UserGuideParser {

    // ExtractUserGuide is a helper method used to contain the file reading logic for easier testing
    String extractUserGuide(BufferedReader reader, String startString, String endString) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        String line = reader.readLine();

        // Consumes all lines in the UserGuide
        while (line != null) {
            lines.add(line);
            line = reader.readLine();
        }

        // Checks for a match to the start heading or string to determine the i-th line to start at
        int startingLine = -1; // Forces an exception if the startString cannot be found
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(startString)) {
                startingLine = i;
                break;
            }
        }
        if (startingLine == -1) {
            throw new IOException("startString not found: " + startString);
        }

        // Checks for a match to the end string to determine where to stop, otherwise just reach end of file
        int endingLine = lines.size();
        for (int i = startingLine + 1; endString != null && i < lines.size(); i++) {
            if (!lines.get(i).startsWith(endString)) {
                continue;
            }
            endingLine = i;
            break;
        }

        // Takes the ArrayList of lines starting from startingLine, builds them into a string for
        // loadUserGuide to pass to HelpWindow
        StringBuilder result = new StringBuilder();
        for (int i = startingLine; i < endingLine; i++) {
            result.append(lines.get(i));
            result.append("\n");
        }
        if (!result.isEmpty()) {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();

    }

    //Null check done within UserGuideParser to facilitate testing
    String loadFromStream(InputStream stream, String startString, String endString) throws IOException {
        if (stream == null) {
            throw new IOException("UserGuide.md not found in resources");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            return extractUserGuide(reader, startString, endString);
        } finally {
            reader.close();
        }
    }

}
