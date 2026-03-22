package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.ui.UiAction;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@codeListTagCombosCommand}.
 */
public class ListTagCombosCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_successful() {
        assertCommandSuccess(new ListTagCombosCommand(), model, ListTagCombosCommand.MESSAGE_SUCCESS, expectedModel,
                UiAction.SHOW_TAG_COMBO, Optional.empty());
    }
}
