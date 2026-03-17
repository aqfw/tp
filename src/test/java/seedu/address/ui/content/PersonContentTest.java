package seedu.address.ui.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

public class PersonContentTest {

    @Test
    public void equals() {
        PersonContent firstPersonContent = new PersonContent(ALICE, "added");
        PersonContent secondPersonContent = new PersonContent(BENSON, "added");
        PersonContent thirdPersonContent = new PersonContent(ALICE, "deleted");

        assertTrue(firstPersonContent.equals(new PersonContent(ALICE, "added")));
        assertFalse(firstPersonContent.equals(secondPersonContent));
        assertTrue(firstPersonContent.equals(firstPersonContent));
        assertFalse(firstPersonContent.equals(thirdPersonContent));
    }

    @Test
    public void hashcode() {
        PersonContent firstPersonContent = new PersonContent(ALICE, "added");
        PersonContent secondPersonContent = new PersonContent(BENSON, "added");
        PersonContent thirdPersonContent = new PersonContent(ALICE, "deleted");

        assertEquals(firstPersonContent.hashCode(), new PersonContent(ALICE, "added").hashCode());
        assertEquals(firstPersonContent.hashCode(), firstPersonContent.hashCode());
        assertNotEquals(firstPersonContent.hashCode(), secondPersonContent.hashCode());
        assertNotEquals(firstPersonContent.hashCode(), thirdPersonContent.hashCode());
    }
}
