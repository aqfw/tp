package seedu.address.ui.content;

import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;
import seedu.address.ui.DetailedPersonCard;

/**
 * A class that renders the details of a person to the right panel.
 */
public class PersonContent implements RightPaneContent {
    private final Person person;

    public PersonContent(Person person) {
        this.person = person;
    }

    @Override
    public void render(VBox contentPlaceholder) {
        DetailedPersonCard detailedCard = new DetailedPersonCard(person, 1);
        contentPlaceholder.getChildren().setAll(detailedCard.getRoot());
    }

    @Override
    public int hashCode() {
        return person.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonContent)) {
            return false;
        }

        PersonContent otherPersonContent = (PersonContent) other;
        return person.equals(otherPersonContent.person);
    }
}
