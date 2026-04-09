package seedu.address.model.outlet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Outlet's name.
 * Guarantees: immutable; is valid as declared in {@link #isValidOutletName(String)}
 */
public class OutletName {

    public static final int MAX_LENGTH = 26;
    public static final String MESSAGE_CONSTRAINTS = "Outlet names should not be blank, should be at most "
            + MAX_LENGTH + " characters long, and should not contain command delimiters such as n/, a/, or pc/.";
    public static final String VALIDATION_REGEX = "[^\\s].*";
    private static final String[] INVALID_DELIMITERS = {"n/", "a/", "pc/"};

    public final String value;

    /**
     * Constructs an {@code OutletName}.
     *
     * @param outletName A valid outlet name.
     */
    public OutletName(String outletName) {
        requireNonNull(outletName);
        checkArgument(isValidOutletName(outletName), MESSAGE_CONSTRAINTS);
        value = outletName;
    }

    /**
     * Returns true if a given string is a valid outlet name.
     */
    public static boolean isValidOutletName(String test) {
        return test.matches(VALIDATION_REGEX)
                && test.length() <= MAX_LENGTH
                && !containsCommandDelimiters(test);
    }

    private static boolean containsCommandDelimiters(String test) {
        for (String delimiter : INVALID_DELIMITERS) {
            if (test.contains(delimiter)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof OutletName)) {
            return false;
        }

        OutletName otherOutletName = (OutletName) other;
        return value.equals(otherOutletName.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
