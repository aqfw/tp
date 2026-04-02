package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AssignOutletCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AssignOutletCommand object.
 */
public class AssignOutletCommandParser implements Parser<AssignOutletCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AssignOutletCommand
     * and returns an AssignOutletCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AssignOutletCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignOutletCommand.MESSAGE_USAGE));
        }
        String[] tokens = trimmedArgs.split("\\s+");

        if (tokens.length != 1 && tokens.length != 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignOutletCommand.MESSAGE_USAGE));
        }

        try {
            Index candidateIndex = ParserUtil.parseIndex(tokens[0]);
            if (tokens.length == 1) {
                return new AssignOutletCommand(candidateIndex);
            }

            Index outletIndex = ParserUtil.parseIndex(tokens[1]);
            return new AssignOutletCommand(candidateIndex, outletIndex);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignOutletCommand.MESSAGE_USAGE),
                    pe);
        }
    }
}
