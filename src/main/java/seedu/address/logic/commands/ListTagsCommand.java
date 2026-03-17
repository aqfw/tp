package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.address.model.Model;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.TagCountsContent;

/**
 * Lists all tags present in the address book to the user.
 */
public class ListTagsCommand extends Command {
    public static final String COMMAND_WORD = "listtags";

    public static final String MESSAGE_PREFIX = "The present tags in decreasing order are: \n";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return new CommandResult(MESSAGE_PREFIX + model.getTagCounter().displayDescendingOrder().toString(),
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new TagCountsContent(model.getTagCounter())));
    }
}
