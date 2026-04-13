package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.testutil.PersonBuilder;

public class TagCounterTest {
    @Test
    public void toStringMethod() {
        TagCounter tagCounter = new TagCounter();
        Person p = new PersonBuilder().withTags("friend", "colleague").build();
        tagCounter.incrementTags(p, new UniquePersonList().asUnmodifiableObservableList());
        String result = tagCounter.toString();
        assertEquals("{[friend]=1, [colleague]=1}", result);
    }

    @Test
    public void incrementTags_success() {
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        TagCounter tagCounter = new TagCounter();
        UniquePersonList uniquePersonList = new UniquePersonList();

        // Equality with empty counters
        assertEquals(new TagCounter(tagMap), tagCounter);

        // Addition of person with 1 tag
        tagCounter.incrementTags(ALICE, uniquePersonList.asUnmodifiableObservableList());
        tagMap.put(new Tag("friends"), 1);
        assertEquals(new TagCounter(tagMap), tagCounter);

        // Addition of person with multiple tags
        tagCounter.incrementTags(BENSON, uniquePersonList.asUnmodifiableObservableList());
        tagMap.put(new Tag("friends"), 2);
        tagMap.put(new Tag("owesMoney"), 1);
        assertEquals(new TagCounter(tagMap), tagCounter);

        // Addition of person with no tags
        tagCounter.incrementTags(CARL, uniquePersonList.asUnmodifiableObservableList());
        assertEquals(new TagCounter(tagMap), tagCounter);
    }

    @Test
    public void decrementTags_success() {
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 2);
        tagMap.put(new Tag("owesMoney"), 1);
        TagCounter tagCounter = new TagCounter();
        UniquePersonList uniquePersonList = new UniquePersonList();
        tagCounter.incrementTags(ALICE, uniquePersonList.asUnmodifiableObservableList());
        tagCounter.incrementTags(BENSON, uniquePersonList.asUnmodifiableObservableList());
        tagCounter.incrementTags(CARL, uniquePersonList.asUnmodifiableObservableList());

        // Check TagCounter is correct initially
        assertEquals(new TagCounter(tagMap), tagCounter);

        // Decrement of person with no tags
        tagCounter.incrementTags(CARL, uniquePersonList.asUnmodifiableObservableList());
        assertEquals(new TagCounter(tagMap), tagCounter);

        // Decrement of person with 1 tag
        tagCounter.decrementTags(ALICE, uniquePersonList.asUnmodifiableObservableList());
        tagMap.put(new Tag("friends"), 1);
        assertEquals(new TagCounter(tagMap), tagCounter);

        // Decrement of person with multiple tags
        tagCounter.decrementTags(BENSON, uniquePersonList.asUnmodifiableObservableList());
        tagMap.clear();
        assertEquals(new TagCounter(tagMap), tagCounter);

    }

    @Test
    public void resetTagCounter() {
        UniquePersonList uniquePersonList = new UniquePersonList();
        for (Person person : getTypicalPersons()) {
            uniquePersonList.add(person);
        }

        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 3);
        tagMap.put(new Tag("owesMoney"), 1);

        TagCounter tagCounter = new TagCounter();

        tagCounter.resetTagCounter(uniquePersonList.asUnmodifiableObservableList());
        assertEquals(new TagCounter(tagMap), tagCounter);
    }

    @Test
    public void decrementTags_reset() {
        UniquePersonList uniquePersonList = new UniquePersonList();
        for (Person person : getTypicalPersons()) {
            uniquePersonList.add(person);
        }

        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("owesMoney"), 1);
        tagMap.put(new Tag("friends"), 3);

        TagCounter tagCounter = new TagCounter();
        tagCounter.decrementTags(BENSON, uniquePersonList.asUnmodifiableObservableList());

        assertEquals(new TagCounter(tagMap), tagCounter);
    }

    @Test
    public void incrementTags_reset() {
        UniquePersonList uniquePersonList = new UniquePersonList();
        for (Person person : getTypicalPersons()) {
            uniquePersonList.add(person);
        }

        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("friends"), 3);
        tagMap.put(new Tag("owesMoney"), 1);

        LinkedHashMap<Tag, Integer> secondTagMap = new LinkedHashMap<Tag, Integer>();
        secondTagMap.put(new Tag("friends"), -1);

        TagCounter tagCounter = new TagCounter(secondTagMap);
        tagCounter.incrementTags(BENSON, uniquePersonList.asUnmodifiableObservableList());

        assertEquals(new TagCounter(tagMap), tagCounter);
    }

    @Test
    public void isEmpty() {
        UniquePersonList uniquePersonList = new UniquePersonList();
        TagCounter tagCounter = new TagCounter();
        assertTrue(tagCounter.isEmpty());

        tagCounter.incrementTags(BENSON, uniquePersonList.asUnmodifiableObservableList());
        assertFalse(tagCounter.isEmpty());
    }

    @Test
    public void displayDescendingOrder() {
        LinkedHashMap<Tag, Integer> tagMap = new LinkedHashMap<Tag, Integer>();
        tagMap.put(new Tag("python"), 1);
        tagMap.put(new Tag("java"), 3);
        tagMap.put(new Tag("C"), 2);

        TagCounter tagCounter = new TagCounter(tagMap);
        assertEquals("{[java]=3, [C]=2, [python]=1}", tagCounter.displayDescendingOrder().toString());
    }

    @Test
    public void equals() {
        LinkedHashMap<Tag, Integer> firstTagMap = new LinkedHashMap<Tag, Integer>();
        firstTagMap.put(new Tag("python"), 1);
        LinkedHashMap<Tag, Integer> secondTagMap = new LinkedHashMap<Tag, Integer>();
        secondTagMap.put(new Tag("python"), 1);
        LinkedHashMap<Tag, Integer> thirdTagMap = new LinkedHashMap<Tag, Integer>();
        thirdTagMap.put(new Tag("python"), 2);

        assertEquals(new TagCounter(firstTagMap), new TagCounter(secondTagMap));
        assertNotEquals(new TagCounter(firstTagMap), new TagCounter(thirdTagMap));
    }
}
