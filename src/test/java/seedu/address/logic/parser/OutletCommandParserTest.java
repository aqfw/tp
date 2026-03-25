package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.OUTLET_ADDRESS_DESC_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.OUTLET_NAME_DESC_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.OUTLET_POSTAL_CODE_DESC_ALPHA;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ENTRY;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddOutletCommand;
import seedu.address.logic.commands.AssignOutletCommand;
import seedu.address.logic.commands.DeleteOutletCommand;
import seedu.address.logic.commands.ListOutletsCommand;
import seedu.address.logic.commands.UnassignOutletCommand;
import seedu.address.testutil.OutletBuilder;

public class OutletCommandParserTest {

    private final OutletCommandParser parser = new OutletCommandParser();

    @Test
    public void parse_addCommand_success() {
        assertParseSuccess(parser, "add" + OUTLET_NAME_DESC_ALPHA + OUTLET_ADDRESS_DESC_ALPHA
                        + OUTLET_POSTAL_CODE_DESC_ALPHA,
                new AddOutletCommand(new OutletBuilder().build()));
    }

    @Test
    public void parse_deleteCommand_success() {
        assertParseSuccess(parser, "delete 1", new DeleteOutletCommand(INDEX_FIRST_ENTRY));
    }

    @Test
    public void parse_assignCommand_success() {
        assertParseSuccess(parser, "assign 1 2", new AssignOutletCommand(INDEX_FIRST_ENTRY, INDEX_SECOND_ENTRY));
    }

    @Test
    public void parse_unassignCommand_success() {
        assertParseSuccess(parser, "unassign 1", new UnassignOutletCommand(INDEX_FIRST_ENTRY));
    }

    @Test
    public void parse_listCommand_success() {
        assertParseSuccess(parser, "list", new ListOutletsCommand());
    }

    @Test
    public void parse_invalidSubcommand_failure() {
        assertParseFailure(parser, "unknown",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, OutletCommandParser.MESSAGE_USAGE));
    }
}
