package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CompareCommand.MESSAGE_COMPARE_SUCCESS;
import static seedu.address.logic.commands.CompareCommand.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.commands.CompareCommand.MESSAGE_SAME_INDEX;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.ComparisonContent;

public class CompareCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // Unit tests for correctness

    @Test
    public void execute_validIndices_successWithCorrectUiAction() throws CommandException {
        Index firstIndex = Index.fromOneBased(1);
        Index secondIndex = Index.fromOneBased(2);

        CompareCommand command = new CompareCommand(firstIndex, secondIndex);
        CommandResult result = command.execute(model);

        assertEquals(UiAction.UPDATE_RIGHT_PANE, result.getUiAction());
    }

    @Test
    public void execute_validIndices_successMessageContainsBothIndices() throws CommandException {
        Index firstIndex = Index.fromOneBased(1);
        Index secondIndex = Index.fromOneBased(2);

        CompareCommand command = new CompareCommand(firstIndex, secondIndex);
        CommandResult result = command.execute(model);

        String expectedMessage = String.format(MESSAGE_COMPARE_SUCCESS, 1, 2);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_validIndices_contentIsComparisonContent() throws CommandException {
        Index firstIndex = Index.fromOneBased(1);
        Index secondIndex = Index.fromOneBased(2);

        CompareCommand command = new CompareCommand(firstIndex, secondIndex);
        CommandResult result = command.execute(model);

        assertTrue(result.getContent().isPresent());
        assertTrue(result.getContent().get() instanceof ComparisonContent);
    }

    @Test
    public void execute_indicesInReverseOrder_successWithCorrectHeaders() throws CommandException {
        Index firstIndex = Index.fromOneBased(3);
        Index secondIndex = Index.fromOneBased(1);

        CompareCommand command = new CompareCommand(firstIndex, secondIndex);
        CommandResult result = command.execute(model);

        String expectedMessage = String.format(MESSAGE_COMPARE_SUCCESS, 3, 1);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_lastTwoValidIndices_success() throws CommandException {
        int listSize = model.getFilteredPersonList().size();
        Index firstIndex = Index.fromOneBased(listSize - 1);
        Index secondIndex = Index.fromOneBased(listSize);

        CompareCommand command = new CompareCommand(firstIndex, secondIndex);
        CommandResult result = command.execute(model);

        assertEquals(UiAction.UPDATE_RIGHT_PANE, result.getUiAction());
    }

    // Tests for invalid inputs

    @Test
    public void execute_sameIndex_throwsCommandException() {
        Index index = Index.fromOneBased(1);
        CompareCommand command = new CompareCommand(index, index);

        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(MESSAGE_SAME_INDEX, thrown.getMessage());
    }

    @Test
    public void execute_firstIndexOutOfRange_throwsCommandException() {
        int listSize = model.getFilteredPersonList().size();
        Index outOfRange = Index.fromOneBased(listSize + 1);
        Index valid = Index.fromOneBased(1);

        CompareCommand command = new CompareCommand(outOfRange, valid);

        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(MESSAGE_INVALID_INDEX, listSize), thrown.getMessage());
    }

    @Test
    public void execute_secondIndexOutOfRange_throwsCommandException() {
        int listSize = model.getFilteredPersonList().size();
        Index valid = Index.fromOneBased(1);
        Index outOfRange = Index.fromOneBased(listSize + 1);

        CompareCommand command = new CompareCommand(valid, outOfRange);

        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(MESSAGE_INVALID_INDEX, listSize), thrown.getMessage());
    }

    @Test
    public void execute_bothIndicesOutOfRange_throwsCommandException() {
        int listSize = model.getFilteredPersonList().size();
        Index outOfRange1 = Index.fromOneBased(listSize + 1);
        Index outOfRange2 = Index.fromOneBased(listSize + 2);

        CompareCommand command = new CompareCommand(outOfRange1, outOfRange2);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

}
