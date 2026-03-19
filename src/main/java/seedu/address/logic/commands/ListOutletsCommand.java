package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Lists all outlets in the address book to the user.
 */
public class ListOutletsCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_SUCCESS = "Listed all outlets";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.resetFilteredOutletList();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof ListOutletsCommand;
    }
}
