package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.TagCountsContent;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";

    private Predicate<? super Person> previousPredicate;

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        previousPredicate = model.getFilteredPersonPredicate();
        model.resetFilteredPersonList();
        return new CommandResult(MESSAGE_SUCCESS, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(model.getTagCounter())));
    }

    @Override
    public void undo(Model model) {
        model.setFilteredPersonPredicate(previousPredicate);
    }
}
