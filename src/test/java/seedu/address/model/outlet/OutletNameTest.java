package seedu.address.model.outlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class OutletNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new OutletName(null));
    }

    @Test
    public void constructor_invalidOutletName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new OutletName(" "));
    }

    @Test
    public void isValidOutletName() {
        assertThrows(NullPointerException.class, () -> OutletName.isValidOutletName(null));

        assertFalse(OutletName.isValidOutletName(""));
        assertFalse(OutletName.isValidOutletName("   "));
        assertFalse(OutletName.isValidOutletName("ABCDEFGHIJKLMNOPQRSTUVWXYZ1"));
        assertFalse(OutletName.isValidOutletName("Something n/Something"));

        assertTrue(OutletName.isValidOutletName("TechCo"));
        assertTrue(OutletName.isValidOutletName("FinServ Marina"));
        assertTrue(OutletName.isValidOutletName("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }

    @Test
    public void equals() {
        OutletName firstName = new OutletName("TechCo");
        OutletName secondName = new OutletName("FinServ");

        assertTrue(firstName.equals(firstName));
        assertTrue(firstName.equals(new OutletName("TechCo")));
        assertFalse(firstName.equals(null));
        assertFalse(firstName.equals(1));
        assertFalse(firstName.equals(secondName));
    }

    @Test
    public void hashCodeMethod() {
        OutletName firstName = new OutletName("TechCo");
        OutletName secondName = new OutletName("FinServ");

        assertEquals(firstName.hashCode(), new OutletName("TechCo").hashCode());
        assertNotEquals(firstName.hashCode(), secondName.hashCode());
    }
}
