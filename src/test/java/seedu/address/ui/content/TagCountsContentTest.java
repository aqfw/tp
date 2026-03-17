package seedu.address.ui.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagCounter;

public class TagCountsContentTest {

    @Test
    public void equals() {
        LinkedHashMap<Tag, Integer> firstTagMap = new LinkedHashMap<Tag, Integer>();
        firstTagMap.put(new Tag("python"), 1);
        LinkedHashMap<Tag, Integer> secondTagMap = new LinkedHashMap<Tag, Integer>();
        secondTagMap.put(new Tag("python"), 2);

        TagCountsContent firstTagCountsContent = new TagCountsContent(new TagCounter(firstTagMap));
        TagCountsContent secondTagCountsContent = new TagCountsContent(new TagCounter(secondTagMap));

        assertTrue(firstTagCountsContent.equals(firstTagCountsContent));
        assertFalse(firstTagCountsContent.equals(secondTagCountsContent));
    }

    @Test
    public void hashcode() {
        LinkedHashMap<Tag, Integer> firstTagMap = new LinkedHashMap<Tag, Integer>();
        firstTagMap.put(new Tag("python"), 1);
        LinkedHashMap<Tag, Integer> secondTagMap = new LinkedHashMap<Tag, Integer>();
        secondTagMap.put(new Tag("python"), 2);

        TagCountsContent firstTagCountsContent = new TagCountsContent(new TagCounter(firstTagMap));
        TagCountsContent secondTagCountsContent = new TagCountsContent(new TagCounter(secondTagMap));

        assertNotEquals(firstTagCountsContent.hashCode(), secondTagCountsContent.hashCode());
        assertEquals(firstTagCountsContent.hashCode(), firstTagCountsContent.hashCode());
    }
}
