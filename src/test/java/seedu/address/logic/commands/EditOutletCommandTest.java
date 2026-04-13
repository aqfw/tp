package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BETA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_ADDRESS_BETA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_NAME_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_NAME_BETA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_POSTAL_CODE_BETA;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditOutletCommand.EditOutletDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.outlet.Outlet;
import seedu.address.testutil.EditOutletDescriptorBuilder;
import seedu.address.testutil.OutletBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditOutletCommand.
 */
public class EditOutletCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addOutlet(new OutletBuilder().build());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Outlet editedOutlet = new OutletBuilder().build();
        EditOutletDescriptor descriptor = new EditOutletDescriptorBuilder(editedOutlet).build();
        EditOutletCommand editOutletCommand = new EditOutletCommand(INDEX_FIRST_ENTRY, descriptor);

        String expectedMessage = String.format(EditOutletCommand.MESSAGE_EDIT_OUTLET_SUCCESS,
                Messages.format(editedOutlet));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setOutlet(model.getFilteredOutletList().get(0), editedOutlet);

        assertCommandSuccess(editOutletCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastOutlet = Index.fromOneBased(model.getFilteredOutletList().size());
        Outlet lastOutlet = model.getFilteredOutletList().get(indexLastOutlet.getZeroBased());

        OutletBuilder outletInList = new OutletBuilder(lastOutlet);
        Outlet editedOutlet = outletInList.withName(VALID_OUTLET_NAME_ALPHA).build();

        EditOutletDescriptor descriptor = new EditOutletDescriptorBuilder().withName(VALID_OUTLET_NAME_ALPHA).build();
        EditOutletCommand editOutletCommand = new EditOutletCommand(indexLastOutlet, descriptor);

        String expectedMessage = String.format(EditOutletCommand.MESSAGE_EDIT_OUTLET_SUCCESS,
                Messages.format(editedOutlet));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setOutlet(lastOutlet, editedOutlet);

        assertCommandSuccess(editOutletCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditOutletCommand editOutletCommand = new EditOutletCommand(INDEX_FIRST_ENTRY, new EditOutletDescriptor());
        Outlet editedOutlet = model.getFilteredOutletList().get(INDEX_FIRST_ENTRY.getZeroBased());

        String expectedMessage = String.format(EditOutletCommand.MESSAGE_EDIT_OUTLET_SUCCESS,
                Messages.format(editedOutlet));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editOutletCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Outlet validOutlet = new OutletBuilder().withName(VALID_OUTLET_NAME_BETA)
                .withAddress(VALID_OUTLET_ADDRESS_BETA).withPostalCode(VALID_OUTLET_POSTAL_CODE_BETA).build();
        model.addOutlet(validOutlet);

        Outlet firstOutlet = model.getFilteredOutletList().get(INDEX_FIRST_ENTRY.getZeroBased());
        EditOutletDescriptor descriptor = new EditOutletDescriptorBuilder(firstOutlet).build();
        EditOutletCommand editOutletCommand = new EditOutletCommand(INDEX_SECOND_ENTRY, descriptor);

        assertCommandFailure(editOutletCommand, model, EditOutletCommand.MESSAGE_DUPLICATE_OUTLET);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredOutletList().size() + 1);
        EditOutletDescriptor descriptor = new EditOutletDescriptorBuilder().withName(VALID_OUTLET_NAME_ALPHA).build();
        EditOutletCommand editOutletCommand = new EditOutletCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editOutletCommand, model, Messages.MESSAGE_INVALID_OUTLET_DISPLAYED_INDEX);
    }

    @Test
    public void undo_editCommand_success() throws CommandException {
        Outlet outletToEdit = model.getFilteredOutletList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Outlet editedOutlet = new OutletBuilder().withName(VALID_OUTLET_NAME_BETA)
                .withAddress(VALID_OUTLET_ADDRESS_BETA).withPostalCode(VALID_OUTLET_POSTAL_CODE_BETA).build();
        EditOutletDescriptor descriptor = new EditOutletDescriptorBuilder(editedOutlet).build();
        EditOutletCommand editOutletCommand = new EditOutletCommand(INDEX_FIRST_ENTRY, descriptor);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        editOutletCommand.execute(model);
        assertFalse(model.hasOutlet(outletToEdit));
        assertTrue(model.hasOutlet(editedOutlet));
        model.recordCommand(editOutletCommand);

        model.undo();
        assertTrue(model.hasOutlet(outletToEdit));
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_editCommand_success() throws CommandException {
        Outlet outletToEdit = model.getFilteredOutletList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Outlet editedOutlet = new OutletBuilder().withName(VALID_OUTLET_NAME_BETA)
                .withAddress(VALID_OUTLET_ADDRESS_BETA).withPostalCode(VALID_OUTLET_POSTAL_CODE_BETA).build();
        EditOutletDescriptor descriptor = new EditOutletDescriptorBuilder(editedOutlet).build();
        EditOutletCommand editOutletCommand = new EditOutletCommand(INDEX_FIRST_ENTRY, descriptor);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        editOutletCommand.execute(model);
        expectedModel.setOutlet(outletToEdit, editedOutlet);
        model.recordCommand(editOutletCommand);

        model.undo();
        model.redo();
        assertEquals(expectedModel, model);
    }

    @Test
    public void equals() {
        final EditOutletCommand standardCommand = new EditOutletCommand(INDEX_FIRST_ENTRY, DESC_BETA);

        // same values -> returns true
        EditOutletDescriptor copyDescriptor = new EditOutletDescriptor(DESC_BETA);
        EditOutletCommand commandWithSameValues = new EditOutletCommand(INDEX_FIRST_ENTRY, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditOutletCommand(INDEX_SECOND_ENTRY, DESC_ALPHA)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditOutletCommand(INDEX_FIRST_ENTRY, DESC_ALPHA)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditOutletDescriptor editOutletDescriptor = new EditOutletDescriptor();
        EditOutletCommand editOutletCommand = new EditOutletCommand(index, editOutletDescriptor);
        String expected = EditOutletCommand.class.getCanonicalName() + "{index=" + index + ", editOutletDescriptor="
                + editOutletDescriptor + "}";
        assertEquals(expected, editOutletCommand.toString());
    }
}
