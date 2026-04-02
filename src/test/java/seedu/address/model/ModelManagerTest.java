package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.outlet.OutletAddress;
import seedu.address.model.outlet.OutletName;
import seedu.address.model.outlet.OutletPostalCode;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagCombo;
import seedu.address.model.tag.TagComboName;
import seedu.address.model.tag.TagCounter;
import seedu.address.model.tag.exceptions.DuplicateTagComboException;
import seedu.address.model.tag.exceptions.TagComboNotFoundException;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private static final Outlet OUTLET_ALPHA = new Outlet(
            new OutletName("TechCo"),
            new OutletAddress("Raffles Place"),
            new OutletPostalCode("048623"));
    private static final Outlet OUTLET_BETA = new Outlet(
            new OutletName("FinServ"),
            new OutletAddress("Marina Bay"),
            new OutletPostalCode("018956"));
    private static final TagCombo TAG_COMBO_ONE = new TagCombo(new TagComboName("ml dev"), Set.of(
            new Tag("python"), new Tag("ml")
    ));
    private static final TagCombo TAG_COMBO_TWO = new TagCombo(new TagComboName("frontend java dev"), Set.of(
            new Tag("frontend"), new Tag("java")
    ));

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasOutlet_nullOutlet_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasOutlet(null));
    }

    @Test
    public void hasOutlet_outletNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasOutlet(OUTLET_ALPHA));
    }

    @Test
    public void hasOutlet_outletInAddressBook_returnsTrue() {
        modelManager.addOutlet(OUTLET_ALPHA);
        assertTrue(modelManager.hasOutlet(OUTLET_ALPHA));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void getFilteredOutletList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredOutletList().remove(0));
    }

    @Test
    public void resetFilteredOutletList_resetsToShowAllOutlets() {
        modelManager.addOutlet(OUTLET_ALPHA);
        modelManager.addOutlet(OUTLET_BETA);
        modelManager.updateFilteredOutletList(outlet -> outlet.equals(OUTLET_ALPHA));
        assertEquals(1, modelManager.getFilteredOutletList().size());

        modelManager.resetFilteredOutletList();
        assertEquals(2, modelManager.getFilteredOutletList().size());
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.resetFilteredPersonList();

        AddressBook outletAddressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        outletAddressBook.addOutlet(OUTLET_ALPHA);
        outletAddressBook.addOutlet(OUTLET_BETA);
        modelManager = new ModelManager(outletAddressBook, userPrefs);
        modelManager.updateFilteredOutletList(outlet -> outlet.equals(OUTLET_ALPHA));

        // different filtered outlet list -> returns false
        assertFalse(modelManager.equals(new ModelManager(outletAddressBook, userPrefs)));

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(outletAddressBook, differentUserPrefs)));

        // different Tag Combo -> return false
        AddressBook tagComboAddressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        tagComboAddressBook.addOutlet(OUTLET_ALPHA);
        tagComboAddressBook.addOutlet(OUTLET_BETA);
        tagComboAddressBook.addTagCombo(TAG_COMBO_ONE);
        modelManager = new ModelManager(tagComboAddressBook, userPrefs);
        assertFalse(modelManager.equals(new ModelManager(outletAddressBook, userPrefs)));
    }

    @Test
    public void getTagCounter() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        UserPrefs userPrefs = new UserPrefs();
        modelManager = new ModelManager(addressBook, userPrefs);

        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 2);
        tagMap.put(new Tag("owesMoney"), 1);

        assertEquals(new TagCounter(tagMap), modelManager.getTagCounter());
    }

    @Test
    public void hasTagCombo_nullTagCombo_throwsNullException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasTagCombo(null));
    }

    @Test
    public void hasTagCombo_tagComboNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasTagCombo(TAG_COMBO_ONE));
    }

    @Test
    public void hasTagCombo_tagComboInAddressBook_returnsTrue() {
        modelManager.addTagCombo(TAG_COMBO_ONE);
        assertTrue(modelManager.hasTagCombo(TAG_COMBO_ONE));
    }

    @Test
    public void addTagCombo_tagComboInAddressBook_throwsDuplicateTagComboException() {
        modelManager.addTagCombo(TAG_COMBO_ONE);
        assertThrows(DuplicateTagComboException.class, () -> modelManager.addTagCombo(TAG_COMBO_ONE));
    }

    @Test
    public void deleteTagCombo_tagComboNotInAddressBook_throwsTagComboNotFoundException() {
        assertThrows(TagComboNotFoundException.class, () -> modelManager.deleteTagCombo(TAG_COMBO_ONE));
    }

    @Test
    public void deleteTagCombo_tagComboInAddressBook_success() {
        modelManager.addTagCombo(TAG_COMBO_ONE);
        modelManager.deleteTagCombo(TAG_COMBO_ONE);

        assertFalse(modelManager.hasTagCombo(TAG_COMBO_ONE));
    }

    @Test
    public void getTagCombo_success() {
        modelManager.addTagCombo(TAG_COMBO_ONE);
        assertEquals(TAG_COMBO_ONE, modelManager.getTagCombo(TAG_COMBO_ONE.getName()));
    }
}
