package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

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
import seedu.address.model.tag.TagCounter;
import seedu.address.model.tag.exceptions.DuplicateTagComboException;
import seedu.address.testutil.TagComboBuilder;

public class AddTagComboCommandTest {

    private Model model;

    @Test
    public void constructor_nullTagCombo_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddTagComboCommand(null));
    }

    @Test
    public void execute_tagComboAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTagComboAdded modelStub = new ModelStubAcceptingTagComboAdded();
        TagCombo validTagCombo = new TagComboBuilder().build();

        CommandResult commandResult = new AddTagComboCommand(validTagCombo).execute(modelStub);

        assertEquals(String.format(AddTagComboCommand.MESSAGE_SUCCESS, Messages.format(validTagCombo)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validTagCombo), modelStub.tagCombosAdded);
    }

    @Test
    public void execute_duplicateTagCombo_throwsCommandException() {
        TagCombo validTagCombo = new TagComboBuilder().build();
        AddTagComboCommand addTagComboCommand = new AddTagComboCommand(validTagCombo);
        ModelStub modelStub = new ModelStubWithTagCombo(validTagCombo);

        assertThrows(CommandException.class,
                AddTagComboCommand.MESSAGE_DUPLICATE_TAG_COMBO, () -> addTagComboCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        TagCombo mlDev = new TagComboBuilder().withName("ml dev").build();
        TagCombo pythonDev = new TagComboBuilder().withName("python dev").build();
        AddTagComboCommand addmlDevCommand = new AddTagComboCommand(mlDev);
        AddTagComboCommand addpythonDevCommand = new AddTagComboCommand(pythonDev);

        // same object -> returns true
        assertTrue(addmlDevCommand.equals(addmlDevCommand));

        // same values -> returns true
        AddTagComboCommand addmlDevCommandCopy = new AddTagComboCommand(mlDev);
        assertTrue(addmlDevCommand.equals(addmlDevCommandCopy));

        // different types -> returns false
        assertFalse(addmlDevCommand.equals(1));

        // null -> returns false
        assertFalse(addmlDevCommand.equals(null));

        // different person -> returns false
        assertFalse(addmlDevCommand.equals(addpythonDevCommand));
    }

    @Test
    public void toStringMethod() {
        TagCombo mlDev = new TagComboBuilder().withName("ml dev").build();
        AddTagComboCommand addmlDevCommand = new AddTagComboCommand(mlDev);
        String expected = AddTagComboCommand.class.getCanonicalName() + "{toAdd=" + mlDev + "}";
        assertEquals(expected, addmlDevCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
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
        public TagCounter getTagCounter() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void recordCommand(UndoableCommand undoableCommand) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undo() throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redo() throws CommandException {
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
    }

    /**
     * A Model stub that contains a single TagCombo.
     */
    private class ModelStubWithTagCombo extends ModelStub {
        private final TagCombo tagCombo;

        ModelStubWithTagCombo(TagCombo tagCombo) {
            requireNonNull(tagCombo);
            this.tagCombo = tagCombo;
        }

        @Override
        public boolean hasTagCombo(TagCombo tagCombo) {
            requireNonNull(tagCombo);
            return this.tagCombo.isSameTagCombo(tagCombo);
        }

        @Override
        public void addTagCombo(TagCombo tagCombo) {
            requireNonNull(tagCombo);
            if (this.tagCombo.isSameTagCombo(tagCombo)) {
                throw new DuplicateTagComboException(
                        "A Tag Combo with this name or tag set is already in the address book."
                );
            }
        }
    }

    /**
     * A Model stub that always accept the TagCombo being added.
     */
    private class ModelStubAcceptingTagComboAdded extends ModelStub {
        final ArrayList<TagCombo> tagCombosAdded = new ArrayList<TagCombo>();

        @Override
        public boolean hasTagCombo(TagCombo tagCombo) {
            requireNonNull(tagCombo);
            return tagCombosAdded.stream().anyMatch(tagCombo::isSameTagCombo);
        }

        @Override
        public void addTagCombo(TagCombo tagCombo) {
            requireNonNull(tagCombo);
            tagCombosAdded.add(tagCombo);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
