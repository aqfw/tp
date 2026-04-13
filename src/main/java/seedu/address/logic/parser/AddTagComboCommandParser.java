package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.tag.TagComboName.MESSAGE_CONSTRAINTS;
import static seedu.address.model.tag.TagComboName.isValidName;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddTagComboCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagCombo;
import seedu.address.model.tag.TagComboName;

/**
 * Parses input arguments and creates a new AddTagComboCommand object
 */
public class AddTagComboCommandParser implements Parser<AddTagComboCommand> {

    public static final String INSUFFICIENT_TAGS_ERROR_MESSAGE = "A tag combo requires at least 2 unique tags!";

    /**
     * Parses the given {@code String} of arguments in the context of the AddTagComboCommand
     * and returns an AddTagComboCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTagComboCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_POSTAL_CODE, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_TAG) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagComboCommand.MESSAGE_USAGE));
        }
        if (!isValidName(argMultimap.getPreamble())) {
            throw new ParseException(MESSAGE_CONSTRAINTS);
        }

        argMultimap.verifyDuplicatePrefixesPresent(PREFIX_TAG);
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        if (tagList.size() <= 1) {
            throw new ParseException(INSUFFICIENT_TAGS_ERROR_MESSAGE);
        }

        TagCombo tagCombo = new TagCombo(new TagComboName(argMultimap.getPreamble()), tagList);

        return new AddTagComboCommand(tagCombo);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
