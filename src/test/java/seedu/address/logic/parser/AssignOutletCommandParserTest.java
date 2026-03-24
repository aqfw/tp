package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AssignOutletCommand;

public class AssignOutletCommandParserTest {

    private final AssignOutletCommandParser parser = new AssignOutletCommandParser();

    @Test
    public void parse_validArgs_returnsAssignOutletCommand() {
        assertParseSuccess(parser, "1 2", new AssignOutletCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignOutletCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "a 2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignOutletCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 b",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignOutletCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 2 3",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignOutletCommand.MESSAGE_USAGE));
    }
}

