package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.AddByCsvCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PostalCode;
import seedu.address.model.tag.Tag;

/**
 * Parses a CSV file path and its contents, then creates a new {@code AddByCsvCommand} object.
 * The CSV file must have the {@code .csv} extension and contain at least the columns:
 * name, phone, email, address, postalCode. An optional tags column may contain semicolon-separated tags.
 */
public class AddByCsvParser implements Parser<AddByCsvCommand> {

    public static final String MESSAGE_FILE_NOT_CSV =
            "The specified file is not in .csv format. Only .csv files are accepted.";
    public static final String MESSAGE_FILE_NOT_FOUND =
            "The specified CSV file does not exist: %1$s";
    public static final String MESSAGE_FILE_READ_ERROR =
            "Error reading the CSV file: %1$s";
    public static final String MESSAGE_EMPTY_CSV =
            "The CSV file is empty or contains only a header row.";
    public static final String MESSAGE_INVALID_CSV_HEADER =
            "The CSV file must have a header row with at least: name,phone,email,address,postalCode. "
                    + "Please do not use Excel to create/edit the CSV file.";
    public static final String MESSAGE_INVALID_ROW =
            "Invalid data at row %1$d: %2$s";

    // name, phone, email, address, postalCode are required; tags are optional
    private static final int EXPECTED_MIN_COLUMNS = 5;
    private static final int NAME_INDEX = 0;
    private static final int PHONE_INDEX = 1;
    private static final int EMAIL_INDEX = 2;
    private static final int ADDRESS_INDEX = 3;
    private static final int POSTAL_CODE_INDEX = 4;
    private static final int TAGS_INDEX = 5;

    @Override
    public AddByCsvCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddByCsvCommand.MESSAGE_USAGE));
        }

        Path filePath = Paths.get(trimmedArgs);

        validateFileExtension(filePath);
        validateFileExists(filePath);

        List<String> lines = readFileLines(filePath);
        HeaderInfo headerInfo = validateAndParseHeader(lines);

        List<Person> persons = parseRows(lines, headerInfo);
        return new AddByCsvCommand(persons);
    }

    /**
     * Validates that the file has a {@code .csv} extension.
     *
     * @throws ParseException if the file does not end with {@code .csv}.
     */
    private void validateFileExtension(Path filePath) throws ParseException {
        String fileName = filePath.getFileName().toString();
        if (!fileName.toLowerCase().endsWith(".csv")) {
            throw new ParseException(MESSAGE_FILE_NOT_CSV);
        }
    }

    /**
     * Validates that the file exists and is a regular file.
     *
     * @throws ParseException if the file does not exist or is not a regular file.
     */
    private void validateFileExists(Path filePath) throws ParseException {
        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            throw new ParseException(String.format(MESSAGE_FILE_NOT_FOUND, filePath));
        }
    }

    /**
     * Reads all lines from the CSV file.
     *
     * @throws ParseException if the file cannot be read.
     */
    private List<String> readFileLines(Path filePath) throws ParseException {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new ParseException(String.format(MESSAGE_FILE_READ_ERROR, e.getMessage()));
        }
    }

    /**
     * Validates that the CSV has a proper header row with the required columns.
     *
     * @throws ParseException if the header is missing or invalid.
     */
    private HeaderInfo validateAndParseHeader(List<String> lines) throws ParseException {
        if (lines.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_CSV);
        }

        String[] header = lines.get(0).split(",", -1);
        if (header.length < EXPECTED_MIN_COLUMNS) {
            throw new ParseException(MESSAGE_INVALID_CSV_HEADER);
        }

        boolean isValidHeader = "name".equalsIgnoreCase(header[NAME_INDEX].trim())
                && "phone".equalsIgnoreCase(header[PHONE_INDEX].trim())
                && "email".equalsIgnoreCase(header[EMAIL_INDEX].trim())
                && "address".equalsIgnoreCase(header[ADDRESS_INDEX].trim());

        if (!isValidHeader) {
            throw new ParseException(MESSAGE_INVALID_CSV_HEADER);
        }

        boolean hasTagsColumn = false;

        if (!"postalCode".equalsIgnoreCase(header[POSTAL_CODE_INDEX].trim())) {
            throw new ParseException(MESSAGE_INVALID_CSV_HEADER);
        }

        if (header.length > TAGS_INDEX) {
            String sixthHeader = header[TAGS_INDEX].trim();
            if ("tags".equalsIgnoreCase(sixthHeader)) {
                hasTagsColumn = true;
            } else {
                throw new ParseException(MESSAGE_INVALID_CSV_HEADER);
            }
        }

        if (header.length > TAGS_INDEX + 1) {
            throw new ParseException(MESSAGE_INVALID_CSV_HEADER);
        }

        return new HeaderInfo(hasTagsColumn);
    }

    /**
     * Parses data rows (skipping the header) into a list of {@code Person} objects.
     *
     * @throws ParseException if any row has invalid data.
     */
    private List<Person> parseRows(List<String> lines, HeaderInfo headerInfo) throws ParseException {
        List<Person> persons = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            Person person = parseRow(line, i + 1, headerInfo);
            persons.add(person);
        }

        if (persons.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_CSV);
        }

        return persons;
    }

    /**
     * Parses a single CSV data row into a {@code Person}.
     *
     * @param row the raw CSV row string.
     * @param rowNumber the 1-based row number for error reporting.
     * @throws ParseException if the row data is invalid.
     */
    private Person parseRow(String row, int rowNumber, HeaderInfo headerInfo) throws ParseException {
        String[] fields = row.split(",", -1);

        if (fields.length < EXPECTED_MIN_COLUMNS) {
            throw new ParseException(String.format(MESSAGE_INVALID_ROW, rowNumber,
                    "expected at least " + EXPECTED_MIN_COLUMNS + " columns"));
        }

        try {
            Name name = ParserUtil.parseName(fields[NAME_INDEX]);
            Phone phone = ParserUtil.parsePhone(fields[PHONE_INDEX]);
            Email email = ParserUtil.parseEmail(fields[EMAIL_INDEX]);
            int endExclusive = fields.length;
            String tagsField = "";

            if (headerInfo.hasTagsColumn) {
                tagsField = fields[endExclusive - 1];
                endExclusive--;
            }

            if (endExclusive <= ADDRESS_INDEX) {
                throw new ParseException("missing postalCode");
            }
            PostalCode postalCode = ParserUtil.parsePostalCode(fields[endExclusive - 1]);
            endExclusive--;

            String addressField = String.join(",", Arrays.copyOfRange(fields, ADDRESS_INDEX, endExclusive));
            Address address = ParserUtil.parseAddress(addressField);
            Set<Tag> tags = parseCsvTags(tagsField);

            return new Person(name, phone, email, address, postalCode, tags);
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_ROW, rowNumber, e.getMessage()));
        }
    }

    /**
     * Parses tags from the row's tags field, separated by semicolons.
     *
     * @param tagsField the tags field content.
     * @throws ParseException if any tag is invalid.
     */
    private Set<Tag> parseCsvTags(String tagsField) throws ParseException {
        Set<Tag> tags = new HashSet<>();

        if (tagsField == null || tagsField.trim().isEmpty()) {
            return tags;
        }

        String[] tagNames = tagsField.split(";");
        for (String tagName : tagNames) {
            String trimmedTag = tagName.trim();
            if (trimmedTag.isEmpty()) {
                continue;
            }
            tags.add(ParserUtil.parseTag(trimmedTag));
        }

        return tags;
    }

    private static class HeaderInfo {
        private final boolean hasTagsColumn;

        private HeaderInfo(boolean hasTagsColumn) {
            this.hasTagsColumn = hasTagsColumn;
        }
    }
}
