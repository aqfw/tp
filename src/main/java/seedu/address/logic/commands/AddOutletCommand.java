package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.outlet.exceptions.DuplicateOutletException;

/**
 * Adds an outlet to the address book.
 */
public class AddOutletCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = "outlet " + COMMAND_WORD + ": Adds an outlet.\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_POSTAL_CODE + "POSTAL_CODE\n"
            + "Constraints: NAME max 26 chars, ADDRESS max 35 chars, and values must not contain n/, a/, or pc/.\n"
            + "Example: outlet " + COMMAND_WORD + " "
            + PREFIX_NAME + "TechCo "
            + PREFIX_ADDRESS + "Raffles Place "
            + PREFIX_POSTAL_CODE + "048623";

    public static final String MESSAGE_SUCCESS = "New outlet added: %1$s";
    public static final String MESSAGE_DUPLICATE_OUTLET = "This outlet already exists in the address book";

    private final Outlet toAdd;

    /**
     * Creates an AddOutletCommand to add the specified {@code Outlet}.
     */
    public AddOutletCommand(Outlet outlet) {
        requireNonNull(outlet);
        toAdd = outlet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            model.addOutlet(toAdd);
        } catch (DuplicateOutletException e) {
            throw new CommandException(MESSAGE_DUPLICATE_OUTLET);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public void undo(Model model) {
        model.deleteOutlet(toAdd);
    }

    @Override
    public void redo(Model model) {
        model.addOutlet(toAdd);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddOutletCommand otherAddOutletCommand)) {
            return false;
        }

        return toAdd.equals(otherAddOutletCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
