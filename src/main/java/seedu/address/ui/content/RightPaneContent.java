package seedu.address.ui.content;

import javafx.scene.layout.VBox;

/**
 * An interface for classes which have the ability to render content on the right panel of the UI.
 */
public interface RightPaneContent {

    /**
     * Renders the content in the VBox.
     * @param contentPlaceholder
     */
    public void render(VBox contentPlaceholder);
}
