package seedu.address.model.outlet;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents an outlet in HireLens.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Outlet {

    private final OutletName outletName;
    private final OutletAddress outletAddress;
    private final OutletPostalCode postalCode;

    /**
     * Every field must be present and not null.
     */
    public Outlet(OutletName outletName, OutletAddress outletAddress, OutletPostalCode postalCode) {
        requireAllNonNull(outletName, outletAddress, postalCode);
        this.outletName = outletName;
        this.outletAddress = outletAddress;
        this.postalCode = postalCode;
    }

    public OutletName getOutletName() {
        return outletName;
    }

    public OutletAddress getOutletAddress() {
        return outletAddress;
    }

    public OutletPostalCode getPostalCode() {
        return postalCode;
    }

    /**
     * Returns true if both outlets have the same identity fields.
     */
    public boolean isSameOutlet(Outlet otherOutlet) {
        if (otherOutlet == this) {
            return true;
        }

        return otherOutlet != null
                && otherOutlet.getOutletName().equals(getOutletName())
                && otherOutlet.getOutletAddress().equals(getOutletAddress())
                && otherOutlet.getPostalCode().equals(getPostalCode());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Outlet)) {
            return false;
        }

        Outlet otherOutlet = (Outlet) other;
        return outletName.equals(otherOutlet.outletName)
                && outletAddress.equals(otherOutlet.outletAddress)
                && postalCode.equals(otherOutlet.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outletName, outletAddress, postalCode);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("outletName", outletName)
                .add("outletAddress", outletAddress)
                .add("postalCode", postalCode)
                .toString();
    }
}
