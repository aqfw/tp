package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.outlet.OutletAddress;
import seedu.address.model.outlet.OutletName;
import seedu.address.model.outlet.OutletPostalCode;
import seedu.address.model.outlet.exceptions.DuplicateOutletException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagCombo;
import seedu.address.model.tag.TagComboName;
import seedu.address.model.tag.UniqueTagComboList;
import seedu.address.model.tag.exceptions.DuplicateTagComboException;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private static final Outlet OUTLET_ALPHA = new Outlet(
            new OutletName("TechCo"),
            new OutletAddress("Raffles Place"),
            new OutletPostalCode("048623"));
    private static final Outlet OUTLET_ALPHA_EDITED = new Outlet(
            new OutletName("TechCo"),
            new OutletAddress("Raffles Place"),
            new OutletPostalCode("048623"));
    private static final TagCombo TAG_COMBO_ONE = new TagCombo(new TagComboName("developer"), Set.of(
            new Tag("python"), new Tag("java")
    ));
    private static final TagCombo TAG_COMBO_TWO = new TagCombo(new TagComboName("developer"), Set.of(
            new Tag("python"), new Tag("java"), new Tag("C")
    ));

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
        assertEquals(Collections.emptyList(), addressBook.getOutletList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withDuplicateOutlets_throwsDuplicateOutletException() {
        List<Outlet> newOutlets = Arrays.asList(OUTLET_ALPHA, OUTLET_ALPHA_EDITED);
        AddressBookStub newData = new AddressBookStub(Collections.emptyList(), newOutlets, Collections.emptySet());

        assertThrows(DuplicateOutletException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withDuplicateTagCombos_throwsDuplicateTagComboException() {
        List<TagCombo> newTagCombos = Arrays.asList(TAG_COMBO_ONE, TAG_COMBO_TWO);
        AddressBookStub newData = new AddressBookStub(Collections.emptyList(), Collections.emptySet(), newTagCombos);

        assertThrows(DuplicateTagComboException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void hasOutlet_nullOutlet_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasOutlet(null));
    }

    @Test
    public void hasOutlet_outletNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasOutlet(OUTLET_ALPHA));
    }

    @Test
    public void hasOutlet_outletInAddressBook_returnsTrue() {
        addressBook.addOutlet(OUTLET_ALPHA);
        assertTrue(addressBook.hasOutlet(OUTLET_ALPHA));
    }

    @Test
    public void hasOutlet_outletWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addOutlet(OUTLET_ALPHA);
        assertTrue(addressBook.hasOutlet(OUTLET_ALPHA_EDITED));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void getOutletList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getOutletList().remove(0));
    }

    @Test
    public void hasTagCombo_tagComboNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasTagCombo(TAG_COMBO_ONE));
    }

    @Test
    public void hasTagCombo_tagComboInAddressBook_returnsTrue() {
        addressBook.addTagCombo(TAG_COMBO_ONE);
        assertTrue(addressBook.hasTagCombo(TAG_COMBO_ONE));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList()
                + ", outlets=" + addressBook.getOutletList() + ", tagCombos=" + new UniqueTagComboList().toString()
                + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Outlet> outlets = FXCollections.observableArrayList();
        private final ObservableList<TagCombo> tagCombos = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this(persons, Collections.emptyList(), Collections.emptySet());
        }

        AddressBookStub(Collection<Person> persons, Collection<Outlet> outlets, Collection<TagCombo> tagCombos) {
            this.persons.setAll(persons);
            this.outlets.setAll(outlets);
            this.tagCombos.addAll(tagCombos);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Outlet> getOutletList() {
            return outlets;
        }

        @Override
        public ObservableList<TagCombo> getTagComboList() {
            return tagCombos;
        }
    }

}
