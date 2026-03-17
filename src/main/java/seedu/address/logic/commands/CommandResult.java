package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.RightPaneContent;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;
    private final UiAction uiAction;
    private final Optional<RightPaneContent> content;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, UiAction uiAction, Optional<RightPaneContent> content) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.uiAction = uiAction;
        this.content = content;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, UiAction.NONE, Optional.empty());
    }

    /**
     * Overloaded constructor for commands that do not update the right pane.
     */
    public CommandResult(String feedbackToUser, UiAction uiAction) throws CommandException {
        this(feedbackToUser, uiAction, Optional.empty());
        if (uiAction == UiAction.UPDATE_RIGHT_PANE) {
            throw new CommandException("This command requires a person or tagcount object.");
        }
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public UiAction getUiAction() {
        return uiAction;
    }

    public Optional<RightPaneContent> getContent() {
        return content;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && uiAction == otherCommandResult.uiAction
                && Objects.equals(content, otherCommandResult.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, uiAction, content);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("uiAction", uiAction)
                .add("rightPaneContent", content)
                .toString();
    }

}
