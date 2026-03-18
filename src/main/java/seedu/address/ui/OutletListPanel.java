package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.outlet.Outlet;

/**
 * Panel containing the list of outlets.
 */
public class OutletListPanel extends UiPart<Region> {
    private static final String FXML = "OutletListPanel.fxml";

    @FXML
    private ListView<Outlet> outletListView;

    /**
     * Creates a {@code OutletListPanel} with the given {@code ObservableList}.
     */
    public OutletListPanel(ObservableList<Outlet> outletList) {
        super(FXML);
        outletListView.setItems(outletList);
        outletListView.setCellFactory(listView -> new OutletListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of an {@code Outlet} using an {@code OutletCard}.
     */
    class OutletListViewCell extends ListCell<Outlet> {
        @Override
        protected void updateItem(Outlet outlet, boolean empty) {
            super.updateItem(outlet, empty);

            if (empty || outlet == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new OutletCard(outlet, getIndex() + 1).getRoot());
            }
        }
    }
}
