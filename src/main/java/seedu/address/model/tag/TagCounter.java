package seedu.address.model.tag;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * A wrapper to count the frequency of occurence of {@code Tag}s in the current candidate book.
 */
public class TagCounter {
    private LinkedHashMap<Tag, Integer> tagCounter;

    public TagCounter() {
        tagCounter = new LinkedHashMap<Tag, Integer>();
    }

    public TagCounter(ReadOnlyAddressBook addressBook) {
        this.resetTagCounter(addressBook.getPersonList());
    }

    public TagCounter(FilteredList<Person> filteredPersons) {
        this.resetTagCounter(filteredPersons);
    }

    /**
     * Constructor for testing purposes.
     */
    public TagCounter(LinkedHashMap<Tag, Integer> tagCounter) {
        this.tagCounter = tagCounter;
    }

    /**
     * Adds the {@code Tag} to the {@code TagCounter} if not already present, then increments the count of the
     * {@code Tag} by 1.
     */
    private void incrementTag(Tag tag) {
        if (!tagCounter.containsKey(tag)) {
            tagCounter.put(tag, 0);
        }
        tagCounter.put(tag, tagCounter.get(tag) + 1);
    }

    /**
     * Adds all {@code Tag}s in a {@code Person} to the {@code TagCounter}.
     */
    public void incrementTags(Person person, ObservableList<Person> personList) {
        for (Tag tag : person.getTags()) {
            this.incrementTag(tag);
        }
        if (!isValid()) {
            resetTagCounter(personList);
        }
    }

    /**
     * Decrements the count of the given {@code Tag} in the tag counter by 1,
     * then removes the {@code Tag} from the {@code TagCounter} if the count is equal to 0.
     */
    private void decrementTag(Tag tag) {
        if (!tagCounter.containsKey(tag)) {
            tagCounter.put(tag, 0);
        }
        tagCounter.put(tag, tagCounter.get(tag) - 1);
        if (tagCounter.get(tag) == 0) {
            tagCounter.remove(tag);
        }
    }

    /**
     * Removes all {@code Tag}s in a {@code Person} from the {@code TagCounter}.
     */
    public void decrementTags(Person person, ObservableList<Person> personList) {
        for (Tag tag : person.getTags()) {
            this.decrementTag(tag);
        }
        if (!isValid()) {
            resetTagCounter(personList);
        }
    }

    /**
     * Resets the {@code TagCounter} using an {@code UniquePersonList} by counting all tags present. This method is to
     * be called as a last resort to reset the TagCounter to the correct state should any errors occur.
     */
    public void resetTagCounter(ObservableList<Person> personList) {
        tagCounter = new LinkedHashMap<Tag, Integer>();
        for (Person person : personList) {
            this.incrementTags(person, personList);
        }
    }

    public LinkedHashMap<Tag, Integer> getTagCounter() {
        return tagCounter;
    }

    /**
     * Checks if the given {@code TagCounter} is valid (contains any values less than or equal to 0).
     */
    private boolean isValid() {
        for (Integer value : tagCounter.values()) {
            if (value <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the {@code TagCounter} is empty.
     */
    public boolean isEmpty() {
        return tagCounter.isEmpty();
    }

    /**
     * Returns the tags in descending order of frequency, along with their counts.
     */
    public LinkedHashMap<Tag, Integer> displayDescendingOrder() {
        return tagCounter.entrySet().stream()
                .sorted(Map.Entry.<Tag, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue, (
                                a, b) -> a,
                        LinkedHashMap::new));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagCounter)) {
            return false;
        }

        TagCounter otherTagCounter = (TagCounter) other;
        return tagCounter.equals(otherTagCounter.tagCounter);
    }

    @Override
    public int hashCode() {
        return tagCounter.hashCode();
    }

    @Override
    public String toString() {
        return tagCounter.toString();
    }
}
