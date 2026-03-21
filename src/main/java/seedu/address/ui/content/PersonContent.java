package seedu.address.ui.content;

import java.util.Objects;

import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;
import seedu.address.ui.DetailedPersonCard;

/**
 * A class that renders the details of a person to the right panel.
 */
public class PersonContent implements RightPaneContent {
    private final String header;
    private final Person person;

    /**
     * Constructs a {@code PersonContent} with a {@code Person} and {@code header}.
     */
    public PersonContent(Person person, String header) {
        this.person = person;
        this.header = header;
    }

    @Override
    public void render(VBox contentPlaceholder) {
        DetailedPersonCard detailedCard = new DetailedPersonCard(person, header);
        contentPlaceholder.getChildren().setAll(detailedCard.getRoot());
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, person);
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
        return person.equals(otherPersonContent.person)
                && header.equals(otherPersonContent.header);
    }
}
