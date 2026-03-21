package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.tag.exceptions.DuplicateTagComboException;
import seedu.address.model.tag.exceptions.TagComboNotFoundException;

/**
 * A list of TagCombos that enforces uniqueness between its elements and does not allow nulls.
 * An outlet is considered unique by comparing using {@code Outlet#isSameOutlet(Outlet)}.
 */
public class UniqueTagComboList implements Iterable<TagCombo> {

    private final ObservableList<TagCombo> internalList = FXCollections.observableArrayList();
    private final ObservableList<TagCombo> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent {@code TagCombo}.
     */
    public boolean contains(TagCombo toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameTagCombo);
    }

    /**
     * Adds a TagCombo to the list if no duplicates exist.
     */
    public void add(TagCombo toAdd) {
        requireNonNull(toAdd);
        if (internalList.contains(toAdd)) {
            throw new DuplicateTagComboException("A tag combo with the same tags already exists!");
        }
        internalList.add(toAdd);
    }

    /**
     * Removes the corresponding tagCombo from the list. {@code toRemove} must be non-null and exist in the list.
     */
    public void remove(TagCombo toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new TagComboNotFoundException();
        }
    }

    public void setTagCombos(UniqueTagComboList tagCombos) {
        requireNonNull(tagCombos);
        internalList.setAll(tagCombos.internalList);
    }

    /**
     * Replaces the contents of this list with {@code TagCombo}s.
     */
    public void setTagCombos(List<TagCombo> tagCombos) {
        requireAllNonNull(tagCombos);
        if (!tagCombosAreUnique(tagCombos)) {
            throw new DuplicateTagComboException("There exists duplicate tag combos in the list!");
        }
        internalList.setAll(tagCombos);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<TagCombo> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<TagCombo> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueTagComboList)) {
            return false;
        }

        UniqueTagComboList otherUniquePersonList = (UniqueTagComboList) other;
        return internalList.equals(otherUniquePersonList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if the list does not contain any duplicate {@code TagCombo}s, otherwise false.
     */
    private boolean tagCombosAreUnique(List<TagCombo> tagCombos) {
        for (int i = 0; i < tagCombos.size() - 1; i++) {
            for (int j = i + 1; j < tagCombos.size(); j++) {
                if (tagCombos.get(i).isSameTagCombo(tagCombos.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
