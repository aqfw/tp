package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Clears the address book.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    private ArrayList<Person> previousPersonList;

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        previousPersonList = new ArrayList<>(model.getAddressBook().getPersonList());
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public void undo(Model model) {
        for (Person person : previousPersonList) {
            model.addPerson(person);
        }
    }

    @Override
    public void redo(Model model) {
        model.setAddressBook(new AddressBook());
    }
}
