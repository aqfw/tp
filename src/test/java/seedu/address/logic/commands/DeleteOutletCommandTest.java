package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showOutletAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
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
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteOutletCommand}.
 */
public class DeleteOutletCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addOutlet(new OutletBuilder().build());
        model.addOutlet(new OutletBuilder().withName("FinServ").withAddress("Marina Bay").withPostalCode("018956")
                .build());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Outlet outletToDelete = model.getFilteredOutletList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteOutletCommand deleteOutletCommand = new DeleteOutletCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(DeleteOutletCommand.MESSAGE_DELETE_OUTLET_SUCCESS,
                Messages.format(outletToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteOutlet(outletToDelete);

        assertCommandSuccess(deleteOutletCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_outletInUse_unassignsAffectedCandidate() {
        Outlet outletToDelete = model.getFilteredOutletList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Person personToAssign = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Person assignedPerson = new PersonBuilder(personToAssign).withWorkingAddress(outletToDelete).build();
        Person unassignedPerson = new PersonBuilder(assignedPerson).withWorkingAddress(null).build();
        model.setPerson(personToAssign, assignedPerson);

        DeleteOutletCommand deleteOutletCommand = new DeleteOutletCommand(INDEX_FIRST_ENTRY);
        String expectedMessage = String.format(DeleteOutletCommand.MESSAGE_DELETE_OUTLET_SUCCESS,
                Messages.format(outletToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(assignedPerson, unassignedPerson);
        expectedModel.deleteOutlet(outletToDelete);

        assertCommandSuccess(deleteOutletCommand, model, expectedMessage, expectedModel, UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new PersonContent(unassignedPerson, DeleteOutletCommand.RIGHT_PANE_HEADER)));
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredOutletList().size() + 1);
        DeleteOutletCommand deleteOutletCommand = new DeleteOutletCommand(outOfBoundIndex);

        assertCommandFailure(deleteOutletCommand, model, Messages.MESSAGE_INVALID_OUTLET_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showOutletAtIndex(model, INDEX_FIRST_ENTRY);

        Outlet outletToDelete = model.getFilteredOutletList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteOutletCommand deleteOutletCommand = new DeleteOutletCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(DeleteOutletCommand.MESSAGE_DELETE_OUTLET_SUCCESS,
                Messages.format(outletToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteOutlet(outletToDelete);
        expectedModel.updateFilteredOutletList(outlet -> false);

        assertCommandSuccess(deleteOutletCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showOutletAtIndex(model, INDEX_FIRST_ENTRY);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getOutletList().size());

        DeleteOutletCommand deleteOutletCommand = new DeleteOutletCommand(outOfBoundIndex);

        assertCommandFailure(deleteOutletCommand, model, Messages.MESSAGE_INVALID_OUTLET_DISPLAYED_INDEX);
    }


    @Test
    public void undo_deleteOutletCommand_success() throws CommandException {
        Outlet outletToDelete = model.getFilteredOutletList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteOutletCommand deleteOutletCommand = new DeleteOutletCommand(INDEX_FIRST_ENTRY);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        deleteOutletCommand.execute(model);
        assertFalse(model.hasOutlet(outletToDelete));
        assertTrue(expectedModel.hasOutlet(outletToDelete));
        model.recordCommand(deleteOutletCommand);

        model.undo();
        assertTrue(model.hasOutlet(outletToDelete));
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_deleteOutletCommand_success() throws CommandException {
        Outlet outletToDelete = model.getFilteredOutletList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteOutletCommand deleteOutletCommand = new DeleteOutletCommand(INDEX_FIRST_ENTRY);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        deleteOutletCommand.execute(model);
        expectedModel.deleteOutlet(outletToDelete);
        model.recordCommand(deleteOutletCommand);

        model.undo();
        model.redo();
        assertEquals(expectedModel, model);
    }

    @Test
    public void equals() {
        DeleteOutletCommand deleteFirstCommand = new DeleteOutletCommand(INDEX_FIRST_ENTRY);
        DeleteOutletCommand deleteSecondCommand = new DeleteOutletCommand(INDEX_SECOND_ENTRY);

        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));
        assertTrue(deleteFirstCommand.equals(new DeleteOutletCommand(INDEX_FIRST_ENTRY)));
        assertFalse(deleteFirstCommand.equals(1));
        assertFalse(deleteFirstCommand.equals(null));
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteOutletCommand deleteOutletCommand = new DeleteOutletCommand(targetIndex);
        String expected = DeleteOutletCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteOutletCommand.toString());
    }
}
