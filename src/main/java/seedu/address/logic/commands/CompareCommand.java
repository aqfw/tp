package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.ComparisonContent;


/**
 * Displays two candidates side-by-side in the right pane for comparison. Candidates identified by their
 * index in the displayed person list.
 * This does not need to inherit from an undoable command as it is a way to display information.
 */
public class CompareCommand extends Command {

    public static final String COMMAND_WORD = "compare";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Compares two candidates side-by-side using their displayed list indices.\n"
            + "Parameters: INDEX_1 INDEX_2 (both must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2";



    public static final String MESSAGE_COMPARE_SUCCESS = "Comparing candidate %1$d and candidate %2$d.";
    public static final String MESSAGE_SAME_INDEX = "The two indices must refer to different candidates.";
    public static final String MESSAGE_INVALID_INDEX = "One or more indices are out of range. "
            + "There are only %1$d candidates listed.";

    private final Index firstIndex;
    private final Index secondIndex;

    /**
     * Creates a {@code CompareCommand} to compare the candidates at the given indices.
     */
    public CompareCommand(Index firstIndex, Index secondIndex) {
        requireNonNull(firstIndex);
        requireNonNull(secondIndex);
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (firstIndex.equals(secondIndex)) {
            throw new CommandException(MESSAGE_SAME_INDEX);
        }

        List<Person> lastShownList = model.getFilteredPersonList();
        int listSize = lastShownList.size();

        if (firstIndex.getZeroBased() >= listSize || secondIndex.getZeroBased() >= listSize) {
            throw new CommandException(String.format(MESSAGE_INVALID_INDEX, listSize));
        }

        Person firstPerson = lastShownList.get(firstIndex.getZeroBased());
        Person secondPerson = lastShownList.get(secondIndex.getZeroBased());

        String firstHeader = "Candidate #" + firstIndex.getOneBased();
        String secondHeader = "Candidate #" + secondIndex.getOneBased();

        ComparisonContent content = new ComparisonContent(
                firstPerson, firstHeader, secondPerson, secondHeader);

        return new CommandResult(
                String.format(MESSAGE_COMPARE_SUCCESS,
                        firstIndex.getOneBased(), secondIndex.getOneBased()),
                UiAction.UPDATE_RIGHT_PANE,
                Optional.of(content));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CompareCommand)) {
            return false;
        }

        CompareCommand otherCommand = (CompareCommand) other;
        return firstIndex.equals(otherCommand.firstIndex)
                && secondIndex.equals(otherCommand.secondIndex);
    }
}
