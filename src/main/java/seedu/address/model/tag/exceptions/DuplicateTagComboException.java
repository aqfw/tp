package seedu.address.model.tag.exceptions;

/**
 * An exception that is thrown when trying to add a TagCombo that already exists.
 */
public class DuplicateTagComboException extends RuntimeException {
    public DuplicateTagComboException() {
        super("Operation would result in duplicate TagCombos!");
    }

    public DuplicateTagComboException(String message) {
        super(message);
    }
}
