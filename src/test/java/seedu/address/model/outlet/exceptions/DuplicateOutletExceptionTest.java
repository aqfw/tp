package seedu.address.model.outlet.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DuplicateOutletExceptionTest {

    @Test
    public void constructor_withMessage_setsMessage() {
        String message = "duplicate message";
        DuplicateOutletException exception = new DuplicateOutletException(message);

        assertEquals(message, exception.getMessage());
    }
}
