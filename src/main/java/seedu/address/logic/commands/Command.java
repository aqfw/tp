package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    /**
     * Reverts the effect of the command on the given {@code Model}.
     *
     * <p>Commands that modify the state of the model should override this method
     * to provide a corresponding undo operation. Commands that do not support undo
     * may rely on this default implementation, which throws an exception.</p>
     *
     * @param model {@code Model} which the command should be reverted.
     * @throws CommandException If the undo operation fails.
     * @throws UnsupportedOperationException If the command does not support undo.
     */
    public void undo(Model model) throws CommandException {
        throw new UnsupportedOperationException("Undo not supported.");
    }
}
