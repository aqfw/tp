package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.model.tag.TagCombo;

/**
 * A UI component that displays information of an {@code TagCombo}.
 */
public class TagComboCard extends UiPart<Region> {
    private static final String FXML = "TagComboListCard.fxml";

    private final TagCombo tagCombo;

    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private FlowPane tagsCardTagCombo;

    /**
     * Creates a {@code TagComboCard} with the given {@code TagCombo} and index to display.
     */
    public TagComboCard(TagCombo tagCombo, int displayedIndex) {
        super(FXML);
        this.tagCombo = tagCombo;
        id.setText(displayedIndex + ". ");
        name.setText(tagCombo.getName().name);
        tagCombo.getTagSet().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tagsCardTagCombo.getChildren().add(new Label(tag.tagName)));
    }
}
