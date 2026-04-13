package seedu.address.model.person.exceptions;

import static seedu.address.model.person.UniquePersonList.MAX_LEN_PERSON_LIST;

/**
 * Signals that performing an operation will exceed the maximum limit for {@code UniquePersonList}.
 */
public class ExceededPersonListCapacityException extends RuntimeException {
    /**
     * Constructor for {@code ExceededPersonListCapacityException}
     */
    public ExceededPersonListCapacityException() {
        super("The maximum capacity of the candidate book is " + MAX_LEN_PERSON_LIST + "! Delete some candidates"
                + " before performing this command again!");
    }
}
