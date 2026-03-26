package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CompareCommand;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses the input arguments and creates a new {@code CompareCommand} object.
 */
public class CompareCommandParser implements Parser<CompareCommand> {

    // Matches the pattern of two positive integers (for two candidates) separated by whitespace
    private static final Pattern COMPARE_ARGS_FORMAT =
            Pattern.compile("(?<firstIndex>\\d+)\\s+(?<secondIndex>\\d+)");

    /**
     * Parses the given {@code args} such that they fit {@code CompareCommand}, returning a
     * {@code CompareCommand} object to be executed.
     * @param args are the user's input arguments from the input window.
     * @return a Compare Command object.
     * @throws ParseException if the args do not conform to the expected format.
     */
    @Override
    public CompareCommand parse(String args) throws ParseException {
        Matcher matcher = COMPARE_ARGS_FORMAT.matcher(args.trim());

        if (!matcher.matches()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompareCommand.MESSAGE_USAGE));
        }

        try {
            Index firstIndex = ParserUtil.parseIndex(matcher.group("firstIndex"));
            Index secondIndex = ParserUtil.parseIndex(matcher.group("secondIndex"));
            return new CompareCommand(firstIndex, secondIndex);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompareCommand.MESSAGE_USAGE), pe);
        }
    }
}
