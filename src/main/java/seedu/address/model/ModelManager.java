package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.UndoableCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.person.Person;
import seedu.address.model.tag.TagCombo;
import seedu.address.model.tag.TagComboName;
import seedu.address.model.tag.TagCounter;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();
    private final FilteredList<Outlet> filteredOutlets;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredOutlets = new FilteredList<>(this.addressBook.getOutletList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        resetFilteredPersonList();
    }

    @Override
    public void addPersonAtIndex(Person person, Index index) {
        addressBook.addPersonAtIndex(person, index);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public boolean hasOutlet(Outlet outlet) {
        requireNonNull(outlet);
        return addressBook.hasOutlet(outlet);
    }

    @Override
    public void deleteOutlet(Outlet target) {
        addressBook.removeOutlet(target);
    }

    @Override
    public void addOutlet(Outlet outlet) {
        addressBook.addOutlet(outlet);
        resetFilteredOutletList();
    }

    @Override
    public void addOutletAtIndex(Outlet outlet, Index index) {
        addressBook.addOutletAtIndex(outlet, index);
    }

    @Override
    public void setOutlet(Outlet target, Outlet editedOutlet) {
        requireAllNonNull(target, editedOutlet);

        addressBook.setOutlet(target, editedOutlet);
    }

    @Override
    public boolean hasTagCombo(TagCombo tagCombo) {
        return addressBook.hasTagCombo(tagCombo);
    }

    @Override
    public void deleteTagCombo(TagCombo target) {
        addressBook.removeTagCombo(target);
    }

    @Override
    public void addTagCombo(TagCombo tagCombo) {
        addressBook.addTagCombo(tagCombo);
    }

    @Override
    public void addTagComboAtIndex(TagCombo tagCombo, Index index) {
        addressBook.addTagComboAtIndex(tagCombo, index);
    }

    @Override
    public TagCombo getTagCombo(TagComboName tagComboName) {
        return addressBook.getTagCombo(tagComboName);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);

        Predicate<? super Person> currentPredicate = filteredPersons.getPredicate();

        if (currentPredicate == null) {
            filteredPersons.setPredicate(predicate);
        } else {
            filteredPersons.setPredicate(predicate.and(currentPredicate));
        }
    }

    @Override
    public void resetFilteredPersonList() {
        filteredPersons.setPredicate(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public Predicate<? super Person> getFilteredPersonPredicate() {
        return filteredPersons.getPredicate();
    }

    @Override
    public void setFilteredPersonPredicate(Predicate<? super Person> predicate) {
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public ObservableList<Outlet> getFilteredOutletList() {
        return filteredOutlets;
    }

    @Override
    public void updateFilteredOutletList(Predicate<Outlet> predicate) {
        requireNonNull(predicate);

        Predicate<? super Outlet> currentPredicate = filteredOutlets.getPredicate();

        if (currentPredicate == null) {
            filteredOutlets.setPredicate(predicate);
        } else {
            filteredOutlets.setPredicate(predicate.and(currentPredicate));
        }
    }

    @Override
    public void resetFilteredOutletList() {
        filteredOutlets.setPredicate(PREDICATE_SHOW_ALL_OUTLETS);
    }

    //=========== TagCounter Accessors =============================================================
    @Override
    public TagCounter getTagCounter() {
        return new TagCounter(filteredPersons);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredOutlets.equals(otherModelManager.filteredOutlets);
    }

    //=========== Undo/Redo Command History =============================================================

    /**
     * Records an undoable command in the undo stack.
     */
    public void recordCommand(UndoableCommand undoableCommand) {
        requireNonNull(undoableCommand);
        undoStack.push(undoableCommand);
        redoStack.clear();
    }

    @Override
    public ObservableList<TagCombo> getTagComboList() {
        return addressBook.getTagComboList();
    }

    /**
     * Returns true if there is at least one command to undo.
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /** Undoes the last executed command. */
    @Override
    public void undo() throws CommandException {
        if (!canUndo()) {
            throw new CommandException("Nothing to undo.");
        }

        UndoableCommand lastCommand = (UndoableCommand) undoStack.pop();
        lastCommand.undo(this);

        redoStack.push(lastCommand);
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    @Override
    public void redo() throws CommandException {
        if (!canRedo()) {
            throw new CommandException("Nothing to redo.");
        }

        UndoableCommand lastCommand = (UndoableCommand) redoStack.pop();
        lastCommand.redo(this);

        undoStack.push(lastCommand);
    }
}
