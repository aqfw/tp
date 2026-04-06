package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AddCommand.RIGHT_PANE_HEADER;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.PersonContent;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddCommand(validPerson), model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)), expectedModel,
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(validPerson, RIGHT_PANE_HEADER)));
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);

        Person duplicatePhoneNumber = new PersonBuilder().withName("Alice Pauline")
                .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@different.com")
                .withPhone("94351253")
                .withTags("friends").build();
        assertCommandFailure(new AddCommand(duplicatePhoneNumber), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);

        Person duplicateEmailAddress = new PersonBuilder().withName("Alice Pauline")
                .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
                .withPhone("12345678")
                .withTags("friends").build();
        assertCommandFailure(new AddCommand(duplicateEmailAddress), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void undo_addCommand_success() throws CommandException {
        Person validPerson = new PersonBuilder().build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        AddCommand addCommand = new AddCommand(validPerson);
        addCommand.execute(model);
        model.recordCommand(addCommand);

        model.undo();
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_addCommand_success() throws CommandException {
        Person validPerson = new PersonBuilder().build();

        AddCommand addCommand = new AddCommand(validPerson);
        addCommand.execute(model);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        model.recordCommand(addCommand);

        model.undo();
        model.redo();
        assertTrue(model.hasPerson(validPerson));
        assertEquals(expectedModel, model);
    }
}
