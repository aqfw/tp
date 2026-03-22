package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.ui.UiAction;

/**
 * Lists all existing {@code TagCombo}s.
 */
public class ListTagCombosCommand extends Command {
    public static final String COMMAND_WORD = "listtagcombo";

    public static final String MESSAGE_SUCCESS = "Listed all tag combos.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        return new CommandResult(MESSAGE_SUCCESS, UiAction.SHOW_TAG_COMBO);
    }
}
