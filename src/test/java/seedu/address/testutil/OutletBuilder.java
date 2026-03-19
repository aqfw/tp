package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_ADDRESS_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_NAME_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_POSTAL_CODE_ALPHA;

import seedu.address.model.outlet.Outlet;
import seedu.address.model.outlet.OutletAddress;
import seedu.address.model.outlet.OutletName;
import seedu.address.model.outlet.OutletPostalCode;

/**
 * A utility class to help with building Outlet objects.
 */
public class OutletBuilder {
    public static final String DEFAULT_NAME = VALID_OUTLET_NAME_ALPHA;
    public static final String DEFAULT_ADDRESS = VALID_OUTLET_ADDRESS_ALPHA;
    public static final String DEFAULT_POSTAL_CODE = VALID_OUTLET_POSTAL_CODE_ALPHA;

    private OutletName name;
    private OutletAddress address;
    private OutletPostalCode postalCode;

    /**
     * Creates a {@code OutletBuilder} with default details.
     */
    public OutletBuilder() {
        name = new OutletName(DEFAULT_NAME);
        address = new OutletAddress(DEFAULT_ADDRESS);
        postalCode = new OutletPostalCode(DEFAULT_POSTAL_CODE);
    }

    /**
     * Initializes the OutletBuilder with the data of {@code outletToCopy}.
     */
    public OutletBuilder(Outlet outletToCopy) {
        name = outletToCopy.getOutletName();
        address = outletToCopy.getOutletAddress();
        postalCode = outletToCopy.getPostalCode();
    }

    /**
     * Sets the {@code OutletName} of the {@code Outlet} that we are building.
     */
    public OutletBuilder withName(String name) {
        this.name = new OutletName(name);
        return this;
    }

    /**
     * Sets the {@code OutletAddress} of the {@code Outlet} that we are building.
     */
    public OutletBuilder withAddress(String address) {
        this.address = new OutletAddress(address);
        return this;
    }

    /**
     * Sets the {@code OutletPostalCode} of the {@code Outlet} that we are building.
     */
    public OutletBuilder withPostalCode(String postalCode) {
        this.postalCode = new OutletPostalCode(postalCode);
        return this;
    }

    public Outlet build() {
        return new Outlet(name, address, postalCode);
    }
}
