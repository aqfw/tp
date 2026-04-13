package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.commands.EditCommand.RIGHT_PANE_HEADER;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_ENTRY;
import static seedu.address.testutil.TypicalIndexes.SET_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.SET_SECOND_ENTRY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.PersonContent;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        model.resetFilteredPersonList();
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(SET_FIRST_ENTRY, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                1) + "\n" + "Person 1 edited: " + Messages.format(editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel,
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(editedPerson, RIGHT_PANE_HEADER)));
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        List<Index> setLastPerson = List.of(indexLastPerson);
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(setLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, 1) + "\n" + "Person 1 edited: "
                        + Messages.format(editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel,
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(editedPerson, RIGHT_PANE_HEADER)));
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(SET_FIRST_ENTRY, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                1) + "\n" + "Person 1 edited: " + Messages.format(editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel,
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(editedPerson, RIGHT_PANE_HEADER)));
    }

    @Test
    public void execute_filteredList_success() throws CommandException {
        model.resetFilteredPersonList();
        showPersonAtIndex(model, INDEX_FIRST_ENTRY);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(SET_FIRST_ENTRY,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, 1) + "\n" + "Person 1 "
                + "edited: " + Messages.format(editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel,
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(editedPerson, RIGHT_PANE_HEADER)));
    }

    @Test
    public void execute_editMultipleCandidates_success() {
        List<Index> listMultipleIndexes = List.of(INDEX_FIRST_ENTRY, INDEX_SECOND_ENTRY);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(listMultipleIndexes, descriptor);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());
        PersonBuilder firstPersonInList = new PersonBuilder(firstPerson);
        Person editedFirstPerson = firstPersonInList.withName(VALID_NAME_BOB).withTags(VALID_TAG_HUSBAND).build();

        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_ENTRY.getZeroBased());
        PersonBuilder secondPersonInList = new PersonBuilder(secondPerson);
        Person editedSecondPerson = secondPersonInList.withName(VALID_NAME_BOB).withTags(VALID_TAG_HUSBAND).build();

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, 2);
        expectedMessage += "\n" + "Person 1 edited: " + Messages.format(editedFirstPerson);
        expectedMessage += "\n" + "Person 2 edited: " + Messages.format(editedSecondPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedFirstPerson);
        expectedModel.setPerson(secondPerson, editedSecondPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel,
                UiAction.UPDATE_RIGHT_PANE, Optional.of(new PersonContent(editedFirstPerson, RIGHT_PANE_HEADER)));
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(SET_SECOND_ENTRY, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_multipleDuplicatePersonUnfilteredList_failure() {
        Person existingPerson = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(existingPerson).build();
        List<Index> setMultipleIndexes = List.of(INDEX_SECOND_ENTRY, INDEX_THIRD_ENTRY);
        EditCommand editCommand = new EditCommand(setMultipleIndexes, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_multipleEditDuplicateFields_failure() {
        List<Index> setMultipleIndexes = List.of(INDEX_FIRST_ENTRY, INDEX_SECOND_ENTRY);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(setMultipleIndexes, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_ENTRY);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_ENTRY.getZeroBased());
        EditCommand editCommand = new EditCommand(SET_FIRST_ENTRY,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        List<Index> outOfBoundSet = List.of(outOfBoundIndex);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundSet, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_ENTRY);
        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        List<Index> outOfBoundSet = List.of(outOfBoundIndex);

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundSet,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


    @Test
    public void undo_editCommand_success() throws CommandException {
        model.resetFilteredPersonList();
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(SET_FIRST_ENTRY, descriptor);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        editCommand.execute(model);
        assertFalse(model.hasPerson(personToEdit));
        assertTrue(model.hasPerson(editedPerson));
        model.recordCommand(editCommand);

        model.undo();
        assertTrue(model.hasPerson(personToEdit));
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_editCommand_success() throws CommandException {
        model.resetFilteredPersonList();
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(SET_FIRST_ENTRY, descriptor);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        editCommand.execute(model);
        expectedModel.setPerson(personToEdit, editedPerson);
        model.recordCommand(editCommand);

        model.undo();
        model.redo();
        assertEquals(expectedModel, model);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(SET_FIRST_ENTRY, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(SET_SECOND_ENTRY, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(SET_FIRST_ENTRY, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        List<Index> set = List.of(index);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(set, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{indexes=" + set + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
