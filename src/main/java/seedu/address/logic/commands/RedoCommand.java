package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Reverts the last undoable command.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return model.redo();
    }
}
