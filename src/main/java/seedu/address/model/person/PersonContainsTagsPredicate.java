package seedu.address.model.person;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s {@code tags} contains all the other tags provided.
 */
public class PersonContainsTagsPredicate implements Predicate<Person> {
    private final Set<Tag> tags;

    /**
     * Constructs a {@code PersonContainsTagsPredicate}. Converts the given {@code Tag}s in the {@code Set<Tag>} to
     * lowercase.
     */
    public PersonContainsTagsPredicate(Set<Tag> tags) {
        Set<Tag> queryTagsLower = tags.stream()
                .map(tag -> new Tag(tag.tagName.toLowerCase()))
                .collect(Collectors.toSet());
        this.tags = queryTagsLower;
    }

    @Override
    public boolean test(Person person) {
        Set<Tag> personTagsLower = person.getTags().stream()
                .map(tag -> new Tag(tag.tagName.toLowerCase()))
                .collect(Collectors.toSet());
        return personTagsLower.containsAll(tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsTagsPredicate)) {
            return false;
        }

        PersonContainsTagsPredicate otherPersonContainsTags = (PersonContainsTagsPredicate) other;
        return tags.equals(otherPersonContainsTags.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tags", tags).toString();
    }
}
