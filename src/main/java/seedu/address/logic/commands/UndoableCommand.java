package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command that can be reverted.
 */
public abstract class UndoableCommand extends Command {

    /**
     * Reverts the effects of this command on the given {@code Model}.
     */
    public abstract CommandResult undo(Model model) throws CommandException;

    public abstract CommandResult redo(Model model) throws CommandException;
}
