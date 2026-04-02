package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds multiple people to the address book from a parsed CSV file.
 */
public class AddByCsvCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addcsv";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds multiple people to the address book from a CSV file.\n"
            + "Parameters: FILE_PATH (must end with .csv)\n"
            + "The CSV file should have columns: name, phone, email, address, postalCode, [tags]\n"
            + "Example: " + COMMAND_WORD + " data/candidates.csv";

    public static final String MESSAGE_SUCCESS = "%1$d person(s) added from CSV file.";
    public static final String MESSAGE_DUPLICATE_PERSON =
            "%1$s already exists in the address book.";

    private final List<Person> personsToAdd;

    /**
     * Creates an AddByCsvCommand to add the specified list of {@code Person}.
     */
    public AddByCsvCommand(List<Person> personsToAdd) {
        requireNonNull(personsToAdd);
        this.personsToAdd = personsToAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        for (Person person : personsToAdd) {
            if (model.hasPerson(person)) {
                throw new CommandException(
                        String.format(MESSAGE_DUPLICATE_PERSON, Messages.format(person)));
            }
        }

        for (Person person : personsToAdd) {
            model.addPerson(person);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, personsToAdd.size()));
    }

    @Override
    public void undo(Model model) {
        for (Person p: personsToAdd) {
            model.deletePerson(p);
        }
    }

    @Override
    public void redo(Model model) {
        for (Person p: personsToAdd) {
            model.addPerson(p);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddByCsvCommand)) {
            return false;
        }

        AddByCsvCommand otherCommand = (AddByCsvCommand) other;
        return personsToAdd.equals(otherCommand.personsToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personsToAdd", personsToAdd)
                .toString();
    }
}
