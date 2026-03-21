package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.person.Person;
import seedu.address.model.tag.TagCombo;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the outlets list.
     * This list will not contain duplicate outlets.
     */
    ObservableList<Outlet> getOutletList();

    /**
     * Returns the set of {@code TagCombo} present in the Address Book.
     */
    ObservableList<TagCombo> getTagComboList();
}
