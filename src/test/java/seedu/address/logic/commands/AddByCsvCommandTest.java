package seedu.address.logic.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import static seedu.address.testutil.Assert.assertThrows;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests for {@code AddByCsvCommand}.
 */
public class AddByCsvCommandTest {

    @Test
    public void constructor_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddByCsvCommand(null));
    }

    @Test
    public void execute_personsAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        List<Person> personsToAdd = Arrays.asList(alice, bob);

        CommandResult commandResult = new AddByCsvCommand(personsToAdd).execute(modelStub);

        assertEquals(String.format(AddByCsvCommand.MESSAGE_SUCCESS, 2),
                commandResult.getFeedbackToUser());
        assertEquals(personsToAdd, modelStub.personsAdded);
    }

    @Test
    public void execute_singlePerson_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person alice = new PersonBuilder().withName("Alice").build();
        List<Person> personsToAdd = Collections.singletonList(alice);

        CommandResult commandResult = new AddByCsvCommand(personsToAdd).execute(modelStub);

        assertEquals(String.format(AddByCsvCommand.MESSAGE_SUCCESS, 1),
                commandResult.getFeedbackToUser());
        assertEquals(personsToAdd, modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person alice = new PersonBuilder().withName("Alice").build();
        List<Person> personsToAdd = Collections.singletonList(alice);
        AddByCsvCommand command = new AddByCsvCommand(personsToAdd);
        ModelStub modelStub = new ModelStubWithPerson(alice);

        assertThrows(CommandException.class,
                String.format(AddByCsvCommand.MESSAGE_DUPLICATE_PERSON,
                        Messages.format(alice)), () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddByCsvCommand addAliceCommand = new AddByCsvCommand(Collections.singletonList(alice));
        AddByCsvCommand addBobCommand = new AddByCsvCommand(Collections.singletonList(bob));

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddByCsvCommand addAliceCommandCopy = new AddByCsvCommand(Collections.singletonList(alice));
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different persons -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        Person alice = new PersonBuilder().withName("Alice").build();
        List<Person> personsToAdd = Collections.singletonList(alice);
        AddByCsvCommand command = new AddByCsvCommand(personsToAdd);
        String expected = AddByCsvCommand.class.getCanonicalName()
                + "{personsToAdd=" + personsToAdd + "}";
        assertEquals(expected, command.toString());
    }

    /**
     * A default model stub that has all methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accepts persons being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
