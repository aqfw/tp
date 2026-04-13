package seedu.address.model.outlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class OutletTest {

    private static final Outlet OUTLET_ALPHA = new Outlet(
            new OutletName("TechCo"),
            new OutletAddress("Raffles Place"),
            new OutletPostalCode("048623"));

    private static final Outlet OUTLET_ALPHA_EDITED = new Outlet(
            new OutletName("TechBrc2"),
            new OutletAddress("Raffles Place"),
            new OutletPostalCode("048623"));

    private static final Outlet OUTLET_BETA = new Outlet(
            new OutletName("FinServ"),
            new OutletAddress("Marina Bay"),
            new OutletPostalCode("018956"));

    private static final Outlet OUTLET_ALPHA_DIFF_POSTAL = new Outlet(
            new OutletName("TechCo"),
            new OutletAddress("Raffles Place"),
            new OutletPostalCode("018956"));

    private static final Outlet OUTLET_ALPHA_DIFF_ADDRESS = new Outlet(
            new OutletName("TechCo"),
            new OutletAddress("Marina Bay"),
            new OutletPostalCode("048623"));

    @Test
    public void isSameOutlet() {
        // same object
        assertTrue(OUTLET_ALPHA.isSameOutlet(OUTLET_ALPHA));

        // null
        assertFalse(OUTLET_ALPHA.isSameOutlet(null));

        // same location, different name
        assertFalse(OUTLET_ALPHA.isSameOutlet(OUTLET_ALPHA_EDITED));

        // different location
        assertFalse(OUTLET_ALPHA.isSameOutlet(OUTLET_BETA));

        // same address but different postal code
        assertFalse(OUTLET_ALPHA.isSameOutlet(OUTLET_ALPHA_DIFF_POSTAL));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Outlet outletAlphaCopy = new Outlet(new OutletName("TechCo"), new OutletAddress("Raffles Place"),
                new OutletPostalCode("048623"));
        assertTrue(OUTLET_ALPHA.equals(outletAlphaCopy));

        // same object -> returns true
        assertTrue(OUTLET_ALPHA.equals(OUTLET_ALPHA));

        // null -> returns false
        assertFalse(OUTLET_ALPHA.equals(null));

        // different type -> returns false
        assertFalse(OUTLET_ALPHA.equals(5));

        // different outlet -> returns false
        assertFalse(OUTLET_ALPHA.equals(OUTLET_BETA));

        // same name but different address -> returns false
        assertFalse(OUTLET_ALPHA.equals(OUTLET_ALPHA_DIFF_ADDRESS));

        // same name and address but different postal code -> returns false
        assertFalse(OUTLET_ALPHA.equals(OUTLET_ALPHA_DIFF_POSTAL));
    }

    @Test
    public void hashCodeMethod() {
        Outlet outletAlphaCopy = new Outlet(new OutletName("TechCo"), new OutletAddress("Raffles Place"),
                new OutletPostalCode("048623"));

        assertEquals(OUTLET_ALPHA.hashCode(), outletAlphaCopy.hashCode());
        assertNotEquals(OUTLET_ALPHA.hashCode(), OUTLET_BETA.hashCode());
    }
}
