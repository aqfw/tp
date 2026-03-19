package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showOutletAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.OutletBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListOutletsCommand.
 */
public class ListOutletsCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addOutlet(new OutletBuilder().build());
        model.addOutlet(new OutletBuilder().withName("FinServ").withAddress("Marina Bay").withPostalCode("018956")
                .build());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListOutletsCommand(), model, ListOutletsCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showOutletAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListOutletsCommand(), model, ListOutletsCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
