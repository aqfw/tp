package seedu.address.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagCounter;

/**
 * A UI component that displays all {@code Tag} alongside its corresponding frequency in the address book.
 */
public class TagCountsPanel extends UiPart<Region> {
    private static final String FXML = "TagCountsPanel.fxml";

    @FXML
    private VBox tagCountsPanel;

    private final TagCounter tagCounter;

    /**
     * Constructs a Tag Counts Panel, then fills it.
     */
    public TagCountsPanel(TagCounter tagCounter) {
        super(FXML);
        this.tagCounter = tagCounter;
        fillScrollPane();
    }

    /**
     * Fills the ScrollPane with rows for each tag + count.
     */
    private void fillScrollPane() {
        Label header = new Label("TAG COUNTS");
        header.getStyleClass().add("small-index-label");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-padding: 0 0 0 15; -fx-font-size: 18");

        Region firstSpacer = new Region();
        firstSpacer.setPrefHeight(8);

        Region secondSpacer = new Region();
        secondSpacer.setPrefHeight(10);

        tagCountsPanel.getChildren().addAll(firstSpacer, header, secondSpacer);

        LinkedHashMap<Tag, Integer> tagMap = tagCounter.getTagCounter();
        for (Map.Entry<Tag, Integer> entry : tagMap.entrySet()) {
            TagCountRow row = new TagCountRow(entry.getKey().getTagName(), entry.getValue());
            tagCountsPanel.getChildren().add(row.getRoot());
        }
    }
}
