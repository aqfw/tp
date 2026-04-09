package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.PersonContent;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String UNDO_SUCCESS = "Undo successful: Added person %1$s";
    public static final String REDO_SUCCESS = "Redo successful: Deleted person %1$s";
    public static final String RIGHT_PANE_HEADER = "CANDIDATE DELETED";
    public static final String RIGHT_PANE_HEADER_UNDO = "NEW CANDIDATE ADDED";

    private final Index targetIndex;
    private Person deletedPerson;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        deletedPerson = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(deletedPerson);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(deletedPerson)),
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(deletedPerson, RIGHT_PANE_HEADER)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    @Override
    public CommandResult undo(Model model) {
        model.addPersonAtIndex(deletedPerson, targetIndex);
        return new CommandResult(String.format(UNDO_SUCCESS, Messages.format(deletedPerson)),
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(deletedPerson, RIGHT_PANE_HEADER_UNDO)));
    }

    @Override
    public CommandResult redo(Model model) {
        model.deletePerson(deletedPerson);
        return new CommandResult(String.format(REDO_SUCCESS, Messages.format(deletedPerson)),
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(deletedPerson, RIGHT_PANE_HEADER)));
    }
}
