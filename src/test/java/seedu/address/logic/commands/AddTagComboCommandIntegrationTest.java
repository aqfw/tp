package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.TagCombo;
import seedu.address.testutil.TagComboBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddTagComboCommand}.
 */
public class AddTagComboCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void undo_addTagComboCommand_success() throws CommandException {
        TagCombo validTagCombo = new TagComboBuilder().build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        AddTagComboCommand addTagComboCommand = new AddTagComboCommand(validTagCombo);
        addTagComboCommand.execute(model);
        model.recordCommand(addTagComboCommand);

        model.undo();
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_addTagComboCommand_success() throws CommandException {
        TagCombo validTagCombo = new TagComboBuilder().build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        AddTagComboCommand addTagComboCommand = new AddTagComboCommand(validTagCombo);
        addTagComboCommand.execute(model);
        expectedModel.addTagCombo(validTagCombo);
        model.recordCommand(addTagComboCommand);

        model.undo();
        model.redo();
        assertEquals(expectedModel, model);
    }
}
