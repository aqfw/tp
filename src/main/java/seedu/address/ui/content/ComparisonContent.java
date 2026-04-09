package seedu.address.ui.content;

import java.util.Objects;

import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;
import seedu.address.ui.DetailedPersonCard;

/**
 *  Renders two {@code DetailedPersonCard} instances side-by-side in the right pane,
 *  which implements the need to compare two candidates visually
 */
public class ComparisonContent implements RightPaneContent {

    private static final double CARD_SPACING = 8.0;

    private final Person firstPerson;
    private final Person secondPerson;
    private final String firstHeader;
    private final String secondHeader;

    /**
     * Constructor for {@code ComparisonContent} with two Persons and their display headers
     * @param firstPerson the Person object representing the first person
     * @param firstHeader the first Person object's header string
     * @param secondPerson the Person object representing the second person
     * @param secondHeader the second Person object's header string
     */
    public ComparisonContent(Person firstPerson, String firstHeader, Person secondPerson,
                             String secondHeader) {
        this.firstPerson = firstPerson;
        this.firstHeader = firstHeader;
        this.secondPerson = secondPerson;
        this.secondHeader = secondHeader;
    }

    // renders two VBoxes which both will scale, in a side-by-side HBox configuration.
    @Override
    public void render(VBox contentPlaceholder) {
        DetailedPersonCard firstCard = new DetailedPersonCard(firstPerson, firstHeader);
        DetailedPersonCard secondCard = new DetailedPersonCard(secondPerson, secondHeader);

        VBox firstWrapper = new VBox(firstCard.getRoot());
        VBox secondWrapper = new VBox(secondCard.getRoot());

        VBox.setVgrow(firstWrapper, Priority.ALWAYS);
        VBox.setVgrow(secondWrapper, Priority.ALWAYS);

        SplitPane splitPane = new SplitPane(firstWrapper, secondWrapper);
        splitPane.setDividerPositions(0.5);

        VBox.setVgrow(splitPane, Priority.ALWAYS);
        contentPlaceholder.getChildren().setAll(splitPane);
    }

    @Override
    public int hashCode() {
        int code = Objects.hash(firstPerson, firstHeader, secondPerson, secondHeader);
        return code;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonContent)) {
            return false;
        }

        ComparisonContent otherComparisonContent = (ComparisonContent) other;
        boolean equality;
        equality = firstPerson.equals(otherComparisonContent.firstPerson)
                && firstHeader.equals(otherComparisonContent.firstHeader)
                && secondPerson.equals(otherComparisonContent.secondPerson)
                && secondHeader.equals(otherComparisonContent.secondHeader);
        return equality;
    }

}
