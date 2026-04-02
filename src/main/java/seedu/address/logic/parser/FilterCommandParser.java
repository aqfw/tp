package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_COMBO;

import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagComboName;

/**
 * Parses arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private Set<Tag> tagList;

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FindCommand object for execution
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TAG, PREFIX_TAG_COMBO);

        if ((argMultimap.getAllValues(PREFIX_TAG).isEmpty() && argMultimap.getAllValues(PREFIX_TAG_COMBO).isEmpty())
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            tagList = new HashSet<Tag>();
        } else {
            tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        }

        Set<TagComboName> tagComboNames = ParserUtil.parseTagComboNames(argMultimap.getAllValues(PREFIX_TAG_COMBO));

        return new FilterCommand(tagList, tagComboNames);
    }
}
