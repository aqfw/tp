package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsTagsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagComboName;
import seedu.address.model.tag.exceptions.TagComboNotFoundException;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.TagCountsContent;

/**
 * Finds all candidates in the candidate book containing the tags specified
 */
public class FilterCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all candidates in the candidate book. "
            + "Parameters: [" + PREFIX_TAG + "TAG].../n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "python "
            + PREFIX_TAG + "java";

    public static final String MESSAGE_TAG_COMBO_NOT_FOUND = "The tag combo was not found in the system!";

    private final Set<Tag> tagList;
    private final Set<TagComboName> tagComboNameList;

    private Predicate<? super Person> previousPredicate;      // for undo
    private Predicate<? super Person> currentPredicate;

    /**
     * Constructs a FilterCommand with a tagList and tagComboNameList.
     */
    public FilterCommand(Set<Tag> tagList, Set<TagComboName> tagComboNameList) {
        this.tagList = tagList;
        this.tagComboNameList = tagComboNameList;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        assert !(tagList.isEmpty() && tagComboNameList.isEmpty());

        try {
            for (TagComboName tc : tagComboNameList) {
                tagList.addAll(model.getTagCombo(tc).getTagSet());
            }
        } catch (TagComboNotFoundException e) {
            return new CommandResult(MESSAGE_TAG_COMBO_NOT_FOUND);
        }

        assert !tagList.isEmpty();

        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(tagList);
        previousPredicate = model.getFilteredPersonPredicate();
        model.updateFilteredPersonList(predicate);
        currentPredicate = model.getFilteredPersonPredicate();
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()),
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new TagCountsContent(model.getTagCounter())));
    }

    @Override
    public void undo(Model model) {
        model.setFilteredPersonPredicate(previousPredicate);
    }

    @Override
    public void redo(Model model) {
        model.setFilteredPersonPredicate(currentPredicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFilterCommand = (FilterCommand) other;
        return tagList.equals(otherFilterCommand.tagList)
                && tagComboNameList.equals(otherFilterCommand.tagComboNameList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("tagList", tagList)
                .add("tagComboNameList", tagComboNameList)
                .toString();
    }
}
