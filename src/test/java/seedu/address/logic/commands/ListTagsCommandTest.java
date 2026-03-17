package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ListTagsCommand.MESSAGE_PREFIX;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.LinkedHashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagCounter;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.TagCountsContent;

public class ListTagsCommandTest {
    private Model model;
    private Model expectedModel;
    private String expectedMessage = MESSAGE_PREFIX + "{[friends]=3, [owesMoney]=1}";

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_successful() {
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 3);
        tagMap.put(new Tag("owesMoney"), 1);
        assertCommandSuccess(new ListTagsCommand(), model, expectedMessage, expectedModel,
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new TagCountsContent(new TagCounter(tagMap))));
    }
}
