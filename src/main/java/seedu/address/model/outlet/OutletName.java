package seedu.address.model.outlet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Outlet's name.
 * Guarantees: immutable; is valid as declared in {@link #isValidOutletName(String)}
 */
public class OutletName {

    public static final int MAX_LENGTH = 10;
    public static final String MESSAGE_CONSTRAINTS = "Outlet names should not be blank, should be at most "
            + MAX_LENGTH + " characters long, should not contain command delimiters such as n/, a/, or pc/, "
            + "and cannot be the reserved word 'unassigned' in any capitalization (e.g., UnAssigned).";
    public static final String VALIDATION_REGEX = "[^\\s].*";
    private static final String RESERVED_NAME = "unassigned";
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
                && !containsCommandDelimiters(test)
                && !test.equalsIgnoreCase(RESERVED_NAME);
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
