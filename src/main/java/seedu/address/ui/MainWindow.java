package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private TagComboPanel tagComboPanel;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane detailedPersonPlaceholder;

    @FXML
    private StackPane outletListPanelPlaceholder;

    @FXML
    private VBox rightDisplayPlaceHolder;

    @FXML
    private VBox tagComboDisplayPlaceHolder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
        personListPanel.setOnPersonSelected((person, header) -> {
            DetailedPersonCard card = new DetailedPersonCard(person, header);
            detailedPersonPlaceholder.getChildren().setAll(card.getRoot());
            showDetails();
        });

        OutletListPanel outletListPanel = new OutletListPanel(logic.getFilteredOutletList());
        outletListPanelPlaceholder.getChildren().add(outletListPanel.getRoot());

        tagComboPanel = new TagComboPanel(logic.getTagComboList());
        tagComboDisplayPlaceHolder.getChildren().add(tagComboPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
        logger.info("Opened help window.");
    }

    /**
     * Displays the selected person on the right as a {@code DetailedPersonCard}.
     */
    public void showPersonDetails(Person person, int displayedIndex) {
        DetailedPersonCard detailedCard = new DetailedPersonCard(person, "Candidate #" + displayedIndex);
        detailedPersonPlaceholder.getChildren().setAll(detailedCard.getRoot());
    }

    /**
     * Displays the selected person on the right as a {@code DetailedPersonCard}.
     */
    public void showPersonDetails(Person person, String header) {
        DetailedPersonCard detailedCard = new DetailedPersonCard(person, header);
        rightDisplayPlaceHolder.getChildren().setAll(detailedCard.getRoot());
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Makes the TagComboPanel visible on the right pane.
     */
    private void showTagCombo() {
        tagComboDisplayPlaceHolder.setVisible(true);
        tagComboDisplayPlaceHolder.setManaged(true);
        rightDisplayPlaceHolder.setVisible(false);
        rightDisplayPlaceHolder.setManaged(false);

        logger.info("Right pane placeholder hidden, right pane now showing TagCombos panel.");
    }

    /**
     * Makes the rightDisplayPlaceHolder visible on the right pane.
     */
    private void showDetails() {
        tagComboDisplayPlaceHolder.setVisible(false);
        tagComboDisplayPlaceHolder.setManaged(false);
        rightDisplayPlaceHolder.setVisible(true);
        rightDisplayPlaceHolder.setManaged(true);

        logger.info("TagCombos Panel hidden, right pane now showing person/tagcount info.");
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.getUiAction() == UiAction.SHOW_HELP) {
                handleHelp();
            }

            if (commandResult.getUiAction() == UiAction.EXIT) {
                handleExit();
            }

            if (commandResult.getUiAction() == UiAction.UPDATE_RIGHT_PANE) {
                showDetails();
                commandResult.getContent().get().render(rightDisplayPlaceHolder);
            }

            if (commandResult.getUiAction() == UiAction.SHOW_TAG_COMBO) {
                showTagCombo();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
