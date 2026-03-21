package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class TagComboTest {

    Set<Tag> firstTagSet = Set.of(new Tag("python"), new Tag("java"));
    Set<Tag> secondTagSet = Set.of(new Tag("python"), new Tag("java"));
    Set<Tag> thirdTagSet = Set.of(new Tag("python"), new Tag("java"), new Tag("C"));

    TagCombo firstTagCombo = new TagCombo("one", firstTagSet);
    TagCombo secondTagCombo = new TagCombo("two", secondTagSet);
    TagCombo thirdTagCombo = new TagCombo("three", thirdTagSet);

    @Test
    public void equals() {
        assertFalse(firstTagCombo.equals(secondTagCombo));
        assertFalse(firstTagCombo.equals(thirdTagCombo));
        assertTrue(firstTagCombo.equals(firstTagCombo));
        assertFalse(firstTagCombo.equals((TagCombo) null));
    }

    @Test
    public void isSameTagCombo_sameTagSetDifferentName_returnsTrue() {
        assertTrue(firstTagCombo.isSameTagCombo(secondTagCombo));
    }

    @Test
    public void isSameTagCombo_differentTagSetDifferentName_returnFalse() {
        assertFalse(firstTagCombo.isSameTagCombo(secondTagCombo));
    }

    @Test
    public void hashcode() {
        assertEquals(firstTagCombo.hashCode(), firstTagCombo.hashCode());
        assertNotEquals(firstTagCombo.hashCode(), thirdTagCombo.hashCode());
        assertNotEquals(firstTagCombo.hashCode(), secondTagCombo.hashCode());
    }
}
