package seedu.address.testutil;

import java.util.List;

import seedu.address.commons.core.index.Index;

/**
 * A utility class containing a list of {@code Index} objects to be used in tests.
 */
public class TypicalIndexes {
    public static final Index INDEX_FIRST_ENTRY = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_ENTRY = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_ENTRY = Index.fromOneBased(3);

    public static final List<Index> SET_FIRST_ENTRY = List.of(Index.fromOneBased(1));
    public static final List<Index> SET_SECOND_ENTRY = List.of(Index.fromOneBased(2));
    public static final List<Index> SET_THIRD_ENTRY = List.of(Index.fromOneBased(3));
}
