package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.ExceededPersonListCapacityException;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.PersonContent;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_POSTAL_CODE + "POSTAL_CODE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_POSTAL_CODE + "120311 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";
    public static final String RIGHT_PANE_HEADER = "NEW CANDIDATE ADDED";
    public static final String RIGHT_PANE_HEADER_UNDO = "CANDIDATE DELETED";
    public static final String UNDO_SUCCESS = "Undo successful: Deleted person %1$s";
    public static final String REDO_SUCCESS = "Redo successful: Added person %1$s";

    private final Person toAdd;
    private Predicate<? super Person> previousPredicate;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        previousPredicate = model.getFilteredPersonPredicate();

        try {
            model.addPerson(toAdd);
        } catch (ExceededPersonListCapacityException e) {
            throw new CommandException(e.getMessage());
        }


        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)),
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(toAdd, RIGHT_PANE_HEADER)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand otherAddCommand)) {
            return false;
        }

        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }

    @Override
    public CommandResult undo(Model model) {
        model.deletePerson(toAdd);
        model.setFilteredPersonPredicate(previousPredicate);
        return new CommandResult(String.format(UNDO_SUCCESS, Messages.format(toAdd)),
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(toAdd, RIGHT_PANE_HEADER_UNDO)));
    }

    @Override
    public CommandResult redo(Model model) {
        model.addPerson(toAdd);
        return new CommandResult(String.format(REDO_SUCCESS, Messages.format(toAdd)),
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(toAdd, RIGHT_PANE_HEADER)));
    }
}
