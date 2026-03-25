package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnassignOutletCommand;

public class UnassignOutletCommandParserTest {

    private final UnassignOutletCommandParser parser = new UnassignOutletCommandParser();

    @Test
    public void parse_validArgs_returnsUnassignOutletCommand() {
        assertParseSuccess(parser, "1", new UnassignOutletCommand(INDEX_FIRST_ENTRY));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignOutletCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignOutletCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignOutletCommand.MESSAGE_USAGE));
    }
}

