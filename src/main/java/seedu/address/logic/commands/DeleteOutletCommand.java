package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.PersonContent;

/**
 * Deletes an outlet identified using its displayed index from the address book.
 */
public class DeleteOutletCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = "outlet " + COMMAND_WORD
            + ": Deletes the outlet identified by the index number used in the displayed outlet list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: outlet " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_OUTLET_SUCCESS = "Deleted outlet: %1$s";
    public static final String UNDO_SUCCESS = "Undo successful: Added outlet %1$s";
    public static final String REDO_SUCCESS = "Redo successful: Deleted outlet %1$s";
    public static final String RIGHT_PANE_HEADER = "CANDIDATE UPDATED";

    private final Index targetIndex;
    private final List<Person> assignedPersonsBeforeDelete = new ArrayList<>();
    private final List<Person> unassignedPersonsAfterDelete = new ArrayList<>();
    private Outlet outletToDelete;

    public DeleteOutletCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Outlet> lastShownList = model.getFilteredOutletList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_OUTLET_DISPLAYED_INDEX);
        }

        assignedPersonsBeforeDelete.clear();
        unassignedPersonsAfterDelete.clear();

        outletToDelete = lastShownList.get(targetIndex.getZeroBased());
        unassignPersonsFromDeletedOutlet(model, outletToDelete);
        model.deleteOutlet(outletToDelete);
        if (!unassignedPersonsAfterDelete.isEmpty()) {
            Person updatedPerson = unassignedPersonsAfterDelete.get(0);
            return new CommandResult(
                    String.format(MESSAGE_DELETE_OUTLET_SUCCESS, Messages.format(outletToDelete)),
                    UiAction.UPDATE_RIGHT_PANE,
                    Optional.of(new PersonContent(updatedPerson, RIGHT_PANE_HEADER)));
        }
        return new CommandResult(String.format(MESSAGE_DELETE_OUTLET_SUCCESS, Messages.format(outletToDelete)));
    }

    @Override
    public CommandResult undo(Model model) {
        model.addOutletAtIndex(outletToDelete, targetIndex);
        for (int idx = 0; idx < assignedPersonsBeforeDelete.size(); idx++) {
            model.setPerson(unassignedPersonsAfterDelete.get(idx), assignedPersonsBeforeDelete.get(idx));
        }
        return new CommandResult(String.format(UNDO_SUCCESS, Messages.format(outletToDelete)));
    }

    @Override
    public CommandResult redo(Model model) {
        for (int idx = 0; idx < assignedPersonsBeforeDelete.size(); idx++) {
            model.setPerson(assignedPersonsBeforeDelete.get(idx), unassignedPersonsAfterDelete.get(idx));
        }
        model.deleteOutlet(outletToDelete);
        return new CommandResult(String.format(REDO_SUCCESS, Messages.format(outletToDelete)));
    }

    private void unassignPersonsFromDeletedOutlet(Model model, Outlet outlet) throws CommandException {
        for (Person person : model.getAddressBook().getPersonList()) {
            if (!outlet.equals(person.getWorkingAddress())) {
                continue;
            }

            Person unassignedPerson = new Person(person.getName(), person.getPhone(), person.getEmail(),
                    person.getAddress(), person.getPostalCode(), person.getTags(), null);
            assignedPersonsBeforeDelete.add(person);
            unassignedPersonsAfterDelete.add(unassignedPerson);
        }

        for (int idx = 0; idx < assignedPersonsBeforeDelete.size(); idx++) {
            try {
                model.setPerson(assignedPersonsBeforeDelete.get(idx), unassignedPersonsAfterDelete.get(idx));
            } catch (DuplicatePersonException e) {
                throw new CommandException(e.getMessage());
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteOutletCommand otherDeleteOutletCommand)) {
            return false;
        }

        return targetIndex.equals(otherDeleteOutletCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
