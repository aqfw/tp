package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CompareCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class CompareCommandParserTest {

    private final CompareCommandParser parser = new CompareCommandParser();

    // Test for valid inputs

    //Base case, 1 2 -> compare command object with 1 2
    @Test
    public void parse_validArgs_returnsCompareCommand() throws ParseException {
        CompareCommand result = parser.parse("1 2");
        CompareCommand expected = new CompareCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        assertEquals(expected, result);
    }

    //With added whitespaces -> compare command object with 3 5
    @Test
    public void parse_validArgsWithLeadingAndTrailingSpaces_returnsCompareCommand() throws ParseException {
        CompareCommand result = parser.parse("  3   5  ");
        CompareCommand expected = new CompareCommand(Index.fromOneBased(3), Index.fromOneBased(5));
        assertEquals(expected, result);
    }

    @Test
    public void parse_sameValidIndex_returnsCompareCommand() throws ParseException {
        // Parser does not reject same indices — that is left to CompareCommand.execute()
        CompareCommand result = parser.parse("2 2");
        CompareCommand expected = new CompareCommand(Index.fromOneBased(2), Index.fromOneBased(2));
        assertEquals(expected, result);
    }

    // Test invalid inputs for ParseException
    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(""));
    }

    @Test
    public void parse_singleIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1"));
    }

    @Test
    public void parse_threeIndices_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 2 3"));
    }

    @Test
    public void parse_nonNumericArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("a b"));
    }

    @Test
    public void parse_zeroIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("0 1"));
    }

    @Test
    public void parse_negativeIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-1 2"));
    }

}
