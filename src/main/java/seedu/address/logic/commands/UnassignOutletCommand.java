package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.PersonContent;

/**
 * Unassigns a candidate from their current outlet.
 */
public class UnassignOutletCommand extends Command {

    public static final String COMMAND_WORD = "unassign";

    public static final String MESSAGE_USAGE = "outlet " + COMMAND_WORD + ": Unassigns a candidate "
            + "from their working outlet.\n"
            + "Parameters: CANDIDATE_INDEX (must be a positive integer)\n"
            + "Example: outlet " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Unassigned %1$s from working outlet";
    private final Index candidateIndex;

    /**
     * Creates an UnassignOutletCommand to unassign the specified candidate.
     */
    public UnassignOutletCommand(Index candidateIndex) {
        requireNonNull(candidateIndex);
        this.candidateIndex = candidateIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownPersons = model.getFilteredPersonList();
        if (candidateIndex.getZeroBased() >= lastShownPersons.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUnassign = lastShownPersons.get(candidateIndex.getZeroBased());
        Person unassignedPerson = new Person(personToUnassign.getName(), personToUnassign.getPhone(),
                personToUnassign.getEmail(), personToUnassign.getAddress(), personToUnassign.getPostalCode(),
                personToUnassign.getTags(), null);

        try {
            model.setPerson(personToUnassign, unassignedPerson);
        } catch (DuplicatePersonException e) {
            throw new CommandException(e.getMessage());
        }

        model.resetFilteredPersonList();
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToUnassign.getName()),
                UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new PersonContent(unassignedPerson, "Candidate #" + candidateIndex.getOneBased())));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnassignOutletCommand otherUnassignOutletCommand)) {
            return false;
        }

        return candidateIndex.equals(otherUnassignOutletCommand.candidateIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("candidateIndex", candidateIndex)
                .toString();
    }
}
