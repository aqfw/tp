package seedu.address.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://se-education.org/addressbook-level3/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    // Path to UserGuide.md relative to project root
    private static final String USERGUIDE_PATH = "docs/Userguide.md";

    // Heading line from Userguide.md to start from
    private static final String START_HEADING = "## Features";

    // The heading line to stop reading at (exclusive), or null to read to end of file
    private static final String END_HEADING = null;


    @FXML
    private Button copyButton;

    @FXML
    private WebView helpMessage;


    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        String markdown = loadUserGuide();
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);
        helpMessage.getEngine().loadContent(html);

        String docsPath = new File("docs").toURI().toString();

        String styled = "<html><head>"
                + "<base href='" + docsPath + "/'>"
                + "<style>"
                + "body { background-color: #454545; color: white; font-family: sans-serif; padding: 10px; }"
                + "code { background-color: #333; padding: 2px 4px; }"
                + "img { max-width: 100%; }"
                + "</style></head><body>"
                + html
                + "</body></html>";

        helpMessage.getEngine().loadContent(styled);
    }

    // No longer required.
    /*
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(loadUserGuide());
        helpMessage.setWrapText(true);
        helpMessage.setEditable(false);
    }
    */

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Reads UserGuide.md from the relative path USERGUIDE_PATH,
     * runs extractUserGuide to extract UserGuide from START_HEADING to END_HEADING
     */
    private String loadUserGuide() {
        try {

            FileInputStream userGuideInput = new FileInputStream(USERGUIDE_PATH);
            InputStreamReader inputStreamReader = new InputStreamReader(userGuideInput);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            try {
                UserGuideParser parser = new UserGuideParser();
                return parser.extractUserGuide(reader, START_HEADING, END_HEADING);
            } finally {
                reader.close();
            }

        } catch (Exception e) {
            logger.warning("Failed to load UserGuide.md from path " + USERGUIDE_PATH
                    + e.getMessage());
            return "Failed to load local user-guide. \n Visit: " + USERGUIDE_URL
                    + "instead";
        }

    }

    /* Not easily testable with JavaFX. Needs to be moved out.
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
        for (int i = startingLine; i< endingLine; i++) {
            result.append(lines.get(i));
            result.append("\n");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length()-1);
        }

        return result.toString();

    }*/


    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
