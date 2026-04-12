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
    public static final String MESSAGE_CSV_LIMIT_EXCEEDED =
            "Cannot add %1$d candidate(s) from CSV. You can add at most %2$d candidate(s) now.";
    public static final String UNDO_SUCCESS = "Undo successful: Removed %1$d people.";
    public static final String REDO_SUCCESS = "Redo successful: Added %1$d people.";
    private static final int MAX_CSV_BATCH_SIZE = 25;
    private static final int MAX_TOTAL_CANDIDATE_CAPACITY = 999;

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

        for (int i = 0; i < personsToAdd.size() - 1; i++) {
            for (int j = i + 1; j < personsToAdd.size(); j++) {
                if (personsToAdd.get(i).isSamePerson(personsToAdd.get(j))) {
                    throw new CommandException(
                            String.format(MESSAGE_DUPLICATE_PERSON, Messages.format(personsToAdd.get(j))));
                }
            }
        }

        for (Person person : personsToAdd) {
            if (model.hasPerson(person)) {
                throw new CommandException(
                        String.format(MESSAGE_DUPLICATE_PERSON, Messages.format(person)));
            }
        }

        int currentTotalCandidates = model.getAddressBook().getPersonList().size();
        int remainingCapacity = MAX_TOTAL_CANDIDATE_CAPACITY - currentTotalCandidates;
        int maxAddableNow = Math.max(0, Math.min(MAX_CSV_BATCH_SIZE, remainingCapacity));
        if (personsToAdd.size() > maxAddableNow) {
            throw new CommandException(String.format(MESSAGE_CSV_LIMIT_EXCEEDED, personsToAdd.size(), maxAddableNow));
        }

        for (Person person : personsToAdd) {
            model.addPerson(person);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, personsToAdd.size()));
    }

    /**
     * Removes all previously added persons from the model.
     */
    @Override
    public CommandResult undo(Model model) {
        for (Person p: personsToAdd) {
            model.deletePerson(p);
        }
        return new CommandResult(String.format(UNDO_SUCCESS, personsToAdd.size()));
    }

    /**
     * Adds all previously removed persons back into the model.
     */
    @Override
    public CommandResult redo(Model model) {
        for (Person p: personsToAdd) {
            model.addPerson(p);
        }
        return new CommandResult(String.format(REDO_SUCCESS, personsToAdd.size()));
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
