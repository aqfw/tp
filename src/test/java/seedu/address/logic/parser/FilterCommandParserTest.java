package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalTagCombos.ML_DEV;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.tag.Tag;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "filter", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FilterCommand expectedFilterCommand = new FilterCommand(Set.of(new Tag("java"), new Tag("python")),
                Set.of());

        assertParseSuccess(parser, " t/java t/python", expectedFilterCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n t/python \n t/java  \t", expectedFilterCommand);
    }

    @Test
    public void parse_validTagSetAndTagComboNames_returnsFindCommand() {
        FilterCommand expectedFilterCommand = new FilterCommand(Set.of(new Tag("java"), new Tag("python")),
                Set.of(ML_DEV.getName()));

        assertParseSuccess(parser, " t/java t/python tc/ml dev", expectedFilterCommand);
    }

    @Test
    public void parse_validTagComboNameEmptyTagSet_returnsFindCommand() {
        FilterCommand expectedFilterCommand = new FilterCommand(Set.of(), Set.of(ML_DEV.getName()));

        assertParseSuccess(parser, " tc/ml dev", expectedFilterCommand);
    }

    @Test
    public void parse_noTagPresent_throwsParseException() {
        assertParseFailure(parser, " n/John p/80129481", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }
}
