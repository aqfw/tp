package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.person.Person;
import seedu.address.testutil.OutletBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.PersonContent;

/**
 * Contains integration tests for {@code UnassignOutletCommand}.
 */
public class UnassignOutletCommandTest {

    private Model model;
    private Outlet outlet;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        outlet = new OutletBuilder().build();
        model.addOutlet(outlet);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Person assignedPerson = new PersonBuilder(firstPerson).withWorkingAddress(outlet).build();
        model.setPerson(firstPerson, assignedPerson);
    }

    @Test
    public void execute_validIndex_success() {
        Person personToUnassign = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Person unassignedPerson = new PersonBuilder(personToUnassign).withWorkingAddress(null).build();

        UnassignOutletCommand unassignOutletCommand = new UnassignOutletCommand(INDEX_FIRST_ENTRY);
        String expectedMessage = String.format(UnassignOutletCommand.MESSAGE_SUCCESS, personToUnassign.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToUnassign, unassignedPerson);

        assertCommandSuccess(unassignOutletCommand, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new PersonContent(unassignedPerson, "Candidate #" + INDEX_FIRST_ENTRY.getOneBased())));
    }

    @Test
    public void execute_invalidPersonIndex_failure() {
        Index outOfBoundPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnassignOutletCommand unassignOutletCommand = new UnassignOutletCommand(outOfBoundPersonIndex);

        assertCommandFailure(unassignOutletCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void undo_unassignOutletCommand_success() throws CommandException {
        Person personToUnassign = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());

        UnassignOutletCommand unassignOutletCommand = new UnassignOutletCommand(INDEX_FIRST_ENTRY);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        unassignOutletCommand.execute(model);
        model.recordCommand(unassignOutletCommand);

        model.undo();
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_unassignOutletCommand_success() throws CommandException {
        Person personToUnassign = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Person unassignedPerson = new PersonBuilder(personToUnassign).withWorkingAddress(null).build();

        UnassignOutletCommand unassignOutletCommand = new UnassignOutletCommand(INDEX_FIRST_ENTRY);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        unassignOutletCommand.execute(model);
        expectedModel.setPerson(personToUnassign, unassignedPerson);
        model.recordCommand(unassignOutletCommand);

        model.undo();
        model.redo();
        assertEquals(expectedModel, model);
    }

    @Test
    public void equals() {
        UnassignOutletCommand unassignFirstCommand = new UnassignOutletCommand(INDEX_FIRST_ENTRY);
        UnassignOutletCommand unassignSecondCommand = new UnassignOutletCommand(INDEX_SECOND_ENTRY);

        assertTrue(unassignFirstCommand.equals(unassignFirstCommand));
        assertTrue(unassignFirstCommand.equals(new UnassignOutletCommand(INDEX_FIRST_ENTRY)));
        assertFalse(unassignFirstCommand.equals(1));
        assertFalse(unassignFirstCommand.equals(null));
        assertFalse(unassignFirstCommand.equals(unassignSecondCommand));
    }

    @Test
    public void toStringMethod() {
        UnassignOutletCommand unassignOutletCommand = new UnassignOutletCommand(INDEX_FIRST_ENTRY);
        String expected = UnassignOutletCommand.class.getCanonicalName() + "{candidateIndex=" + INDEX_FIRST_ENTRY
                + "}";
        assertEquals(expected, unassignOutletCommand.toString());
    }
}
