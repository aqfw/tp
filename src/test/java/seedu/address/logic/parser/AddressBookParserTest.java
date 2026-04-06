package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.SET_FIRST_ENTRY;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddByCsvCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddOutletCommand;
import seedu.address.logic.commands.AddTagComboCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteTagComboCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListOutletsCommand;
import seedu.address.logic.commands.ListTagCombosCommand;
import seedu.address.logic.commands.ListTagsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.OutletBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;
import seedu.address.testutil.TagComboBuilder;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_addcsv() throws Exception {
        // Use a valid CSV file path for the addcsv command
        String csvPath = "src/test/data/AddByCsvParserTest/validCandidates.csv";
        AddByCsvCommand command = (AddByCsvCommand) parser.parseCommand(
                AddByCsvCommand.COMMAND_WORD + " " + csvPath);
        assertTrue(command instanceof AddByCsvCommand);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_ENTRY.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(SET_FIRST_ENTRY, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_listTags() throws Exception {
        assertTrue(parser.parseCommand(ListTagsCommand.COMMAND_WORD) instanceof ListTagsCommand);
        assertTrue(parser.parseCommand(ListTagsCommand.COMMAND_WORD + " 3") instanceof ListTagsCommand);
    }

    @Test
    public void parseCommand_outletList() throws Exception {
        assertTrue(parser.parseCommand("outlet list") instanceof ListOutletsCommand);
    }

    @Test
    public void parseCommand_outletAdd() throws Exception {
        AddOutletCommand command =
                (AddOutletCommand) parser.parseCommand("outlet add n/TechCo a/Raffles Place pc/048623");
        assertEquals(new AddOutletCommand(new OutletBuilder().build()), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_tagComboAdd() throws Exception {
        AddTagComboCommand command =
                (AddTagComboCommand) parser.parseCommand("addtagcombo ml dev t/python t/ml");
        assertEquals(new AddTagComboCommand(new TagComboBuilder().withName("ml dev").withTags("ml", "python")
                .build()), command);
    }

    @Test
    public void parseCommand_listTagCombos() throws Exception {
        assertTrue(parser.parseCommand(ListTagCombosCommand.COMMAND_WORD) instanceof ListTagCombosCommand);
        assertTrue(parser.parseCommand(ListTagCombosCommand.COMMAND_WORD + " 3") instanceof ListTagCombosCommand);
    }

    @Test
    public void parseCommand_deleteTagCombos() throws Exception {
        DeleteTagComboCommand command = (DeleteTagComboCommand) parser.parseCommand(
                DeleteTagComboCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new DeleteTagComboCommand(INDEX_FIRST_ENTRY), command);
    }
}
