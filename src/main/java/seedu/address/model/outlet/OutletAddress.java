package seedu.address.model.outlet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Outlet's address.
 * Guarantees: immutable; is valid as declared in {@link #isValidOutletAddress(String)}
 */
public class OutletAddress {

    public static final int MAX_LENGTH = 18;
    public static final String MESSAGE_CONSTRAINTS = "Outlet addresses should not be blank, should be at most "
            + MAX_LENGTH + " characters long, and should not contain command delimiters such as n/, a/, or pc/.";
    public static final String VALIDATION_REGEX = "[^\\s].*";
    private static final String[] INVALID_DELIMITERS = {"n/", "a/", "pc/"};

    public final String value;

    /**
     * Constructs an {@code OutletAddress}.
     *
     * @param outletAddress A valid outlet address.
     */
    public OutletAddress(String outletAddress) {
        requireNonNull(outletAddress);
        checkArgument(isValidOutletAddress(outletAddress), MESSAGE_CONSTRAINTS);
        value = outletAddress;
    }

    /**
     * Returns true if a given string is a valid outlet address.
     */
    public static boolean isValidOutletAddress(String test) {
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

        if (!(other instanceof OutletAddress)) {
            return false;
        }

        OutletAddress otherOutletAddress = (OutletAddress) other;
        return value.equals(otherOutletAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
