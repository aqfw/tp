package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.outlet.OutletAddress;
import seedu.address.model.outlet.OutletName;
import seedu.address.model.outlet.OutletPostalCode;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteOutletCommand}.
 */
public class EditOutletCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = "outlet " + COMMAND_WORD
            + ": Edits the details of the outlet identified "
            + "by the index number used in the displayed outlet list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_POSTAL_CODE + "POSTAL_CODE] "
            + "Example: outlet " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "TechCo "
            + PREFIX_ADDRESS + "Raffles Place "
            + PREFIX_POSTAL_CODE + "048623";

    public static final String MESSAGE_EDIT_OUTLET_SUCCESS = "Edited Outlet: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_OUTLET = "This outlet already exists in the address book";
    private final Index index;
    private final EditOutletCommand.EditOutletDescriptor editOutletDescriptor;

    private Outlet editedOutlet;
    private Outlet originalOutlet;

    /**
     * @param index of the person in the outlet list to edit
     * @param editOutletDescriptor details to edit the person with
     */
    public EditOutletCommand(Index index, EditOutletCommand.EditOutletDescriptor editOutletDescriptor) {
        requireNonNull(index);
        requireNonNull(editOutletDescriptor);

        this.index = index;
        this.editOutletDescriptor = new EditOutletCommand.EditOutletDescriptor(editOutletDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Outlet> lastShownList = model.getFilteredOutletList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        originalOutlet = lastShownList.get(index.getZeroBased());
        editedOutlet = createEditedOutlet(originalOutlet, editOutletDescriptor);

        if (!originalOutlet.isSameOutlet(editedOutlet) && model.hasOutlet(editedOutlet)) {
            throw new CommandException(MESSAGE_DUPLICATE_OUTLET);
        }

        model.resetFilteredOutletList();
        model.setOutlet(originalOutlet, editedOutlet);
        return new CommandResult(String.format(MESSAGE_EDIT_OUTLET_SUCCESS, Messages.format(editedOutlet)));
    }

    @Override
    public void undo(Model model) {
        model.setOutlet(editedOutlet, originalOutlet);
    }

    @Override
    public void redo(Model model) {
        model.setOutlet(originalOutlet, editedOutlet);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editOutletDescriptor}.
     */
    private static Outlet createEditedOutlet(Outlet outletToEdit, EditOutletCommand.EditOutletDescriptor
            editOutletDescriptor) {
        assert outletToEdit != null;

        OutletName updatedName = editOutletDescriptor.getName().orElse(outletToEdit.getOutletName());
        OutletAddress updatedAddress = editOutletDescriptor.getAddress().orElse(outletToEdit.getOutletAddress());
        OutletPostalCode updatedPostalCode = editOutletDescriptor.getPostalCode().orElse(outletToEdit.getPostalCode());

        return new Outlet(updatedName, updatedAddress, updatedPostalCode);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditOutletCommand otherEditCommand = (EditOutletCommand) other;
        return index.equals(otherEditCommand.index)
                && editOutletDescriptor.equals(otherEditCommand.editOutletDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editOutletDescriptor", editOutletDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditOutletDescriptor {
        private OutletName name;
        private OutletAddress address;
        private OutletPostalCode postalCode;

        public EditOutletDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditOutletDescriptor(EditOutletCommand.EditOutletDescriptor toCopy) {
            setName(toCopy.name);
            setAddress(toCopy.address);
            setPostalCode(toCopy.postalCode);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, address, postalCode);
        }

        public void setName(OutletName name) {
            this.name = name;
        }

        public Optional<OutletName> getName() {
            return Optional.ofNullable(name);
        }

        public void setAddress(OutletAddress address) {
            this.address = address;
        }

        public Optional<OutletAddress> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setPostalCode(OutletPostalCode postalCode) {
            this.postalCode = postalCode;
        }

        public Optional<OutletPostalCode> getPostalCode() {
            return Optional.ofNullable(postalCode);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditOutletCommand.EditOutletDescriptor)) {
                return false;
            }

            EditOutletCommand.EditOutletDescriptor otherEditOutletDescriptor =
                    (EditOutletCommand.EditOutletDescriptor) other;
            return Objects.equals(name, otherEditOutletDescriptor.name)
                    && Objects.equals(address, otherEditOutletDescriptor.address)
                    && Objects.equals(postalCode, otherEditOutletDescriptor.postalCode);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("address", address)
                    .add("postalCode", postalCode)
                    .toString();
        }
    }
}
