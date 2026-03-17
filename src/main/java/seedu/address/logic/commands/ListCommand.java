package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.address.model.Model;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.TagCountsContent;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.resetFilteredPersonList();
        return new CommandResult(MESSAGE_SUCCESS, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(model.getTagCounter())));
    }
}
