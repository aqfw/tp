package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.tag.TagCombo;

/**
 * A utility class containing a list of {@code TagCombos} objects to be used in tests.
 */
public class TypicalTagCombos {
    public static final TagCombo ML_DEV = new TagComboBuilder()
            .withName("ml dev")
            .withTags("ml", "python")
            .build();

    public static final TagCombo BACKEND_JAVA_DEV = new TagComboBuilder()
            .withName("backend java dev")
            .withTags("java", "backend")
            .build();

    public static final TagCombo ADMIN_ASSISTANT = new TagComboBuilder()
            .withName("admin assistant")
            .withTags("excel", "communication")
            .build();

    /**
     * Returns an {@code AddressBook} with all the typical tagcombos.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (TagCombo tagCombo : getTypicalTagCombos()) {
            ab.addTagCombo(tagCombo);
        }
        return ab;
    }

    public static List<TagCombo> getTypicalTagCombos() {
        return new ArrayList<>(Arrays.asList(ML_DEV, BACKEND_JAVA_DEV, ADMIN_ASSISTANT));
    }
}
