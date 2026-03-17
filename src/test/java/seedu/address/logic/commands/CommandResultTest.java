package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddCommand.RIGHT_PANE_HEADER;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.ui.UiAction;
import seedu.address.ui.content.PersonContent;

public class CommandResultTest {
    @Test
    public void equals() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns true
        assertTrue(commandResult.equals(new CommandResult("feedback")));
        assertTrue(commandResult.equals(new CommandResult("feedback", UiAction.NONE, Optional.empty())));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(new CommandResult("different")));

        // different UiAction -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", UiAction.EXIT, Optional.empty())));

        // different RightPaneContent -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", UiAction.NONE,
                Optional.of(new PersonContent(ALICE, RIGHT_PANE_HEADER)))));
    }

    @Test
    public void hashcode() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(), new CommandResult("feedback").hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("different").hashCode());

        // different UiAction -> returns false
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", UiAction.EXIT,
                Optional.empty()).hashCode());

        // different RightPaneContent -> returns false
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", UiAction.NONE,
                Optional.of(new PersonContent(ALICE , RIGHT_PANE_HEADER))).hashCode());
    }

    @Test
    public void toStringMethod() {
        CommandResult commandResult = new CommandResult("feedback", UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new PersonContent(ALICE , RIGHT_PANE_HEADER)));
        String expected = CommandResult.class.getCanonicalName() + "{feedbackToUser="
                + commandResult.getFeedbackToUser()
                + ", uiAction=" + commandResult.getUiAction()
                + ", rightPaneContent=" + commandResult.getContent() + "}";
        assertEquals(expected, commandResult.toString());
    }
}
