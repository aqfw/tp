package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UndoableCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.person.Person;
import seedu.address.model.tag.TagCombo;
import seedu.address.model.tag.TagComboName;
import seedu.address.model.tag.TagCounter;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    /** {@code Predicate} that always evaluate to true */
    Predicate<Outlet> PREDICATE_SHOW_ALL_OUTLETS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Adds the given person at a specified index.
     * {@code person} must not already exist in the address book.
     */
    void addPersonAtIndex(Person person, Index index);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Resets all filters currently applied.
     */
    void resetFilteredPersonList();

    /**
     * Retrives and sets the filter.
     */

    Predicate<? super Person> getFilteredPersonPredicate();

    void setFilteredPersonPredicate(Predicate<? super Person> predicate);

    /**
     * Returns true if an outlet with the same identity as {@code outlet} exists in the address book.
     */
    boolean hasOutlet(Outlet outlet);

    /**
     * Deletes the given outlet.
     * The outlet must exist in the address book.
     */
    void deleteOutlet(Outlet target);

    /**
     * Adds the given outlet.
     * {@code outlet} must not already exist in the address book.
     */
    void addOutlet(Outlet outlet);

    /**
     * Adds the given outlet at a specified index.
     * {@code outlet} must not already exist in the address book.
     */
    void addOutletAtIndex(Outlet outlet, Index index);

    /**
     * Replaces the given outlet {@code target} with {@code editedOutlet}.
     * {@code target} must exist in the address book.
     * The outlet identity of {@code editedOutlet} must not be the same as another existing outlet in the address
     * book.
     */

    void setOutlet(Outlet target, Outlet editedOutlet);

    /**
     * Returns true if a {@code TagCombo} with the same identity as tagCombo exists in the {@code AddressBook}.
     */
    boolean hasTagCombo(TagCombo tagCombo);

    /**
     * Deletes the given {@code TagCombo}.
     * The {@code TagCombo} must exist in the {@code AddressBook}.
     */
    void deleteTagCombo(TagCombo target);

    /**
     * Adds the given {@code TagCombo}.
     * The given {@code TagCombo} must not already exist in the {@code AddressBook}.
     */
    void addTagCombo(TagCombo tagCombo);

    /**
     * Adds the given {@code TagCombo} at a specified index.
     * The given {@code TagCombo} must not already exist in the {@code AddressBook}.
     */
    void addTagComboAtIndex(TagCombo tagCombo, Index index);

    /**
     * Gets the TagCombo associated with the tagComboName.
     */
    TagCombo getTagCombo(TagComboName tagComboName);

    /** Returns an unmodifiable view of the filtered outlet list */
    ObservableList<Outlet> getFilteredOutletList();

    /**
     * Updates the filter of the filtered outlet list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredOutletList(Predicate<Outlet> predicate);

    /**
     * Resets all filters currently applied to outlets.
     */
    void resetFilteredOutletList();

    /**
     * Returns a list of the tags in the model, along with their frequencies in descending order.
     */
    TagCounter getTagCounter();

    /**
     * Returns the list of tag combos.
     */
    ObservableList<TagCombo> getTagComboList();

    void recordCommand(UndoableCommand undoableCommand);

    void undo() throws CommandException;

    void redo() throws CommandException;
}
