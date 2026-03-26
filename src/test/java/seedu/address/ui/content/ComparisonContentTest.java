package seedu.address.ui.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;

import org.junit.jupiter.api.Test;

public class ComparisonContentTest {

    // Tests for equals (self-explanatory test method titles)

    @Test
    public void equals_sameObject_returnsTrue() {
        ComparisonContent content = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        assertTrue(content.equals(content));
    }

    @Test
    public void equals_differentFirstPerson_returnsFalse() {
        ComparisonContent first = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        ComparisonContent second = new ComparisonContent(CARL, "Candidate #1", BOB, "Candidate #2");
        assertFalse(first.equals(second));
    }

    @Test
    public void equals_differentSecondPerson_returnsFalse() {
        ComparisonContent first = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        ComparisonContent second = new ComparisonContent(ALICE, "Candidate #1", CARL, "Candidate #2");
        assertFalse(first.equals(second));
    }

    @Test
    public void equals_differentFirstHeader_returnsFalse() {
        ComparisonContent first = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        ComparisonContent second = new ComparisonContent(ALICE, "Different Header", BOB, "Candidate #2");
        assertFalse(first.equals(second));
    }

    @Test
    public void equals_differentSecondHeader_returnsFalse() {
        ComparisonContent first = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        ComparisonContent second = new ComparisonContent(ALICE, "Candidate #1", BOB, "Different Header");
        assertFalse(first.equals(second));
    }

    @Test
    public void equals_swappedPersons_returnsFalse() {
        ComparisonContent first = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        ComparisonContent second = new ComparisonContent(BOB, "Candidate #1", ALICE, "Candidate #2");
        assertFalse(first.equals(second));
    }

    @Test
    public void equals_swappedHeaders_returnsFalse() {
        ComparisonContent first = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        ComparisonContent second = new ComparisonContent(ALICE, "Candidate #2", BOB, "Candidate #1");
        assertFalse(first.equals(second));
    }

    @Test
    public void equals_null_returnsFalse() {
        ComparisonContent content = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        assertFalse(content.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        ComparisonContent content = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        assertFalse(content.equals("not a ComparisonContent"));
    }

    // Tests for hashcode

    @Test
    public void hashCode_sameValues_sameHashCode() {
        ComparisonContent first = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        ComparisonContent second = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void hashCode_differentPersons_differentHashCode() {
        ComparisonContent first = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        ComparisonContent second = new ComparisonContent(ALICE, "Candidate #1", CARL, "Candidate #2");
        assertNotEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void hashCode_differentHeaders_differentHashCode() {
        ComparisonContent first = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        ComparisonContent second = new ComparisonContent(ALICE, "A Very Weird Header", BOB, "Candidate #2");
        assertNotEquals(first.hashCode(), second.hashCode());
    }

    // This one is to test that from a prior call, the same hashCode is hashed to the same Person obbject.
    @Test
    public void hashCode_consistentAcrossMultipleCalls_sameHashCode() {
        ComparisonContent content = new ComparisonContent(ALICE, "Candidate #1", BOB, "Candidate #2");
        assertEquals(content.hashCode(), content.hashCode());
    }
}
