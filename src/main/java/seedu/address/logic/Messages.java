package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.person.Person;
import seedu.address.model.tag.TagCombo;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is out of range. "
            + "Use list to view valid person indices.";
    public static final String MESSAGE_INVALID_OUTLET_DISPLAYED_INDEX = "The outlet index provided is out of range. "
            + "Use outlet list to view valid outlet indices.";
    public static final String MESSAGE_INVALID_TAG_COMBO_DISPLAYED_INDEX =
            "The tag combo index provided is out of range. "
            + "Use listtagcombo to view valid tag combo indices.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_REQUIRE_DUPLICATE_FIELDS =
            "The following fields require multiple values: ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Returns an error message indicating the duplicate prefixes required.
     */
    public static String getErrorMessageForNonDuplicatePrefixes(Prefix... nonDuplicatePrefixes) {
        assert nonDuplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(nonDuplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_REQUIRE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Postal Code: ")
                .append(person.getPostalCode())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code outlet} for display to the user.
     */
    public static String format(Outlet outlet) {
        return outlet.getOutletName()
                + "; Address: "
                + outlet.getOutletAddress()
                + "; Postal Code: "
                + outlet.getPostalCode();
    }

    /**
     * Formats the {@code TagCombo} for display to the user.
     */
    public static String format(TagCombo tagCombo) {
        final StringBuilder builder = new StringBuilder();
        builder.append(tagCombo.getName())
                .append("; Tags: ");
        tagCombo.getTagSet().forEach(builder::append);
        return builder.toString();
    }
}
