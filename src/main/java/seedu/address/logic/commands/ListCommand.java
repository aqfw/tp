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

    public static final String MESSAGE_SUCCESS = "Listed all persons.";
    public static final String UNDO_SUCCESS = "Undo successful: Returned to previous view.";
    public static final String REDO_SUCCESS = "Redo successful: Now viewing all candidates.";

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
    public CommandResult undo(Model model) {
        model.setFilteredPersonPredicate(previousPredicate);
        return new CommandResult(UNDO_SUCCESS, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(model.getTagCounter())));
    }

    @Override
    public CommandResult redo(Model model) {
        model.resetFilteredPersonList();
        return new CommandResult(REDO_SUCCESS, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(model.getTagCounter())));
    }
}
