package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.outlet.UniqueOutletList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.tag.TagCombo;
import seedu.address.model.tag.UniqueTagComboList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed for persons and outlets.
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueOutletList outlets;
    private final UniqueTagComboList tagCombos;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        outlets = new UniqueOutletList();
        tagCombos = new UniqueTagComboList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setOutlets(newData.getOutletList());
    }

    /**
     * Replaces the contents of the outlet list with {@code outlets}.
     * {@code outlets} must not contain duplicate outlets.
     */
    public void setOutlets(List<Outlet> outlets) {
        this.outlets.setOutlets(outlets);
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// outlet-level operations

    /**
     * Returns true if an outlet with the same identity as {@code outlet} exists in the address book.
     */
    public boolean hasOutlet(Outlet outlet) {
        requireNonNull(outlet);
        return outlets.contains(outlet);
    }

    /**
     * Adds an outlet to the address book.
     * The outlet must not already exist in the address book.
     */
    public void addOutlet(Outlet outlet) {
        outlets.add(outlet);
    }

    /**
     * Replaces the given outlet {@code target} in the list with {@code editedOutlet}.
     * {@code target} must exist in the address book.
     * The outlet identity of {@code editedOutlet} must not be the same as another existing outlet in the address
     * book.
     */
    public void setOutlet(Outlet target, Outlet editedOutlet) {
        requireNonNull(editedOutlet);

        outlets.setOutlet(target, editedOutlet);
    }

    /**
     * Removes {@code outlet} from this {@code AddressBook}.
     * {@code outlet} must exist in the address book.
     */
    public void removeOutlet(Outlet outlet) {
        outlets.remove(outlet);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("outlets", outlets)
                .add("tagCombos", tagCombos)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Outlet> getOutletList() {
        return outlets.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<TagCombo> getTagComboList() {
        return tagCombos.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons) && outlets.equals(otherAddressBook.outlets)
                && tagCombos.equals(otherAddressBook.tagCombos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, outlets, tagCombos);
    }
}
