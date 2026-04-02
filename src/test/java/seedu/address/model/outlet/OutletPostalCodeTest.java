package seedu.address.model.outlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class OutletPostalCodeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new OutletPostalCode(null));
    }

    @Test
    public void constructor_invalidPostalCode_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new OutletPostalCode(""));
    }

    @Test
    public void isValidPostalCode() {
        assertThrows(NullPointerException.class, () -> OutletPostalCode.isValidPostalCode(null));

        assertFalse(OutletPostalCode.isValidPostalCode(""));
        assertFalse(OutletPostalCode.isValidPostalCode("12345"));
        assertFalse(OutletPostalCode.isValidPostalCode("1234567"));
        assertFalse(OutletPostalCode.isValidPostalCode("12A456"));

        assertTrue(OutletPostalCode.isValidPostalCode("000000"));
        assertTrue(OutletPostalCode.isValidPostalCode("048623"));
    }

    @Test
    public void equals() {
        OutletPostalCode firstPostalCode = new OutletPostalCode("048623");
        OutletPostalCode secondPostalCode = new OutletPostalCode("018956");

        assertTrue(firstPostalCode.equals(firstPostalCode));
        assertTrue(firstPostalCode.equals(new OutletPostalCode("048623")));
        assertFalse(firstPostalCode.equals(null));
        assertFalse(firstPostalCode.equals(1));
        assertFalse(firstPostalCode.equals(secondPostalCode));
    }

    @Test
    public void hashCodeMethod() {
        OutletPostalCode firstPostalCode = new OutletPostalCode("048623");
        OutletPostalCode secondPostalCode = new OutletPostalCode("018956");

        assertEquals(firstPostalCode.hashCode(), new OutletPostalCode("048623").hashCode());
        assertNotEquals(firstPostalCode.hashCode(), secondPostalCode.hashCode());
    }
}
