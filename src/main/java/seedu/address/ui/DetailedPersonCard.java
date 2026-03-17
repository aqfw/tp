package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays all information of a {@code Person}.
 */
public class DetailedPersonCard extends UiPart<Region> {
    private static final String FXML = "DetailedPersonCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label postalCode;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags_detailed_card;

    /**
     * Creates a {@code DetailedPersonCode} with the given {@code Person} and index to display.
     */
    public DetailedPersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText("Candidate #" + displayedIndex);
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        postalCode.setText(person.getPostalCode().value);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags_detailed_card.getChildren().add(new Label(tag.tagName)));
    }
}
