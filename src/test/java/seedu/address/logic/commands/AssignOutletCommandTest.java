package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.person.Person;
import seedu.address.testutil.OutletBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests for {@code AssignOutletCommand}.
 */
public class AssignOutletCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addOutlet(new OutletBuilder().build());
        model.addOutlet(new OutletBuilder().withName("FinServ").withAddress("Marina Bay").withPostalCode("018956")
                .build());
    }

    @Test
    public void execute_validIndexes_success() {
        Person personToAssign = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Outlet outletToAssign = model.getFilteredOutletList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person assignedPerson = new PersonBuilder(personToAssign).withWorkingAddress(outletToAssign).build();

        AssignOutletCommand assignOutletCommand = new AssignOutletCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        String expectedMessage = String.format(AssignOutletCommand.MESSAGE_SUCCESS, personToAssign.getName(),
                outletToAssign.getOutletName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToAssign, assignedPerson);

        assertCommandSuccess(assignOutletCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_failure() {
        Index outOfBoundPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AssignOutletCommand assignOutletCommand = new AssignOutletCommand(outOfBoundPersonIndex, INDEX_FIRST_PERSON);

        assertCommandFailure(assignOutletCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidOutletIndex_failure() {
        Index outOfBoundOutletIndex = Index.fromOneBased(model.getFilteredOutletList().size() + 1);
        AssignOutletCommand assignOutletCommand = new AssignOutletCommand(INDEX_FIRST_PERSON, outOfBoundOutletIndex);

        assertCommandFailure(assignOutletCommand, model, Messages.MESSAGE_INVALID_OUTLET_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        AssignOutletCommand assignFirstCommand = new AssignOutletCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        AssignOutletCommand assignSecondCommand = new AssignOutletCommand(INDEX_SECOND_PERSON, INDEX_SECOND_PERSON);

        assertTrue(assignFirstCommand.equals(assignFirstCommand));
        assertTrue(assignFirstCommand.equals(new AssignOutletCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON)));
        assertFalse(assignFirstCommand.equals(1));
        assertFalse(assignFirstCommand.equals(null));
        assertFalse(assignFirstCommand.equals(assignSecondCommand));
    }

    @Test
    public void toStringMethod() {
        AssignOutletCommand assignOutletCommand = new AssignOutletCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        String expected = AssignOutletCommand.class.getCanonicalName() + "{candidateIndex="
                + INDEX_FIRST_PERSON + ", outletIndex=" + INDEX_SECOND_PERSON + "}";
        assertEquals(expected, assignOutletCommand.toString());
    }
}
