package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.outlet.Outlet;

/**
 * A UI component that displays information of an {@code Outlet}.
 */
public class OutletCard extends UiPart<Region> {
    private static final String FXML = "OutletListCard.fxml";

    public final Outlet outlet;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label address;
    @FXML
    private Label postalCode;

    /**
     * Creates an {@code OutletCard} with the given {@code Outlet} and index to display.
     */
    public OutletCard(Outlet outlet, int displayedIndex) {
        super(FXML);
        this.outlet = outlet;
        id.setText(displayedIndex + ". ");
        name.setText(outlet.getOutletName().value);
        address.setText(outlet.getOutletAddress().value);
        postalCode.setText(outlet.getPostalCode().value);
    }
}
