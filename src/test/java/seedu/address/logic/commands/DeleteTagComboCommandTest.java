package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;
import static seedu.address.testutil.TypicalTagCombos.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.TagCombo;
import seedu.address.ui.UiAction;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteTagComboCommand}.
 */
public class DeleteTagComboCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        TagCombo tagComboToDelete = model.getTagComboList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteTagComboCommand deleteTagComboCommand = new DeleteTagComboCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(DeleteTagComboCommand.MESSAGE_DELETE_TAG_COMBO_SUCCESS,
                Messages.format(tagComboToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTagCombo(tagComboToDelete);

        assertCommandSuccess(deleteTagComboCommand, model, expectedMessage, expectedModel,
                UiAction.SHOW_TAG_COMBO, Optional.empty());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getTagComboList().size() + 1);
        DeleteTagComboCommand deleteTagComboCommand = new DeleteTagComboCommand(outOfBoundIndex);

        assertCommandFailure(deleteTagComboCommand, model, Messages.MESSAGE_INVALID_TAG_COMBO_DISPLAYED_INDEX);
    }

    @Test
    public void undo_deleteTagComboCommand_success() throws CommandException {
        TagCombo tagComboToDelete = model.getTagComboList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteTagComboCommand deleteTagComboCommand = new DeleteTagComboCommand(INDEX_FIRST_ENTRY);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        deleteTagComboCommand.execute(model);
        assertFalse(model.hasTagCombo(tagComboToDelete));
        assertTrue(expectedModel.hasTagCombo(tagComboToDelete));
        model.recordCommand(deleteTagComboCommand);

        model.undo();
        assertTrue(model.hasTagCombo(tagComboToDelete));
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_deleteTagComboCommand_success() throws CommandException {
        TagCombo tagComboToDelete = model.getTagComboList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteTagComboCommand deleteTagComboCommand = new DeleteTagComboCommand(INDEX_FIRST_ENTRY);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        deleteTagComboCommand.execute(model);
        expectedModel.deleteTagCombo(tagComboToDelete);
        model.recordCommand(deleteTagComboCommand);

        model.undo();
        model.redo();
        assertEquals(expectedModel, model);
    }

    @Test
    public void equals() {
        DeleteTagComboCommand firstDeleteTagComboCommand = new DeleteTagComboCommand(INDEX_FIRST_ENTRY);
        DeleteTagComboCommand secondDeleteTagComboCommand = new DeleteTagComboCommand(INDEX_SECOND_ENTRY);

        // same object -> returns true
        assertTrue(firstDeleteTagComboCommand.equals(firstDeleteTagComboCommand));

        // same values -> returns true
        DeleteTagComboCommand firstDeleteTagComboCommandCopy = new DeleteTagComboCommand(INDEX_FIRST_ENTRY);
        assertTrue(firstDeleteTagComboCommand.equals(firstDeleteTagComboCommandCopy));

        // different types -> returns false
        assertFalse(firstDeleteTagComboCommand.equals(1));

        // null -> returns false
        assertFalse(firstDeleteTagComboCommand.equals(null));

        // different index -> returns false
        assertFalse(firstDeleteTagComboCommand.equals(secondDeleteTagComboCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteTagComboCommand deleteTagComboCommand = new DeleteTagComboCommand(targetIndex);
        String expected = DeleteTagComboCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteTagComboCommand.toString());
    }
}
