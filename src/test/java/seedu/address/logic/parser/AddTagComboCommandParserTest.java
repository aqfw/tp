package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_COMBO_NAME;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_PYTHON;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_COMBO_NAME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PYTHON;
import static seedu.address.logic.parser.AddTagComboCommandParser.INSUFFICIENT_TAGS_ERROR_MESSAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.model.tag.TagComboName.MESSAGE_CONSTRAINTS;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddTagComboCommand;
import seedu.address.model.tag.TagCombo;
import seedu.address.testutil.TagComboBuilder;

public class AddTagComboCommandParserTest {
    private static final String INVALID_LENGTH_TAG_COMBO_NAME = "this is a tag combo name that is too long";
    private AddTagComboCommandParser parser = new AddTagComboCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        TagCombo expectedTagCombo = new TagComboBuilder().withName(VALID_TAG_COMBO_NAME)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();

        assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_TAG_COMBO_NAME + TAG_DESC_FRIEND
                        + TAG_DESC_HUSBAND, new AddTagComboCommand(expectedTagCombo));

        expectedTagCombo = new TagComboBuilder().withName(VALID_TAG_COMBO_NAME)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND, VALID_TAG_PYTHON)
                .build();

        // success with > 2 tags
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + VALID_TAG_COMBO_NAME + TAG_DESC_FRIEND
                + TAG_DESC_HUSBAND + TAG_DESC_PYTHON, new AddTagComboCommand(expectedTagCombo));
    }

    @Test
    public void parse_nameNotPresent_throwsParseException() {
        assertParseFailure(parser, PREAMBLE_WHITESPACE + TAG_DESC_FRIEND + TAG_DESC_HUSBAND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagComboCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_tagPrefixNotPresent_throwsParseException() {
        assertParseFailure(parser, PREAMBLE_WHITESPACE + VALID_TAG_COMBO_NAME,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagComboCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_tagPrefixNotDuplicated_throwsParseException() {
        assertParseFailure(parser, PREAMBLE_WHITESPACE + VALID_TAG_COMBO_NAME + TAG_DESC_FRIEND,
                Messages.getErrorMessageForNonDuplicatePrefixes(PREFIX_TAG));
    }

    @Test
    public void parse_tagTooLong_throwsParseException() {
        assertParseFailure(parser, PREAMBLE_WHITESPACE + INVALID_LENGTH_TAG_COMBO_NAME + TAG_DESC_FRIEND
                        + TAG_DESC_HUSBAND, MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nonAlphaNumeric_throwsParseException() {
        assertParseFailure(parser, PREAMBLE_WHITESPACE + INVALID_TAG_COMBO_NAME + TAG_DESC_FRIEND
                        + TAG_DESC_HUSBAND, MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicateTags_throwsParseException() {
        assertParseFailure(parser, PREAMBLE_WHITESPACE + VALID_TAG_COMBO_NAME
                        + TAG_DESC_FRIEND + TAG_DESC_FRIEND, INSUFFICIENT_TAGS_ERROR_MESSAGE);
    }
}
