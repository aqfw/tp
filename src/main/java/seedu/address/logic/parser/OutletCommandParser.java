package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddOutletCommand;
import seedu.address.logic.commands.AssignOutletCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteOutletCommand;
import seedu.address.logic.commands.ListOutletsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses outlet commands.
 */
public class OutletCommandParser implements Parser<Command> {

    public static final String COMMAND_WORD = "outlet";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Manages outlets.\n"
            + "Subcommands:\n"
            + "  " + AddOutletCommand.MESSAGE_USAGE + "\n"
            + "  " + AssignOutletCommand.MESSAGE_USAGE + "\n"
            + "  " + DeleteOutletCommand.MESSAGE_USAGE + "\n"
            + "  outlet " + ListOutletsCommand.COMMAND_WORD + ": Lists all outlets.";

    private static final Pattern OUTLET_COMMAND_FORMAT = Pattern.compile("(?<subcommand>\\S+)(?<arguments>.*)");

    @Override
    public Command parse(String args) throws ParseException {
        final Matcher matcher = OUTLET_COMMAND_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }

        final String subcommand = matcher.group("subcommand");
        final String subcommandArguments = matcher.group("arguments");

        switch (subcommand) {
        case AddOutletCommand.COMMAND_WORD:
            return new AddOutletCommandParser().parse(subcommandArguments);
        case AssignOutletCommand.COMMAND_WORD:
            return new AssignOutletCommandParser().parse(subcommandArguments);
        case DeleteOutletCommand.COMMAND_WORD:
            return new DeleteOutletCommandParser().parse(subcommandArguments);
        case ListOutletsCommand.COMMAND_WORD:
            if (!subcommandArguments.trim().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
            }
            return new ListOutletsCommand();
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }
    }
}
