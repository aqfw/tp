package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.person.Person;
import seedu.address.model.tag.TagCombo;
import seedu.address.model.tag.TagComboName;
import seedu.address.model.tag.TagCounter;
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
        List<Person> personsToAdd = Arrays.asList(ALICE, BOB);

        CommandResult commandResult = new AddByCsvCommand(personsToAdd).execute(modelStub);

        assertEquals(String.format(AddByCsvCommand.MESSAGE_SUCCESS, 2),
                commandResult.getFeedbackToUser());
        assertEquals(personsToAdd, modelStub.personsAdded);
    }

    @Test
    public void execute_singlePerson_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        List<Person> personsToAdd = Collections.singletonList(ALICE);

        CommandResult commandResult = new AddByCsvCommand(personsToAdd).execute(modelStub);

        assertEquals(String.format(AddByCsvCommand.MESSAGE_SUCCESS, 1),
                commandResult.getFeedbackToUser());
        assertEquals(personsToAdd, modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        List<Person> personsToAdd = Collections.singletonList(ALICE);
        AddByCsvCommand command = new AddByCsvCommand(personsToAdd);
        ModelStub modelStub = new ModelStubWithPerson(ALICE);

        assertThrows(CommandException.class,
                String.format(AddByCsvCommand.MESSAGE_DUPLICATE_PERSON,
                        Messages.format(ALICE)), () -> command.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonWithinCsv_throwsCommandException() {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person duplicateAlice = new PersonBuilder(ALICE).withName("Alice Clone").build();
        List<Person> personsToAdd = Arrays.asList(ALICE, duplicateAlice);
        AddByCsvCommand command = new AddByCsvCommand(personsToAdd);
        String expectedMessage = String.format(
                AddByCsvCommand.MESSAGE_DUPLICATE_PERSON, Messages.format(duplicateAlice));

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(modelStub));
        assertTrue(modelStub.personsAdded.isEmpty());
    }

    @Test
    public void execute_moreThanBatchSize_throwsCommandException() {
        List<Person> personsToAdd = buildUniquePersons(26);
        AddByCsvCommand command = new AddByCsvCommand(personsToAdd);
        ModelStub modelStub = new ModelStubWithExistingCandidates(0);
        String expectedMessage = String.format(AddByCsvCommand.MESSAGE_CSV_LIMIT_EXCEEDED, 26, 25);

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(modelStub));
    }

    @Test
    public void execute_exceedsRemainingCapacity_throwsCommandException() {
        List<Person> personsToAdd = buildUniquePersons(10);
        AddByCsvCommand command = new AddByCsvCommand(personsToAdd);
        ModelStub modelStub = new ModelStubWithExistingCandidates(990);
        String expectedMessage = String.format(AddByCsvCommand.MESSAGE_CSV_LIMIT_EXCEEDED, 10, 9);

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(modelStub));
    }

    @Test
    public void execute_atRemainingCapacityBoundary_success() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubWithExistingCandidates(990);
        List<Person> personsToAdd = buildUniquePersons(9);

        CommandResult commandResult = new AddByCsvCommand(personsToAdd).execute(modelStub);

        assertEquals(String.format(AddByCsvCommand.MESSAGE_SUCCESS, 9), commandResult.getFeedbackToUser());
        assertEquals(personsToAdd, modelStub.personsAdded);
    }

    @Test
    public void execute_atFullCapacity_throwsCommandException() {
        List<Person> personsToAdd = buildUniquePersons(1);
        AddByCsvCommand command = new AddByCsvCommand(personsToAdd);
        ModelStub modelStub = new ModelStubWithExistingCandidates(999);
        String expectedMessage = String.format(AddByCsvCommand.MESSAGE_CSV_LIMIT_EXCEEDED, 1, 0);

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        AddByCsvCommand addAliceCommand = new AddByCsvCommand(Collections.singletonList(ALICE));
        AddByCsvCommand addBobCommand = new AddByCsvCommand(Collections.singletonList(BOB));

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddByCsvCommand addAliceCommandCopy = new AddByCsvCommand(Collections.singletonList(ALICE));
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
        List<Person> personsToAdd = Collections.singletonList(ALICE);
        AddByCsvCommand command = new AddByCsvCommand(personsToAdd);
        String expected = AddByCsvCommand.class.getCanonicalName()
                + "{personsToAdd=" + personsToAdd + "}";
        assertEquals(expected, command.toString());
    }

    private List<Person> buildUniquePersons(int count) {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            persons.add(new PersonBuilder()
                    .withName("Person " + i)
                    .withPhone(String.format("%08d", 80000000 + i))
                    .withEmail("p" + i + "@example.com")
                    .withAddress("Blk " + i + " Street")
                    .withPostalCode(String.format("%06d", 100000 + i))
                    .build());
        }
        return persons;
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
        public void addPersonAtIndex(Person person, Index index) {
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

        @Override
        public void resetFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasOutlet(Outlet outlet) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteOutlet(Outlet target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addOutlet(Outlet outlet) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addOutletAtIndex(Outlet outlet, Index index) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setOutlet(Outlet target, Outlet editedOutlet) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Outlet> getFilteredOutletList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredOutletList(Predicate<Outlet> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetFilteredOutletList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Predicate<? super Person> getFilteredPersonPredicate() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setFilteredPersonPredicate(Predicate<? super Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public TagCounter getTagCounter() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void recordCommand(UndoableCommand undoableCommand) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public CommandResult undo() throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public CommandResult redo() throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<TagCombo> getTagComboList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTagCombo(TagCombo tagCombo) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTagCombo(TagCombo target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTagCombo(TagCombo tagCombo) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTagComboAtIndex(TagCombo tagCombo, Index index) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public TagCombo getTagCombo(TagComboName tagComboName) {
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

    /**
     * A Model stub that accepts persons being added with a configurable existing candidate count.
     */
    private class ModelStubWithExistingCandidates extends ModelStubAcceptingPersonAdded {
        private final int existingCandidates;

        ModelStubWithExistingCandidates(int existingCandidates) {
            this.existingCandidates = existingCandidates;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new ReadOnlyAddressBook() {
                @Override
                public ObservableList<Person> getPersonList() {
                    return FXCollections.observableArrayList(Collections.nCopies(existingCandidates, ALICE));
                }

                @Override
                public ObservableList<Outlet> getOutletList() {
                    return FXCollections.observableArrayList();
                }

                @Override
                public ObservableList<TagCombo> getTagComboList() {
                    return FXCollections.observableArrayList();
                }
            };
        }
    }
}
