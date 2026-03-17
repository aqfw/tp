package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
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
import seedu.address.model.person.PersonContainsTagsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagCounter;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.TagCountsContent;

public class FilterCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        Set<Tag> firstPredicateTagSet = Set.of(new Tag("Java"), new Tag("Python"));
        Set<Tag> firstPredicateTagSetLower = Set.of(new Tag("java"), new Tag("python"));
        Set<Tag> secondPredicateTagSet = Set.of(new Tag("Java"), new Tag("Python"), new Tag("C"));

        PersonContainsTagsPredicate firstPredicate = new PersonContainsTagsPredicate(firstPredicateTagSet);
        PersonContainsTagsPredicate firstPredicateLower = new PersonContainsTagsPredicate(firstPredicateTagSetLower);
        PersonContainsTagsPredicate secondPredicate = new PersonContainsTagsPredicate(secondPredicateTagSet);

        FilterCommand firstFilterCommand = new FilterCommand(firstPredicate);
        FilterCommand secondFilterCommand = new FilterCommand(secondPredicate);

        // same object -> returns true
        assertTrue(firstFilterCommand.equals(firstFilterCommand));

        // same values -> returns true
        FilterCommand firstFilterCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(firstFilterCommand.equals(firstFilterCommandCopy));

        // different types -> returns false
        assertFalse(firstFilterCommand.equals(1));

        // null -> returns false
        assertFalse(firstFilterCommand.equals(null));

        // different person -> returns false
        assertFalse(firstFilterCommand.equals(secondFilterCommand));

        // ignore case -> return true
        assertTrue(firstPredicate.equals(firstPredicate));
    }

    @Test
    public void execute_oneTag_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(Set.of(new Tag("enemies")));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_oneTag_peopleFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(Set.of(new Tag("friends")));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 3);
        tagMap.put(new Tag("owesMoney"), 1);
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void execute_twoTags_peopleFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(Set.of(new Tag("friends"),
                new Tag("owesMoney")));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 1);
        tagMap.put(new Tag("owesMoney"), 1);
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void updateFilteredPersonList_multipleTagFilters_success() {
        model.resetFilteredPersonList();

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonContainsTagsPredicate firstPredicate = new PersonContainsTagsPredicate(
                                                        Set.of(new Tag("friends")));
        FilterCommand command = new FilterCommand(firstPredicate);
        expectedModel.updateFilteredPersonList(firstPredicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 3);
        tagMap.put(new Tag("owesMoney"), 1);
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));

        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonContainsTagsPredicate secondPredicate = new PersonContainsTagsPredicate(
                                                        Set.of(new Tag("owesMoney")));
        FilterCommand command2 = new FilterCommand(secondPredicate);
        expectedModel.updateFilteredPersonList(secondPredicate);
        tagMap.put(new Tag("friends"), 1);
        assertCommandSuccess(command2, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
    }

    @Test
    public void toStringMethod() {
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(Set.of(new Tag("friends"),
                new Tag("owesMoney")));
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }

    @Test
    public void execute_twoTags_upperCase() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(Set.of(new Tag("FRIENDS"),
                new Tag("OWESmoNEy")));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 1);
        tagMap.put(new Tag("owesMoney"), 1);
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }
}
