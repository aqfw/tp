package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PersonContainsTagsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagCounter;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.TagCountsContent;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void updateFilteredPersonList_multipleNameFilters_success() {
        model.resetFilteredPersonList();

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate firstPredicate = preparePredicate("Kurz Elle Alice");
        FindCommand command = new FindCommand(firstPredicate);
        expectedModel.updateFilteredPersonList(firstPredicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 1);
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));

        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate secondPredicate = preparePredicate("Alice");
        FindCommand command2 = new FindCommand(secondPredicate);
        expectedModel.updateFilteredPersonList(secondPredicate);
        assertCommandSuccess(command2, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
    }

    @Test
    public void updateFilteredPersonList_tagAndNameFilters_success() {
        model.resetFilteredPersonList();

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate firstPredicate = preparePredicate("Kurz Elle Alice");
        FindCommand command = new FindCommand(firstPredicate);
        expectedModel.updateFilteredPersonList(firstPredicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 1);
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));

        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonContainsTagsPredicate secondPredicate = new PersonContainsTagsPredicate(
                Set.of(new Tag("friends")));
        FilterCommand command2 = new FilterCommand(secondPredicate);
        expectedModel.updateFilteredPersonList(secondPredicate);
        assertCommandSuccess(command2, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
