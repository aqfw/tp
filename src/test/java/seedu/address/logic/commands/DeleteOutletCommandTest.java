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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.outlet.Outlet;
import seedu.address.testutil.OutletBuilder;

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
