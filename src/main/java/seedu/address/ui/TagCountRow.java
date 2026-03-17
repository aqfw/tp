package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * A UI component that displays a {@Code Tag} alongside its corresponding frequency in the address book.
 */
public class TagCountRow extends UiPart<Region> {
    private static final String FXML = "TagCountRow.fxml";

    @FXML
    private Label tagName;
    @FXML
    private Label tagCount;

    /**
     * Constructs a row with a {@Code Tag} and its corresponding frequency in the addressbook.
     */
    public TagCountRow(String tagName, int tagCount) {
        super(FXML);

        this.tagName.setText(tagName);
        this.tagCount.setText(tagCount + "");
    }
}
