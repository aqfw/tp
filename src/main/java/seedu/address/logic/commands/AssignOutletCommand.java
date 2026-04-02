package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.outlet.Outlet;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.ui.UiAction;
import seedu.address.ui.content.PersonContent;

/**
 * Assigns an outlet to a candidate.
 */
public class AssignOutletCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = "outlet " + COMMAND_WORD + ": Assigns a candidate to an outlet.\n"
            + "Parameters: CANDIDATE_INDEX [OUTLET_INDEX] (positive integers)\n"
            + "If OUTLET_INDEX is omitted, candidate is assigned to the nearest outlet by postal code.\n"
            + "Examples: outlet " + COMMAND_WORD + " 1 2, outlet " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Assigned %1$s to outlet %2$s";
    public static final String MESSAGE_NO_OUTLETS_AVAILABLE = "No outlets available to assign.";
    public static final String MESSAGE_MISSING_POSTAL_DATA =
            "Unable to load SG postal dataset from resources: /data/SG_postal.csv";

    private static final String SG_POSTAL_DATA_PATH = "/data/SG_postal.csv";
    private static final Map<String, Coordinate> SG_POSTAL_COORDINATES = new HashMap<>();

    private final Index candidateIndex;
    private final Index outletIndex;

    private Person personToAssign;
    private Person assignedPerson;

    /**
     * Creates an AssignOutletCommand to assign the nearest outlet to the specified candidate.
     */
    public AssignOutletCommand(Index candidateIndex) {
        requireNonNull(candidateIndex);
        this.candidateIndex = candidateIndex;
        this.outletIndex = null;
    }

    /**
     * Creates an AssignOutletCommand to assign an outlet to the specified candidate.
     */
    public AssignOutletCommand(Index candidateIndex, Index outletIndex) {
        requireNonNull(candidateIndex);
        requireNonNull(outletIndex);
        this.candidateIndex = candidateIndex;
        this.outletIndex = outletIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownPersons = model.getFilteredPersonList();
        if (candidateIndex.getZeroBased() >= lastShownPersons.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        List<Outlet> lastShownOutlets = model.getFilteredOutletList();
        if (lastShownOutlets.isEmpty()) {
            throw new CommandException(MESSAGE_NO_OUTLETS_AVAILABLE);
        }

        personToAssign = lastShownPersons.get(candidateIndex.getZeroBased());
        Outlet outletToAssign = outletIndex == null
                ? resolveNearestOutlet(personToAssign, lastShownOutlets)
                : resolveSpecifiedOutlet(lastShownOutlets);
        assignedPerson = new Person(personToAssign.getName(), personToAssign.getPhone(),
                personToAssign.getEmail(), personToAssign.getAddress(), personToAssign.getPostalCode(),
                personToAssign.getTags(), outletToAssign);

        try {
            model.setPerson(personToAssign, assignedPerson);
        } catch (DuplicatePersonException e) {
            throw new CommandException(e.getMessage());
        }

        model.resetFilteredPersonList();
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, personToAssign.getName(), outletToAssign.getOutletName()),
                UiAction.UPDATE_RIGHT_PANE,
                Optional.of(new PersonContent(assignedPerson, "Candidate #" + candidateIndex.getOneBased())));
    }

    private Outlet resolveSpecifiedOutlet(List<Outlet> outlets) throws CommandException {
        if (outletIndex.getZeroBased() >= outlets.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_OUTLET_DISPLAYED_INDEX);
        }
        return outlets.get(outletIndex.getZeroBased());
    }

    private Outlet resolveNearestOutlet(Person person, List<Outlet> outlets) throws CommandException {
        loadPostalCoordinatesIfNeeded();

        String candidatePostal = normalizePostal(person.getPostalCode().value);
        Coordinate candidateCoordinate = SG_POSTAL_COORDINATES.get(candidatePostal);
        if (candidateCoordinate == null) {
            List<Outlet> outletsNotInSgData = new ArrayList<>();
            for (Outlet outlet : outlets) {
                Coordinate outletCoordinate = SG_POSTAL_COORDINATES.get(
                        normalizePostal(outlet.getPostalCode().value));
                if (outletCoordinate == null) {
                    outletsNotInSgData.add(outlet);
                }
            }

            if (!outletsNotInSgData.isEmpty()) {
                return pickRandomOutlet(outletsNotInSgData);
            }

            return pickRandomOutlet(outlets);
        }

        Outlet nearestOutlet = null;
        double nearestDistanceSquared = Double.MAX_VALUE;
        for (Outlet outlet : outlets) {
            Coordinate outletCoordinate = SG_POSTAL_COORDINATES.get(
                    normalizePostal(outlet.getPostalCode().value));
            if (outletCoordinate == null) {
                continue;
            }

            double distanceSquared = candidateCoordinate.distanceSquaredTo(outletCoordinate);
            if (distanceSquared < nearestDistanceSquared) {
                nearestDistanceSquared = distanceSquared;
                nearestOutlet = outlet;
            }
        }

        if (nearestOutlet != null) {
            return nearestOutlet;
        }

        return pickRandomOutlet(outlets);
    }

    private Outlet pickRandomOutlet(List<Outlet> outlets) {
        int randomIndex = ThreadLocalRandom.current().nextInt(outlets.size());
        return outlets.get(randomIndex);
    }

    private static void loadPostalCoordinatesIfNeeded() throws CommandException {
        if (!SG_POSTAL_COORDINATES.isEmpty()) {
            return;
        }

        synchronized (SG_POSTAL_COORDINATES) {
            if (!SG_POSTAL_COORDINATES.isEmpty()) {
                return;
            }

            try (InputStream stream = AssignOutletCommand.class.getResourceAsStream(SG_POSTAL_DATA_PATH)) {
                if (stream == null) {
                    throw new CommandException(MESSAGE_MISSING_POSTAL_DATA);
                }

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                    // Skip header row.
                    reader.readLine();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] columns = line.split(",", -1);
                        if (columns.length < 4) {
                            continue;
                        }

                        String postalCode = normalizePostal(columns[0].trim());
                        if (postalCode.isEmpty()) {
                            continue;
                        }

                        try {
                            int latitudeIndex = columns.length - 2;
                            int longitudeIndex = columns.length - 1;
                            double latitude = Double.parseDouble(columns[latitudeIndex].trim());
                            double longitude = Double.parseDouble(columns[longitudeIndex].trim());
                            SG_POSTAL_COORDINATES.put(postalCode, new Coordinate(latitude, longitude));
                        } catch (NumberFormatException ignored) {
                            // Ignore malformed rows.
                        }
                    }
                }
            } catch (IOException e) {
                throw new CommandException(MESSAGE_MISSING_POSTAL_DATA);
            }
        }
    }

    private static String normalizePostal(String postal) {
        return postal.replaceFirst("^0+(?!$)", "");
    }

    private static class Coordinate {
        private final double latitude;
        private final double longitude;

        private Coordinate(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        private double distanceSquaredTo(Coordinate other) {
            double latitudeDelta = latitude - other.latitude;
            double longitudeDelta = longitude - other.longitude;
            return latitudeDelta * latitudeDelta + longitudeDelta * longitudeDelta;
        }
    }

    @Override
    public void undo(Model model) {
        model.setPerson(assignedPerson, personToAssign);
    }

    @Override
    public void redo(Model model) {
        model.setPerson(personToAssign, assignedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AssignOutletCommand otherAssignOutletCommand)) {
            return false;
        }

        if (!candidateIndex.equals(otherAssignOutletCommand.candidateIndex)) {
            return false;
        }

        if (outletIndex == null) {
            return otherAssignOutletCommand.outletIndex == null;
        }
        return outletIndex.equals(otherAssignOutletCommand.outletIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("candidateIndex", candidateIndex)
                .add("outletIndex", outletIndex)
                .toString();
    }
}
