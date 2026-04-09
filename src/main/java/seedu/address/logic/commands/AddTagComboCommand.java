package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.TagCombo;
import seedu.address.model.tag.exceptions.DuplicateTagComboException;
import seedu.address.ui.UiAction;

/**
 * Adds a {@code TagCombo} to the {@code AddressBook}.
 */
public class AddTagComboCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "addtagcombo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a Tag Combo.\n"
            + "Parameters: "
            + PREFIX_TAG + "TAG "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " developer "
            + PREFIX_TAG + "python "
            + PREFIX_TAG + "java";

    public static final String MESSAGE_SUCCESS = "New Tag Combo added: %1$s";
    public static final String MESSAGE_DUPLICATE_TAG_COMBO = "A Tag Combo with this name or tag set is "
            + "already in the address book.";

    private final TagCombo toAdd;

    /**
     * Creates an AddTagComboCommand to add the specified {@code TagCombo}.
     */
    public AddTagComboCommand(TagCombo tagCombo) {
        requireNonNull(tagCombo);
        toAdd = tagCombo;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            model.addTagCombo(toAdd);
        } catch (DuplicateTagComboException e) {
            throw new CommandException(e.getMessage());
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)), UiAction.SHOW_TAG_COMBO);
    }

    @Override
    public void undo(Model model) {
        model.deleteTagCombo(toAdd);
    }

    @Override
    public void redo(Model model) {
        model.addTagCombo(toAdd);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddTagComboCommand otherAddTagComboCommand)) {
            return false;
        }

        return toAdd.equals(otherAddTagComboCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
