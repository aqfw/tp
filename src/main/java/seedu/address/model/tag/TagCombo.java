package seedu.address.model.tag;

import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;

/**
 * A class representing a set of {@code Tag}s, which can be used for filtering. 2 {@code TagCombo}s are considered
 * equal if they have the same set of {@code Tag}s, regardless of the name assigned to the {@code TagCombo}.
 */
public class TagCombo {
    private final String name;
    private final Set<Tag> tagSet;

    /**
     * Constructs a {@code TagCombo} with a name and tagSet.
     */
    public TagCombo(String name, Set<Tag> tagSet) {
        this.name = name;
        this.tagSet = tagSet;
    }

    /**
     * Returns true if 2 {@code TagCombo}s have the same tagSets.
     */
    public boolean isSameTagCombo(TagCombo otherTagCombo) {
        if (otherTagCombo == this) {
            return true;
        }
        return this.tagSet.equals(otherTagCombo.tagSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tagSet);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCombo)) {
            return false;
        }

        TagCombo otherTag = (TagCombo) other;
        return tagSet.equals(otherTag.tagSet) && this.name.equals(otherTag.name);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("tagSet", tagSet)
                .toString();
    }
}
