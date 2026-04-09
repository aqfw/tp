package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * A class representing the name of a {@code TagCombo}.
 */
public class TagComboName {
    public static final String MESSAGE_CONSTRAINTS = "Names should only contain alphanumeric characters and spaces, "
            + "and it should not be blank. Should contain at most 25 characters.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";
    public static final int MAX_LENGTH_TAG_COMBO_NAME = 25;

    public final String name;

    /**
     * Constructs a {@code TagComboName}.
     */
    public TagComboName(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        this.name = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX) && test.length() <= MAX_LENGTH_TAG_COMBO_NAME;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagComboName)) {
            return false;
        }

        TagComboName otherName = (TagComboName) other;
        return name.equals(otherName.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
