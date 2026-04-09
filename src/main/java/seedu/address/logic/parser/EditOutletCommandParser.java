package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditOutletCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses an Edit Outlet Command.
 */
public class EditOutletCommandParser implements Parser<EditOutletCommand> {
    @Override
    public EditOutletCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ADDRESS,
                PREFIX_POSTAL_CODE);
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditOutletCommand.MESSAGE_USAGE),
                                                    pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_POSTAL_CODE);

        EditOutletCommand.EditOutletDescriptor editOutletDescriptor = new EditOutletCommand.EditOutletDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editOutletDescriptor.setName(ParserUtil.parseOutletName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editOutletDescriptor.setAddress(ParserUtil.parseOutletAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_POSTAL_CODE).isPresent()) {
            editOutletDescriptor
                    .setPostalCode(ParserUtil.parseOutletPostalCode(argMultimap.getValue(PREFIX_POSTAL_CODE).get()));
        }

        if (!editOutletDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditOutletCommand.MESSAGE_NOT_EDITED);
        }

        return new EditOutletCommand(index, editOutletDescriptor);
    }
}
