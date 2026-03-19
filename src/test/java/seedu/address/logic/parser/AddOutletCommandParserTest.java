package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_OUTLET_POSTAL_CODE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.OUTLET_ADDRESS_DESC_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.OUTLET_NAME_DESC_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.OUTLET_POSTAL_CODE_DESC_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_ADDRESS_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_NAME_ALPHA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OUTLET_POSTAL_CODE_ALPHA;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddOutletCommand;
import seedu.address.model.outlet.OutletAddress;
import seedu.address.model.outlet.OutletName;
import seedu.address.model.outlet.OutletPostalCode;
import seedu.address.testutil.OutletBuilder;

public class AddOutletCommandParserTest {

    private static final String INVALID_OUTLET_NAME_DESC = " n/   ";
    private final AddOutletCommandParser parser = new AddOutletCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + OUTLET_NAME_DESC_ALPHA + OUTLET_ADDRESS_DESC_ALPHA
                + OUTLET_POSTAL_CODE_DESC_ALPHA,
                new AddOutletCommand(new OutletBuilder().build()));
    }

    @Test
    public void parse_repeatedValues_failure() {
        String validOutletString = OUTLET_NAME_DESC_ALPHA + OUTLET_ADDRESS_DESC_ALPHA + OUTLET_POSTAL_CODE_DESC_ALPHA;

        assertParseFailure(parser, OUTLET_NAME_DESC_ALPHA + validOutletString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
        assertParseFailure(parser, OUTLET_ADDRESS_DESC_ALPHA + validOutletString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
        assertParseFailure(parser, OUTLET_POSTAL_CODE_DESC_ALPHA + validOutletString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_POSTAL_CODE));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddOutletCommand.MESSAGE_USAGE);

        assertParseFailure(parser, VALID_OUTLET_NAME_ALPHA + OUTLET_ADDRESS_DESC_ALPHA + OUTLET_POSTAL_CODE_DESC_ALPHA,
                expectedMessage);
        assertParseFailure(parser, OUTLET_NAME_DESC_ALPHA + VALID_OUTLET_ADDRESS_ALPHA + OUTLET_POSTAL_CODE_DESC_ALPHA,
                expectedMessage);
        assertParseFailure(parser, OUTLET_NAME_DESC_ALPHA + OUTLET_ADDRESS_DESC_ALPHA + VALID_OUTLET_POSTAL_CODE_ALPHA,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_OUTLET_NAME_DESC + OUTLET_ADDRESS_DESC_ALPHA + OUTLET_POSTAL_CODE_DESC_ALPHA,
                OutletName.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, OUTLET_NAME_DESC_ALPHA + INVALID_ADDRESS_DESC + OUTLET_POSTAL_CODE_DESC_ALPHA,
                OutletAddress.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, OUTLET_NAME_DESC_ALPHA + OUTLET_ADDRESS_DESC_ALPHA + INVALID_OUTLET_POSTAL_CODE_DESC,
                OutletPostalCode.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + OUTLET_NAME_DESC_ALPHA + OUTLET_ADDRESS_DESC_ALPHA
                + OUTLET_POSTAL_CODE_DESC_ALPHA,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddOutletCommand.MESSAGE_USAGE));
    }
}
