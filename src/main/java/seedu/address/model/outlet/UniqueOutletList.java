package seedu.address.model.outlet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.model.outlet.exceptions.DuplicateOutletException;
import seedu.address.model.outlet.exceptions.OutletNotFoundException;

/**
 * A list of outlets that enforces uniqueness between its elements and does not allow nulls.
 * An outlet is considered unique by comparing using {@code Outlet#isSameOutlet(Outlet)}.
 */
public class UniqueOutletList implements Iterable<Outlet> {

    private final ObservableList<Outlet> internalList = FXCollections.observableArrayList();
    private final ObservableList<Outlet> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent outlet as the given argument.
     */
    public boolean contains(Outlet toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameOutlet);
    }

    /**
     * Adds an outlet to the list.
     * The outlet must not already exist in the list.
     */
    public void add(Outlet toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateOutletException();
        }
        internalList.add(toAdd);
    }

    /**
     * Adds an outlet to the list at the specified index.
     * The outlet must not already exist in the list.
     */
    public void addAtIndex(Outlet toAdd, Index index) {
        requireAllNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateOutletException();
        }
        internalList.add(index.getZeroBased(), toAdd);
    }

    /**
     * Replaces the outlet {@code target} in the list with {@code editedOutlet}.
     * {@code target} must exist in the list.
     * The outlet identity of {@code editedOutlet} must not be the same as another existing outlet in the list.
     */
    public void setOutlet(Outlet target, Outlet editedOutlet) {
        requireAllNonNull(target, editedOutlet);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new OutletNotFoundException();
        }

        if (!target.isSameOutlet(editedOutlet) && contains(editedOutlet)) {
            throw new DuplicateOutletException();
        }

        internalList.set(index, editedOutlet);
    }

    /**
     * Removes the equivalent outlet from the list.
     * The outlet must exist in the list.
     */
    public void remove(Outlet toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new OutletNotFoundException();
        }
    }

    public void setOutlets(UniqueOutletList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code outlets}.
     * {@code outlets} must not contain duplicate outlets.
     */
    public void setOutlets(List<Outlet> outlets) {
        requireAllNonNull(outlets);
        if (!outletsAreUnique(outlets)) {
            throw new DuplicateOutletException();
        }

        internalList.setAll(outlets);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Outlet> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Outlet> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueOutletList)) {
            return false;
        }

        UniqueOutletList otherUniqueOutletList = (UniqueOutletList) other;
        return internalList.equals(otherUniqueOutletList.internalList);
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
     * Returns true if {@code outlets} contains only unique outlets.
     */
    private boolean outletsAreUnique(List<Outlet> outlets) {
        for (int i = 0; i < outlets.size() - 1; i++) {
            for (int j = i + 1; j < outlets.size(); j++) {
                if (outlets.get(i).isSameOutlet(outlets.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
