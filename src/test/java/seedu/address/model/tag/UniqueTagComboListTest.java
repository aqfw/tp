package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.exceptions.DuplicateTagComboException;
import seedu.address.model.tag.exceptions.TagComboNotFoundException;

public class UniqueTagComboListTest {

    private static final TagCombo TAG_COMBO_ONE = new TagCombo(new TagComboName("ml dev"), Set.of(
            new Tag("python"), new Tag("ml")
    ));
    private static final TagCombo TAG_COMBO_TWO = new TagCombo(new TagComboName("frontend java dev"), Set.of(
            new Tag("frontend"), new Tag("java")
    ));
    private static final TagCombo TAG_COMBO_THREE = new TagCombo(new TagComboName("python dev"), Set.of(
            new Tag("python"), new Tag("ml")
    ));
    private static final TagCombo TAG_COMBO_FOUR = new TagCombo(new TagComboName("ml dev"), Set.of(
            new Tag("python"), new Tag("ml"), new Tag("java")
    ));
    private final UniqueTagComboList uniqueTagComboList = new UniqueTagComboList();

    @Test
    public void contains_nullTagCombo_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagComboList.contains(null));
    }

    @Test
    public void contains_tagComboNotInList_returnsFalse() {
        assertFalse(uniqueTagComboList.contains(TAG_COMBO_ONE));
    }

    @Test
    public void contains_tagComboInList_returnsTrue() {
        uniqueTagComboList.add(TAG_COMBO_ONE);
        assertTrue(uniqueTagComboList.contains(TAG_COMBO_ONE));
    }

    @Test
    public void isSameTagCombo_tagComboWithSameTagSetDifferentName_returnsTrue() {
        uniqueTagComboList.add(TAG_COMBO_ONE);
        assertTrue(uniqueTagComboList.contains(TAG_COMBO_THREE));
    }

    @Test
    public void add_nullTagCombo_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagComboList.add(null));
    }

    @Test
    public void add_duplicateTagCombo_throwsDuplicateTagComboException() {
        uniqueTagComboList.add(TAG_COMBO_ONE);
        assertThrows(DuplicateTagComboException.class, () -> uniqueTagComboList.add(TAG_COMBO_THREE));
    }

    @Test
    public void add_duplicateName_throwsDuplicateTagComboException() {
        uniqueTagComboList.add(TAG_COMBO_ONE);
        assertThrows(DuplicateTagComboException.class, () -> uniqueTagComboList.add(TAG_COMBO_FOUR));
    }

    @Test
    public void remove_nullTagCombo_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagComboList.remove(null));
    }

    @Test
    public void remove_tagComboDoesNotExist_throwsTagComboNotFoundException() {
        assertThrows(TagComboNotFoundException.class, () -> uniqueTagComboList.remove(TAG_COMBO_ONE));
    }

    @Test
    public void remove_existingTagCombo_removesTagCombo() {
        uniqueTagComboList.add(TAG_COMBO_ONE);
        uniqueTagComboList.remove(TAG_COMBO_ONE);
        UniqueTagComboList expectedUniqueTagComboList = new UniqueTagComboList();
        assertEquals(expectedUniqueTagComboList, uniqueTagComboList);
    }

    @Test
    public void setTagCombos_nullUniqueTagComboList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagComboList.setTagCombos((UniqueTagComboList) null));
    }

    @Test
    public void setTagCombos_uniqueTagComboList_replacesOwnListWithProvidedUniqueTagComboList() {
        uniqueTagComboList.add(TAG_COMBO_ONE);
        UniqueTagComboList expectedUniqueTagComboList = new UniqueTagComboList();
        expectedUniqueTagComboList.add(TAG_COMBO_TWO);
        uniqueTagComboList.setTagCombos(expectedUniqueTagComboList);
        assertEquals(expectedUniqueTagComboList, uniqueTagComboList);
    }

    @Test
    public void setTagCombos_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagComboList.setTagCombos((List<TagCombo>) null));
    }

    @Test
    public void setTagCombos_list_replacesOwnListWithProvidedList() {
        uniqueTagComboList.add(TAG_COMBO_ONE);
        List<TagCombo> tagComboList = Collections.singletonList(TAG_COMBO_TWO);
        uniqueTagComboList.setTagCombos(tagComboList);
        UniqueTagComboList expectedUniqueTagComboList = new UniqueTagComboList();
        expectedUniqueTagComboList.add(TAG_COMBO_TWO);
        assertEquals(expectedUniqueTagComboList, uniqueTagComboList);
    }

    @Test
    public void setTagCombos_listWithDuplicateTagCombos_throwsDuplicateTagComboException() {
        List<TagCombo> listWithDuplicateTagCombos = Arrays.asList(TAG_COMBO_ONE, TAG_COMBO_ONE);
        assertThrows(DuplicateTagComboException.class, () -> uniqueTagComboList
                .setTagCombos(listWithDuplicateTagCombos));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueTagComboList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void getTagCombo_success() {
        uniqueTagComboList.add(TAG_COMBO_ONE);
        assertEquals(TAG_COMBO_ONE, uniqueTagComboList.getTagCombo(TAG_COMBO_ONE.getName()));
    }

    @Test
    public void getTagCombo_tagComboNotFound() {
        assertThrows(TagComboNotFoundException.class, () -> uniqueTagComboList.getTagCombo(TAG_COMBO_ONE.getName()));
    }

    @Test
    public void equals() {
        uniqueTagComboList.add(TAG_COMBO_ONE);

        assertTrue(uniqueTagComboList.equals(uniqueTagComboList));

        assertFalse(uniqueTagComboList.equals(new UniqueTagComboList()));

        assertFalse(uniqueTagComboList.equals(null));

        assertFalse(uniqueTagComboList.equals(5.0f));
    }

    @Test
    public void toStringMethod() {
        assertEquals(new ToStringBuilder(uniqueTagComboList)
                .add("internalList", uniqueTagComboList.asUnmodifiableObservableList())
                .add("internalMap", new HashMap<TagComboName, TagCombo>())
                .toString(),
                uniqueTagComboList.toString());
    }
}
