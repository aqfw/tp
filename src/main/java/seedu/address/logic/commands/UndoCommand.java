package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Reverts the last undoable command.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        model.undo();
        return new CommandResult("Undo successful!");
    }
}
