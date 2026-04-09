package seedu.address.model.outlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class OutletAddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new OutletAddress(null));
    }

    @Test
    public void constructor_invalidOutletAddress_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new OutletAddress(" "));
    }

    @Test
    public void isValidOutletAddress() {
        assertThrows(NullPointerException.class, () -> OutletAddress.isValidOutletAddress(null));

        assertFalse(OutletAddress.isValidOutletAddress(""));
        assertFalse(OutletAddress.isValidOutletAddress("   "));
        assertFalse(OutletAddress.isValidOutletAddress("123456789012345678901234567890123456"));
        assertFalse(OutletAddress.isValidOutletAddress("Something Street a/Something"));

        assertTrue(OutletAddress.isValidOutletAddress("Raffles Place"));
        assertTrue(OutletAddress.isValidOutletAddress("18 Cross St, #08-01"));
        assertTrue(OutletAddress.isValidOutletAddress("12345678901234567890123456789012345"));
    }

    @Test
    public void equals() {
        OutletAddress firstAddress = new OutletAddress("Raffles Place");
        OutletAddress secondAddress = new OutletAddress("Marina Bay");

        assertTrue(firstAddress.equals(firstAddress));
        assertTrue(firstAddress.equals(new OutletAddress("Raffles Place")));
        assertFalse(firstAddress.equals(null));
        assertFalse(firstAddress.equals(1));
        assertFalse(firstAddress.equals(secondAddress));
    }

    @Test
    public void hashCodeMethod() {
        OutletAddress firstAddress = new OutletAddress("Raffles Place");
        OutletAddress secondAddress = new OutletAddress("Marina Bay");

        assertEquals(firstAddress.hashCode(), new OutletAddress("Raffles Place").hashCode());
        assertNotEquals(firstAddress.hashCode(), secondAddress.hashCode());
    }
}
