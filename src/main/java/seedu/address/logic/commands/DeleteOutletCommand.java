package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.outlet.Outlet;

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

    private final Index targetIndex;
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

        outletToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteOutlet(outletToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_OUTLET_SUCCESS, Messages.format(outletToDelete)));
    }

    @Override
    public void undo(Model model) {
        model.addOutlet(outletToDelete);
    }

    @Override
    public void redo(Model model) {
        model.deleteOutlet(outletToDelete);
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
