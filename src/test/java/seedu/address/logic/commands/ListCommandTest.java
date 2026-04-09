package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagCounter;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.TagCountsContent;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel,
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new TagCountsContent(model.getTagCounter())));
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_ENTRY);
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 3);
        tagMap.put(new Tag("owesMoney"), 1);
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel,
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new TagCountsContent(new TagCounter(tagMap))));
    }

    @Test
    public void undo_listCommand_success() throws CommandException {
        Set<Tag> tagSet = Set.of(new Tag("friends"));
        FilterCommand filterCommand = new FilterCommand(tagSet, Set.of());

        filterCommand.execute(model);
        ListCommand listCommand = new ListCommand();
        listCommand.execute(model);
        model.recordCommand(listCommand);
        model.undo();

        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void redo_listCommand_success() throws CommandException {
        Set<Tag> tagSet = Set.of(new Tag("friends"));
        FilterCommand filterCommand = new FilterCommand(tagSet, Set.of());

        filterCommand.execute(model);
        ListCommand listCommand = new ListCommand();
        model.recordCommand(listCommand);
        listCommand.execute(model);
        model.undo();
        model.redo();

        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

}
