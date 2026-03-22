package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteTagComboCommand;

public class DeleteTagComboCommandParserTest {
    private DeleteTagComboCommandParser parser = new DeleteTagComboCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteTagComboCommandParser() {
        assertParseSuccess(parser, "1", new DeleteTagComboCommand(INDEX_FIRST_ENTRY));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTagComboCommand.MESSAGE_USAGE));
    }
}
