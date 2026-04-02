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
import static seedu.address.testutil.TypicalTagCombos.FRIEND_OWES_MONEY;
import static seedu.address.testutil.TypicalTagCombos.ML_DEV;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonContainsTagsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagComboName;
import seedu.address.model.tag.TagCounter;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.TagCountsContent;

public class FilterCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        Set<Tag> firstPredicateTagSet = Set.of(new Tag("java"), new Tag("python"));
        Set<Tag> secondPredicateTagSet = Set.of(new Tag("java"), new Tag("python"), new Tag("C"));
        Set<TagComboName> firstTagComboNameSet = Set.of(ML_DEV.getName());

        FilterCommand firstFilterCommand = new FilterCommand(firstPredicateTagSet, Set.of());
        FilterCommand secondFilterCommand = new FilterCommand(secondPredicateTagSet, Set.of());
        FilterCommand thirdFilterCommand = new FilterCommand(firstPredicateTagSet, firstTagComboNameSet);

        // same object -> returns true
        assertTrue(firstFilterCommand.equals(firstFilterCommand));

        // same values -> returns true
        FilterCommand firstFilterCommandCopy = new FilterCommand(firstPredicateTagSet, Set.of());
        assertTrue(firstFilterCommand.equals(firstFilterCommandCopy));

        // different types -> returns false
        assertFalse(firstFilterCommand.equals(1));

        // null -> returns false
        assertFalse(firstFilterCommand.equals(null));

        // different person -> returns false
        assertFalse(firstFilterCommand.equals(secondFilterCommand));

        assertFalse(firstFilterCommand.equals(thirdFilterCommand));


    }

    @Test
    public void execute_oneTag_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        Set<Tag> tagSet = (Set.of(new Tag("enemies")));
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(tagSet);
        FilterCommand command = new FilterCommand(tagSet, Set.of());
        expectedModel.updateFilteredPersonList(predicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_oneTag_peopleFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Set<Tag> tagSet = Set.of(new Tag("friends"));
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(tagSet);
        FilterCommand command = new FilterCommand(tagSet, Set.of());
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
        Set<Tag> tagSet = Set.of(new Tag("friends"), new Tag("owesMoney"));
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(tagSet);
        FilterCommand command = new FilterCommand(tagSet, Set.of());
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
        Set<Tag> firstPredicateTagSet = Set.of(new Tag("friends"));
        PersonContainsTagsPredicate firstPredicate = new PersonContainsTagsPredicate(firstPredicateTagSet);
        FilterCommand command = new FilterCommand(firstPredicateTagSet, Set.of());
        expectedModel.updateFilteredPersonList(firstPredicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 3);
        tagMap.put(new Tag("owesMoney"), 1);
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));

        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Set<Tag> secondPredicateTagSet = Set.of(new Tag("owesMoney"));
        PersonContainsTagsPredicate secondPredicate = new PersonContainsTagsPredicate(secondPredicateTagSet);
        FilterCommand command2 = new FilterCommand(secondPredicateTagSet, Set.of());
        expectedModel.updateFilteredPersonList(secondPredicate);
        tagMap.put(new Tag("friends"), 1);
        assertCommandSuccess(command2, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
    }

    @Test
    public void execute_twoTags_upperCase() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Set<Tag> tagSet = Set.of(new Tag("FRIENDS"), new Tag("OWESmoNEy"));
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(tagSet);
        FilterCommand command = new FilterCommand(tagSet, Set.of());
        expectedModel.updateFilteredPersonList(predicate);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 1);
        tagMap.put(new Tag("owesMoney"), 1);
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_validTagCombo_success() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Set<Tag> tagSet = FRIEND_OWES_MONEY.getTagSet();
        FilterCommand command = new FilterCommand(new HashSet<>(), Set.of(FRIEND_OWES_MONEY.getName()));
        model.addTagCombo(FRIEND_OWES_MONEY);
        expectedModel.addTagCombo(FRIEND_OWES_MONEY);
        expectedModel.updateFilteredPersonList(new PersonContainsTagsPredicate(tagSet));
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 1);
        tagMap.put(new Tag("owesMoney"), 1);
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(tagMap))));
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_validTagComboValidTags_success() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(new Tag("python"));
        FilterCommand command = new FilterCommand(tagSet, Set.of(FRIEND_OWES_MONEY.getName()));
        model.addTagCombo(FRIEND_OWES_MONEY);
        expectedModel.addTagCombo(FRIEND_OWES_MONEY);
        Set<Tag> tagSetAll = Set.of(new Tag("python"), new Tag("friends"), new Tag("owesMoney"));
        expectedModel.updateFilteredPersonList(new PersonContainsTagsPredicate(tagSetAll));
        assertCommandSuccess(command, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new TagCountsContent(new TagCounter(new LinkedHashMap<>()))));
        assertEquals(Arrays.asList(), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        Set<Tag> tagSet = Set.of(new Tag("friends"), new Tag("owesMoney"));
        Set<TagComboName> tagComboNameSet = Set.of(ML_DEV.getName());
        FilterCommand filterCommand = new FilterCommand(tagSet, tagComboNameSet);
        String expected = FilterCommand.class.getCanonicalName() + "{tagList=" + tagSet.toString()
                + ", tagComboNameList=" + tagComboNameSet.toString() + "}";
        assertEquals(expected, filterCommand.toString());
    }
}
