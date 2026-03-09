package seedu.address.logic.parser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import seedu.address.logic.commands.AddByCsvCommand;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests for {@code AddByCsvParser}.
 * Test CSV data files are located under {@code src/test/data/AddByCsvParserTest/}.
 */
public class AddByCsvParserTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "AddByCsvParserTest");

    private final AddByCsvParser parser = new AddByCsvParser();

    // ==================== Happy-path tests ====================

    @Test
    public void parse_validCsvWithTags_success() throws Exception {
        String filePath = TEST_DATA_FOLDER.resolve("validCandidates.csv").toString();

        AddByCsvCommand result = parser.parse(" " + filePath);

        Person alice = new PersonBuilder().withName("Alice Pauline").withPhone("94351253")
                .withEmail("alice@example.com").withAddress("123 Jurong West Ave 6 #08-111")
                .withTags("friends").build();
        Person benson = new PersonBuilder().withName("Benson Meier").withPhone("98765432")
                .withEmail("johnd@example.com").withAddress("311 Clementi Ave 2 #02-25")
                .withTags("owesMoney", "friends").build();
        Person carl = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
                .withEmail("heinz@example.com").withAddress("wall street")
                .withTags().build();

        List<Person> expectedPersons = Arrays.asList(alice, benson, carl);
        assertEquals(new AddByCsvCommand(expectedPersons), result);
    }

    @Test
    public void parse_validCsvWithoutTagsColumn_success() throws Exception {
        String filePath = TEST_DATA_FOLDER.resolve("validNoTags.csv").toString();

        AddByCsvCommand result = parser.parse(" " + filePath);

        Person hoon = new PersonBuilder().withName("Hoon Meier").withPhone("8482424")
                .withEmail("stefan@example.com").withAddress("little india")
                .withTags().build();
        Person ida = new PersonBuilder().withName("Ida Mueller").withPhone("8482131")
                .withEmail("hans@example.com").withAddress("chicago ave")
                .withTags().build();

        List<Person> expectedPersons = Arrays.asList(hoon, ida);
        assertEquals(new AddByCsvCommand(expectedPersons), result);
    }

    // ==================== Error-handling tests ====================

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddByCsvCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_notCsvExtension_failure() {
        String filePath = TEST_DATA_FOLDER.resolve("notCsv.txt").toString();
        assertParseFailure(parser, " " + filePath, AddByCsvParser.MESSAGE_FILE_NOT_CSV);
    }

    @Test
    public void parse_fileDoesNotExist_failure() {
        String filePath = TEST_DATA_FOLDER.resolve("doesNotExist.csv").toString();
        assertParseFailure(parser, " " + filePath,
                String.format(AddByCsvParser.MESSAGE_FILE_NOT_FOUND, Paths.get(filePath)));
    }

    @Test
    public void parse_emptyCsvFile_failure() {
        String filePath = TEST_DATA_FOLDER.resolve("empty.csv").toString();
        assertParseFailure(parser, " " + filePath, AddByCsvParser.MESSAGE_INVALID_CSV_HEADER);
    }

    @Test
    public void parse_headerOnlyCsv_failure() {
        String filePath = TEST_DATA_FOLDER.resolve("headerOnly.csv").toString();
        assertParseFailure(parser, " " + filePath, AddByCsvParser.MESSAGE_EMPTY_CSV);
    }

    @Test
    public void parse_invalidHeader_failure() {
        String filePath = TEST_DATA_FOLDER.resolve("invalidHeader.csv").toString();
        assertParseFailure(parser, " " + filePath, AddByCsvParser.MESSAGE_INVALID_CSV_HEADER);
    }

    @Test
    public void parse_tooFewColumns_failure() {
        String filePath = TEST_DATA_FOLDER.resolve("tooFewColumns.csv").toString();
        assertParseFailure(parser, " " + filePath,
                String.format(AddByCsvParser.MESSAGE_INVALID_ROW, 2,
                        "expected at least 4 columns"));
    }

    @Test
    public void parse_invalidName_failure() {
        String filePath = TEST_DATA_FOLDER.resolve("invalidName.csv").toString();
        assertParseFailure(parser, " " + filePath,
                String.format(AddByCsvParser.MESSAGE_INVALID_ROW, 2,
                        seedu.address.model.person.Name.MESSAGE_CONSTRAINTS));
    }

    @Test
    public void parse_invalidPhone_failure() {
        String filePath = TEST_DATA_FOLDER.resolve("invalidPhone.csv").toString();
        assertParseFailure(parser, " " + filePath,
                String.format(AddByCsvParser.MESSAGE_INVALID_ROW, 2,
                        seedu.address.model.person.Phone.MESSAGE_CONSTRAINTS));
    }

    @Test
    public void parse_invalidEmail_failure() {
        String filePath = TEST_DATA_FOLDER.resolve("invalidEmail.csv").toString();
        assertParseFailure(parser, " " + filePath,
                String.format(AddByCsvParser.MESSAGE_INVALID_ROW, 2,
                        seedu.address.model.person.Email.MESSAGE_CONSTRAINTS));
    }

    @Test
    public void parse_invalidTag_failure() {
        String filePath = TEST_DATA_FOLDER.resolve("invalidTag.csv").toString();
        assertParseFailure(parser, " " + filePath,
                String.format(AddByCsvParser.MESSAGE_INVALID_ROW, 2,
                        seedu.address.model.tag.Tag.MESSAGE_CONSTRAINTS));
    }

    // ==================== Edge-case tests ====================

    @Test
    public void parse_validCsvWithBlankLinesBetweenRows_success() throws Exception {
        String filePath = TEST_DATA_FOLDER.resolve("validWithBlankLines.csv").toString();

        AddByCsvCommand result = parser.parse(" " + filePath);

        Person alice = new PersonBuilder().withName("Alice Pauline").withPhone("94351253")
                .withEmail("alice@example.com").withAddress("123 Jurong West Ave 6 #08-111")
                .withTags("friends").build();
        Person benson = new PersonBuilder().withName("Benson Meier").withPhone("98765432")
                .withEmail("johnd@example.com").withAddress("311 Clementi Ave 2 #02-25")
                .withTags("owesMoney", "friends").build();
        Person carl = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
                .withEmail("heinz@example.com").withAddress("wall street")
                .withTags().build();

        List<Person> expectedPersons = Arrays.asList(alice, benson, carl);
        assertEquals(new AddByCsvCommand(expectedPersons), result);
    }

    @Test
    public void parse_validCsvWithEmptyTagBetweenSemicolons_success() throws Exception {
        String filePath = TEST_DATA_FOLDER.resolve("validWithEmptyTagSemicolons.csv").toString();

        AddByCsvCommand result = parser.parse(" " + filePath);

        Person alice = new PersonBuilder().withName("Alice Pauline").withPhone("94351253")
                .withEmail("alice@example.com").withAddress("123 Jurong West Ave 6 #08-111")
                .withTags("friends", "family").build();

        List<Person> expectedPersons = Arrays.asList(alice);
        assertEquals(new AddByCsvCommand(expectedPersons), result);
    }
}
