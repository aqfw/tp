package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteOutletCommand;

public class DeleteOutletCommandParserTest {

    private final DeleteOutletCommandParser parser = new DeleteOutletCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteOutletCommand() {
        assertParseSuccess(parser, "1", new DeleteOutletCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteOutletCommand.MESSAGE_USAGE));
    }
}
