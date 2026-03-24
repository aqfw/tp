package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Assigns an outlet to a candidate.
 */
public class AssignOutletCommand extends Command {

    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = "outlet " + COMMAND_WORD + ": Assigns a candidate to an outlet.\n"
            + "Parameters: CANDIDATE_INDEX OUTLET_INDEX (both must be positive integers)\n"
            + "Example: outlet " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_SUCCESS = "Assigned %1$s to outlet %2$s";

    private final Index candidateIndex;
    private final Index outletIndex;

    /**
     * Creates an AssignOutletCommand to assign an outlet to the specified candidate.
     */
    public AssignOutletCommand(Index candidateIndex, Index outletIndex) {
        requireNonNull(candidateIndex);
        requireNonNull(outletIndex);
        this.candidateIndex = candidateIndex;
        this.outletIndex = outletIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownPersons = model.getFilteredPersonList();
        if (candidateIndex.getZeroBased() >= lastShownPersons.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        List<Outlet> lastShownOutlets = model.getFilteredOutletList();
        if (outletIndex.getZeroBased() >= lastShownOutlets.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_OUTLET_DISPLAYED_INDEX);
        }

        Person personToAssign = lastShownPersons.get(candidateIndex.getZeroBased());
        Outlet outletToAssign = lastShownOutlets.get(outletIndex.getZeroBased());
        Person assignedPerson = new Person(personToAssign.getName(), personToAssign.getPhone(),
                personToAssign.getEmail(), personToAssign.getAddress(), personToAssign.getPostalCode(),
                personToAssign.getTags(), outletToAssign);

        try {
            model.setPerson(personToAssign, assignedPerson);
        } catch (DuplicatePersonException e) {
            throw new CommandException(e.getMessage());
        }

        model.resetFilteredPersonList();
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, personToAssign.getName(), outletToAssign.getOutletName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AssignOutletCommand otherAssignOutletCommand)) {
            return false;
        }

        return candidateIndex.equals(otherAssignOutletCommand.candidateIndex)
                && outletIndex.equals(otherAssignOutletCommand.outletIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("candidateIndex", candidateIndex)
                .add("outletIndex", outletIndex)
                .toString();
    }
}
