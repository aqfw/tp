package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_ADDRESS_BETA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_NAME_BETA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_POSTAL_CODE_BETA;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.outlet.Outlet;
import seedu.address.testutil.OutletBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddOutletCommand}.
 */
public class AddOutletCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addOutlet(new OutletBuilder().build());
    }

    @Test
    public void execute_newOutlet_success() {
        Outlet validOutlet = new OutletBuilder().withName(VALID_OUTLET_NAME_BETA)
                .withAddress(VALID_OUTLET_ADDRESS_BETA).withPostalCode(VALID_OUTLET_POSTAL_CODE_BETA).build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addOutlet(validOutlet);

        assertCommandSuccess(new AddOutletCommand(validOutlet), model,
                String.format(AddOutletCommand.MESSAGE_SUCCESS, Messages.format(validOutlet)),
                expectedModel);
    }

    @Test
    public void execute_duplicateOutlet_throwsCommandException() {
        Outlet outletInList = model.getAddressBook().getOutletList().get(0);
        assertCommandFailure(new AddOutletCommand(outletInList), model, AddOutletCommand.MESSAGE_DUPLICATE_OUTLET);
    }


    @Test
    public void undo_addOutletCommand_success() throws CommandException {
        Outlet validOutlet = new OutletBuilder().withName(VALID_OUTLET_NAME_BETA)
                .withAddress(VALID_OUTLET_ADDRESS_BETA).withPostalCode(VALID_OUTLET_POSTAL_CODE_BETA).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        AddOutletCommand addOutletCommand = new AddOutletCommand(validOutlet);
        addOutletCommand.execute(model);
        model.recordCommand(addOutletCommand);

        model.undo();
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_addOutletCommand_success() throws CommandException {
        Outlet validOutlet = new OutletBuilder().withName(VALID_OUTLET_NAME_BETA)
                .withAddress(VALID_OUTLET_ADDRESS_BETA).withPostalCode(VALID_OUTLET_POSTAL_CODE_BETA).build();

        AddOutletCommand addOutletCommand = new AddOutletCommand(validOutlet);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        addOutletCommand.execute(model);
        expectedModel.addOutlet(validOutlet);
        model.recordCommand(addOutletCommand);

        model.undo();
        model.redo();
        assertTrue(model.hasOutlet(validOutlet));
        assertEquals(expectedModel, model);
    }

    @Test
    public void equals() {
        Outlet firstOutlet = new OutletBuilder().build();
        Outlet secondOutlet = new OutletBuilder().withName(VALID_OUTLET_NAME_BETA)
                .withAddress(VALID_OUTLET_ADDRESS_BETA).withPostalCode(VALID_OUTLET_POSTAL_CODE_BETA).build();
        AddOutletCommand addFirstCommand = new AddOutletCommand(firstOutlet);
        AddOutletCommand addSecondCommand = new AddOutletCommand(secondOutlet);

        assertTrue(addFirstCommand.equals(addFirstCommand));
        assertTrue(addFirstCommand.equals(new AddOutletCommand(firstOutlet)));
        assertFalse(addFirstCommand.equals(1));
        assertFalse(addFirstCommand.equals(null));
        assertFalse(addFirstCommand.equals(addSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Outlet outlet = new OutletBuilder().build();
        AddOutletCommand addOutletCommand = new AddOutletCommand(outlet);
        String expected = AddOutletCommand.class.getCanonicalName() + "{toAdd=" + outlet + "}";
        assertEquals(expected, addOutletCommand.toString());
    }
}
