package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnassignOutletCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnassignOutletCommand object.
 */
public class UnassignOutletCommandParser implements Parser<UnassignOutletCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnassignOutletCommand
     * and returns an UnassignOutletCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnassignOutletCommand parse(String args) throws ParseException {
        try {
            Index candidateIndex = ParserUtil.parseIndex(args);
            return new UnassignOutletCommand(candidateIndex);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignOutletCommand.MESSAGE_USAGE),
                    pe);
        }
    }
}

