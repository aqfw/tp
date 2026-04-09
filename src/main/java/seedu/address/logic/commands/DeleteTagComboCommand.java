package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.TagCombo;
import seedu.address.ui.UiAction;

/**
 * Deletes a {@code TagCombo} identified using it's displayed index from the address book.
 */
public class DeleteTagComboCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "deletetagcombo";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the tag combo identified by the index number used in the displayed tag combo list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TAG_COMBO_SUCCESS = "Deleted Tag Combo: %1$s";

    private final Index targetIndex;
    private TagCombo deletedTagCombo;

    public DeleteTagComboCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<TagCombo> tagComboList = model.getTagComboList();

        if (targetIndex.getZeroBased() >= tagComboList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TAG_COMBO_DISPLAYED_INDEX);
        }

        deletedTagCombo = tagComboList.get(targetIndex.getZeroBased());
        model.deleteTagCombo(deletedTagCombo);
        return new CommandResult(String.format(MESSAGE_DELETE_TAG_COMBO_SUCCESS, Messages.format(deletedTagCombo)),
                UiAction.SHOW_TAG_COMBO);
    }

    @Override
    public void undo(Model model) {
        model.addTagComboAtIndex(deletedTagCombo, targetIndex);
    }

    @Override
    public void redo(Model model) {
        model.deleteTagCombo(deletedTagCombo);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteTagComboCommand)) {
            return false;
        }

        DeleteTagComboCommand otherDeleteTagComboCommand = (DeleteTagComboCommand) other;
        return targetIndex.equals(otherDeleteTagComboCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
