package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddByCsvCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void undo_addByCsvCommand_success() throws CommandException {
        Person validPerson = new PersonBuilder().build();
        List<Person> validCsv = List.of(validPerson);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        AddByCsvCommand addByCsvCommand = new AddByCsvCommand(validCsv);
        addByCsvCommand.execute(model);
        model.recordCommand(addByCsvCommand);

        model.undo();
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_addByCsvCommand_success() throws CommandException {
        Person validPerson = new PersonBuilder().build();
        List<Person> validCsv = List.of(validPerson);

        AddByCsvCommand addByCsvCommand = new AddByCsvCommand(validCsv);
        addByCsvCommand.execute(model);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        model.recordCommand(addByCsvCommand);

        model.undo();
        model.redo();
        assertTrue(model.hasPerson(validPerson));
        assertEquals(expectedModel, model);
    }
}
