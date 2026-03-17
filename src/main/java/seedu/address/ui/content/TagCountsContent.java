package seedu.address.ui.content;


import javafx.scene.layout.VBox;
import seedu.address.model.tag.TagCounter;
import seedu.address.ui.TagCountsPanel;

/**
 * A class that renders the tag counts to the right pane.
 */
public class TagCountsContent implements RightPaneContent {
    private final TagCounter tagCounter;

    public TagCountsContent(TagCounter tagCounter) {
        this.tagCounter = tagCounter;
    }

    @Override
    public void render(VBox contentPlaceholder) {
        TagCountsPanel tagCountsPanel = new TagCountsPanel(tagCounter);
        contentPlaceholder.getChildren().setAll(tagCountsPanel.getRoot());
    }

    @Override
    public int hashCode() {
        return tagCounter.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagCountsContent)) {
            return false;
        }

        TagCountsContent otherTagCounts = (TagCountsContent) other;
        return tagCounter.equals(otherTagCounts.tagCounter);
    }
}
