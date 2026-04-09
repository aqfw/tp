package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.function.Predicate;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Clears the address book.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String UNDO_SUCCESS = "Undo successful: Address book has been restored.";
    public static final String REDO_SUCCESS = "Redo successful: Address book has been cleared.";

    private ArrayList<Person> previousPersonList;
    private Predicate<? super Person> previousPredicate;

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        previousPersonList = new ArrayList<>(model.getAddressBook().getPersonList());
        previousPredicate = model.getFilteredPersonPredicate();
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public CommandResult undo(Model model) {
        for (Person person : previousPersonList) {
            model.addPerson(person);
        }
        model.setFilteredPersonPredicate(previousPredicate);
        return new CommandResult(UNDO_SUCCESS);
    }

    @Override
    public CommandResult redo(Model model) {
        model.setAddressBook(new AddressBook());
        return new CommandResult(REDO_SUCCESS);
    }
}
