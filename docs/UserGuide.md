---
layout: page
title: User Guide
---

HireLens is a **desktop app for HR recruiters to manage candidates, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, HireLens can get your candidate management tasks done faster than traditional GUI apps.

* Table of Contents 
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103-F08-3/tp/releases/tag/v1.5).

3. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * `list` : Lists all contacts.

    * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 pc/123456` : Adds a contact named `John Doe` to the Address Book.

    * `delete 3` : Deletes the 3rd candidate shown in the current candidate list.

    * `clear` : Deletes all candidates, outlets and tagcombos.

    * `exit` : Exits the app.

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## UI Components and Glossary

![UiComponents](images/UiComponents.png)

The image above shows the key UI Components, which are described in detail below.

1. **Command Box**: This is where commands are entered into.
2. **Status Message Box**: Upon submitting a command, this is where the status message is displayed.
3. **Candidate List**: This is where the list of candidates is stored. It shows 4 different fields of the candidate: their **ID**, **Name**, **Tags**, and their **Address**. Clicking on each candidate in the **Candidate List** shows their full details in the **Right Panel** (described below), including **Postal Code** and **Email Address**, alongside any truncated fields in the **Candidate List**.
4. **Right Panel**: This is where additional information is displayed. The **Right Panel** can display information such as tag counts, tag combos, full person details etc. Refer to [Features](#features) below for details of each command and their interaction with the right panel.
5. **Outlet Panel**: This is where the list of outlets are displayed.

### Additional Glossary Terms

1. **Candidate**: Each person stored in the candidate list will be referred to as a candidate. A Candidate consists of the following details: **Name**, **Address**, **Email Address**, **Postal Code**, **Phone Number**, **Tags** (optional).
2. **View**: A view refers to the graphical display of the candidate book. The current view refers to list of candidates that is currently visible in the graphical view. This distinction is important as some commands are performed on the current view of the address book, rather than the full candidate book.
3. **Tag Combination**: A set of tags defined by the user under a specific name (E.g The **MLE** tag combination could contain the tags **Python**, **SQL** and **Machine Learning**).
4. **Outlet**: An outlet corresponds to a physical location of an office/asset of the company, with the following details: **Name**, **Address** and **Postal Code**.

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Multiple square bracket items within a curly brace implies that all fields within the curly brace are optional, but at least 1 of such field is required. e.g. `{[t/TAG] [tc/TAG_COMBO]}` means that both `t/` and `tc/` are optional, but at least 1 is required.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Displays this `UserGuide.md` markdown file as raw markdown text with special characters included. A Copy URL button is provided in event of a missing `UserGuide.md` so that a link to the project Github repo's user guide is copied to the clipboard.

Format: `help`

Example: Entering `help` in the Command Box opens a new Help Window with the features described above. Status Message Box output is: `Opened help window.`
* Alternatively, clicking the "help" button opens the Help Window as well, though it does not produce the Status Message Box output.

### Adding a candidate: `add`

Adds a candidate to the candidate list.
- You cannot have a duplicate `PHONE_NUMBER` or `EMAIL` already in the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS pc/POSTAL_CODE [t/TAG]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A candidate can have any number of tags (including 0)
</div>

The full details of the candidate are displayed on the **Right Panel** on success.

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 pc/123456`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 pc/654321 t/criminal`

### Adding Candidates by CSV File : `addcsv`

Adds multiple candidates from a CSV file.

Format: `addcsv PATH_TO_CSV_FILE`
- You cannot have a duplicate `PHONE_NUMBER` or `EMAIL` already in the address book or within the csv file.

* `PATH_TO_CSV_FILE` must point to a `.csv` file that exists.
* The file path is resolved from the current working directory (typically the folder where you run the app/JAR).
* Header row is required and must be:
  `name,phone,email,address,postalCode` with an optional sixth column `tags`.
* Header names are case-insensitive, but order must remain the same.
* Extra columns beyond the optional `tags` column are not allowed.
* `postalCode` is required.
* `tags` is optional; when present, multiple tags in one cell must be separated by `;`.
* Addresses containing commas are supported.
* Blank lines are ignored.
* Duplicate rules (same as `add`):
  a candidate is considered a duplicate if phone matches an existing candidate, or email matches an existing candidate.
* Duplicate checks are performed against:
  all existing candidates in the app, and candidates within the same CSV file.
* Import is all-or-nothing:
  if any row is invalid, or any duplicate candidate is detected, no candidates are added.
* Batch size is limited to:
  `min(25, 999 - current number of candidates)`.
  If the file exceeds this limit, no candidates are added.

Examples:
* `addcsv data/candidates.csv`
* `addcsv 1/2/test.csv`

<div markdown="span" class="alert alert-warning">**Caution:**
Please do not use Excel to create/edit CSV files for `addcsv`.
Use a plain text editor to avoid hidden formatting/encoding issues for the headers.
</div>

### Listing all candidates : `list`

Shows a list of all candidates in the candidate list.

Format: `list`

Tag counts are displayed on the **Right Panel** on success, similar to calling `listtags`.

### Editing a candidate : `edit`

Edits at least one existing candidate in the candidate list.
- You cannot have a duplicate `PHONE_NUMBER` or `EMAIL` already in the address book or within the candidates being edited.

Format: `edit INDEX [INDEX]… {[n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pc/POSTAL_CODE] [t/TAG]} [t/TAG]…​`

* Edits the candidates at the specified `INDEXES`. The index refers to the index number shown in the displayed candidate list. The index **must be a positive integer** 1, 2, 3, …​
* At least one index must be provided and all the indexes provided must be valid inputs.
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the candidate will be removed i.e adding of tags is not cumulative.
* You can remove all the candidate’s tags by typing `t/` without
  specifying any tags after it.
* The full details of the edited candidated is displayed on the **Right Panel** on success.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st candidate to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd candidate to be `Betsy Crower` and clears all existing tags.
*  `edit 1 2 3 t/python` Edits the tags of the 1st, 2nd and 3rd candidate to be replaced by python.
*  `edit 1 t/Java` displays the following results.

![result for `edit 1 t/Java`](images/editPaulineJava.png)

### Locating candidates by name : `find`

Finds candidates whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* cCandidates matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`
* The search works on the CURRENT view of the Candidate List, rather than the full Candidate List.

Examples:
* `find John` returns `john` and `John Doe`
* `find carl ethan` returns `Carl Kurz`, `Ethan Lim` <br>
  ![result for 'find carl ethan'](images/findCarlEthanResult.png)

### Locating candidates by tag: `filter`

Finds candidates who contain ALL of the given tags, and tags given in the tag combo.

Format: `filter {[t/TAG] [tc/TAG_COMBO]} [t/TAG]... [tc/TAG_COMBO]...`

* The search is case-insensitive. e.g `hans` will match `Hans`.
* The search works on the CURRENT view of the Candidate List, rather than the full Candidate List.
* The search requires at least 1 tag/tag combo to work.
* The tag combo must exist to work, whereas an invalid tag will simply return 0 candidates.
* The tag counts after filtering are displayed on the **Right Panel** on success.

Examples:
* `filter tc/ml dev`
* `filter t/java tc/ml dev`
* `filter t/Java t/Python` returns `Benson Meier`, `Natalie Lim`.
  ![result for 'filter t/python t/java'](images/filterPythonJavaResult.png)

### Comparing Candidates: `compare`

Compare two candidates from the currently shown candidate list by the candidate's number (e.g. 1 in the case of `1. Benson Huang`), side-by-side in the right panel.
Information clears when another action takes up the right panel.

Format: `compare INDEX_1 INDEX_2`

Example: `compare 1 2` selects candidate numbered 1 and 2 in the list for comparison, displaying them in the right pane. 
* Order is the candidate identified by INDEX_1 on the left and that by INDEX_2 on the right
* Status message box shows: `Comparing candidate 1 and candidate 2.`


### Listing existing tags: `listtags`

Lists all tags in descending order of frequencies, along with their frequencies in the **Right Panel**.

* Order is not guaranteed in the case of ties.

Format: `listtags`

### Adding tag combos: `addtagcombo`

Adds a tag combo to the Tag Combo List.
- You cannot have a duplicate `TAGS` or `NAME` already in the address book. In this case, `TAGS` refers to the tags stored within the tag combo. 
- For example, `addtagcombo HR t/marketing t/leadership` has `TAGS` marketing and leadership.

Format: `addtagcombo NAME t/TAG t/TAG [t/TAG]...`

* The name of the tag combo must consist of only alphanumeric characters, and be at most 25 characters long.
* Minimally 2 tags are needed to define a tag combo, as a tag combo with only one tag is functionally equivalent to a tag with an alias.
* The list of tag combos are displayed in the **Right Panel** on success, similar to `listtagcombo`.

Examples:
* `addtagcombo ml dev t/python t/ml`
* `addtagcombo java backend dev t/java t/backend t/docker`
  ![result for `addtagcombo java backend dev t/java t/backend t/docker`](images/addTagComboResult.png)

### Deleting tag combos: `deletetagcombo`

Deletes a tag combo from the Tag Combo List.

Format: `deletetagcombo INDEX`

* Deletes the tag combo at the specified `INDEX`.
* The index refers to the index number shown in the displayed tag combo list.
* The index **must be a positive integer** 1, 2, 3, …​
* The full tag combo list is displayed on the **Right Panel** on success, similar to `listtagcombo`.

Examples:
* `deletetagcombo 1`

### Listing tag combos: `listtagcombo`

Lists all tag combos in the Right Panel.

Format: `listtagcombo`

  ![result for 'listtagcombo'](images/listTagComboResult.png)

### Deleting a candidate : `delete`

Deletes the specified candidate from the address book.

Format: `delete INDEX`

* Deletes the candidate at the specified `INDEX`.
* The index refers to the index number shown in the displayed candidate list.
* The index **must be a positive integer** 1, 2, 3, …​
* The full details of the deleted candidate are displayed on the **Right Panel** on success.

Examples:
* `list` followed by `delete 2` deletes the 2nd candidate in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st candidate in the results of the `find` command.

### Clearing all entries : `clear`

Clears all entries from the application (Candidate List, Outlet List, Tag Combo List)

Format: `clear`

### Undoing previous action : `undo`

Reverts the previous action performed.
Note that the actions of a user are not saved when the app is closed. Thus, closing the app and attempting to
undo the last action performed will result in the message "Nothing to undo!".

Format: `undo`

* `add` Deletes the `Candidate` added.
* `addcsv` Deletes all `Candidate`s added.
* `outlet add` Deletes the `Outlet` added.
* `addtagcombo` Deletes the `tagcombo` added.
* `delete` Adds the `Candidate` deleted.
* `outlet delete` Adds the `Outlet` deleted.
* `deletetagcombo` Adds the `tagcombo` deleted.
* `edit` Returns the edited `Candidate` to original state.
* `outlet edit` Returns the edited `Outlet` to the original state.
* `outlet assign` Unassigns the `Candidate` from the given `Outlet`.
* `outlet unassign` Reassigns the `Candidate` to the previous `Outlet`.
* `list`, `filter`, `find` Returns to the previous view of the Address Book.
* `clear` Adds all `Candidate`s deleted.

### Redoing previous action : `redo`

Redoes the previous action performed.
Note that after performing a new action, the previous undo cannot be redone. This is to prevent complicated interactions
that may arise from editing an entry and then redoing the previous edit.
To demonstrate:
- `edit 1 t/python`
- `undo`
- `edit 1 n/John Doe`
- `redo` -> "Nothing to redo!"

Format: `redo`

* `add` Adds the `Candidate` deleted.
* `addcsv` Adds all `Candidate`s deleted.
* `outlet add` Adds the `Outlet` deleted.
* `addtagcombo` Adds the `tagcombo` deleted.
* `delete` Deletes the `Candidate` added.
* `outlet delete` Deletes the `Outlet` added.
* `deletetagcombo` Deletes the `tagcombo` added.
* `edit` Returns the original `Candidate` to edited state.
* `outlet edit` Returns the original `Outlet` to the edited state.
* `outlet assign` Reassigns the `Candidate` to the given `Outlet`.
* `outlet unassign` Unassigns the `Candidate` from the previous `Outlet`.
* `list`, `filter`, `find` Returns to the filtered view of the Address Book.
* `clear` Deletes all `Candidate`s added.

* `delete 2` followed by `undo` restores the person deleted at index 2.
* `list` followed by `find Alice` followed by `undo` restores the view to a list of all persons in the address book.

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Adding Outlets : `outlet add`

Adds an `Outlet`.
- You cannot have a duplicate `ADDRESS` and `POSTAL_CODE` both already in the address book.

Format: `outlet add n/NAME a/ADDRESS pc/POSTAL_CODE`

- All three prefixes `n/`, `a/`, and `pc/` are required.
- Each prefix should appear at most once.
- Outlet name must be at most 10 characters long.
- Outlet address must be at most 18 characters long.
- Outlet name and address must not contain delimiters.
- Outlet name cannot be the reserved word `unassigned` (in any capitalization).
- Outlet postal code must be exactly 6 digits.
- Duplicate outlet definition:
  an outlet is considered duplicate only when name, address, and postal code all match an existing outlet.

Examples:

- `outlet add n/FinServ a/Marina Bay pc/018956`
- `outlet add n/TechCo a/Raffles Place pc/048623`
  ![result for `outlet add n/TechCo a/Raffles Place pc/048623`](images/OutletAddResult.png)

### Editing Outlets : `outlet edit`

Edits an existing `Outlet`.
- You cannot have a duplicate `ADDRESS` and `POSTAL_CODE` both already in the address book.

- Format: `outlet edit <index> {[n/NAME] [a/ADDRESS] [pc/POSTAL_CODE]}`

Examples:

- `outlet edit 1 a/One Raffles Place pc/048616` Edits the address and the postal code of the 1st outlet to be `One Raffles Place` and `048616` respectively.
- `outlet edit 2 n/TechHub` Edits the name of the 2nd outlet to be `TechHub`.

### Assigning Candidates to Outlets : `outlet assign`

Assigns a candidate to an `Outlet`.

Format: `outlet assign CANDIDATE_INDEX [OUTLET_INDEX]`

* `CANDIDATE_INDEX` refers to the displayed candidate list.
* `OUTLET_INDEX` (if provided) refers to the displayed outlet list.
* At least one outlet must exist.
* Assumption: outlets are intended to be Singapore locations with valid Singapore postal codes.
* If `OUTLET_INDEX` is provided, the candidate is assigned directly to that outlet.
* If `OUTLET_INDEX` is omitted, nearest assignment mode is used:
  * Candidate/outlet postal codes are looked up in the built-in SG postal dataset (`SG_postal.csv`).
  * If candidate postal code is found:
    * nearest outlet is selected using Euclidean distance on latitude/longitude,
      considering only outlets whose postal code is also found in the dataset.
    * if no outlet postal code is found in the dataset, one outlet is chosen randomly.
  * If candidate postal code is not found:
    * if at least one outlet postal code is also not found in the dataset, one of those outlets is chosen randomly.
    * otherwise, one outlet is chosen randomly from all outlets.
* Random fallback selection is non-deterministic.
* If the SG postal dataset cannot be loaded, command execution fails.
* If candidate address appears to be outside Singapore, assignment still succeeds and a warning is shown.
  This warning is heuristic (keyword-based) and may have false positives/negatives.

Examples:

- `outlet assign 1 1`
  ![result for `outlet assign 1 1`](images/OutletAssignResult.png)
- `outlet assign 1`
  ![result for `outlet assign 1`](images/OutletAssignResult.png)
- `outlet assign 4` with non-Singapore-like address warning.
  ![result for `outlet assign 4` with warning](images/OutletAssignResultWithWarning.png)

### Unassigning Candidates from Outlets : `outlet unassign`

Unassigns a candidate from their working `Outlet`.

Format: `outlet unassign CANDIDATE_INDEX`

* `CANDIDATE_INDEX` refers to the displayed candidate list.
* If the candidate is already unassigned, the command keeps the candidate unassigned.

Examples:

- `outlet unassign 2`

### Deleting Outlets : `outlet delete`

Deletes an `Outlet`.

Format: `outlet delete INDEX`

* `INDEX` refers to the displayed outlet list.
* If candidates are assigned to the deleted outlet, they are automatically unassigned.
* Auto-unassignment applies across all candidates in the address book (not only filtered/displayed candidates).

Examples:

- `outlet delete 1`

### Listing Outlets : `outlet list`

Lists all outlets.

Format: `outlet list`

### Saving the data

HireLens data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

HireLens data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, HireLens will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause HireLens to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous HireLens home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.

2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

3. Long command names<br>
   Certain command names such as `addtagcombo` and `deletetagcombo` are relatively long and not well-suited for a typist-oriented UI. These longer names were intentionally chosen as default placeholders to support a future `rebind` feature, which would allow users to map frequently used commands to shorter aliases (e.g., `filter` → `f`). However, this feature has not yet been implemented. In practice, the impact of these longer command names is limited, as they mainly apply to low-frequency operations such as tag combo and outlet-related commands.

4. Semantic ambiguity between `filter` and `find`<br>
   `filter` and `find` commands provide very similar functionality, with `filter` taking in `Tag`s or `TagCombo`s, and `find` taking in names. However, `find` works using partial matching, while `filter` works using case-insensitive full matching. While this overlap in purpose may introduce some redundancy, the commands remain functionally distinct due to their differing input types and matching strategies. As such, the potential for user confusion is limited, and both commands continue to serve valid use cases. Given this distinction, consolidating or refactoring them is considered a low priority, as the current design does not significantly impact usability.

5. Showing candidates full details require clicking<br>
   As each candidate may contain many details, the application is designed to display full information only when required, achieved by clicking on a candidate in the list to reduce visual clutter. Additionally, full details are automatically shown for certain commands such as `add` and `edit`, ensuring that relevant information is surfaced when necessary.

    However, this interaction model is not fully aligned with a typist-oriented UI, as it relies on mouse input for navigation. While this may introduce minor inefficiency for keyboard-focused users, the impact is limited since key workflows still surface the required details automatically. Enhancements to support keyboard-based navigation of candidate details are being considered for future iterations.


--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS pc/POSTAL_CODE [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd pc/123456 t/friend t/colleague`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [INDEX]… {[n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [pc/POSTAL_CODE] [t/TAG]} [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Filter** | `filter {[t/TAG] [tc/TAG_COMBO]} [t/TAG]... [tc/TAG_COMBO]... `<br> e.g., `filter t/java t/python tc/ml dev`
**Compare Candidates** | `compare INDEX INDEX`<br> e.g. `compare 1 2`
**List** | `list`
**Help** | `help`
**Exit** | `exit`
**Undo** | `undo`
**Redo** | `redo`
**List Tags** | `listtags`
**Add Tag Combo** | `addtagcombo NAME t/TAG t/TAG [t/TAG]...`<br> e.g., `addtagcombo ml dev t/python t/ml`
**Delete Tag Combo** | `deletetagcombo INDEX`<br> e.g., `deletetagcombo 1`
**List Tag Combos** | `listtagcombo`
**Add by CSV** | `addcsv PATH_TO_CSV_FILE` <br> e.g., `addcsv data/candidates.csv`
**Add Outlet** | `outlet add n/NAME a/ADDRESS pc/POSTAL_CODE` <br> e.g., `outlet add n/FinServ a/Marina Bay pc/018956`
**Edit Outlet** | `outlet edit INDEX {[n/NAME] [a/ADDRESS] [pc/POSTAL_CODE]}` <br> e.g., `outlet edit 1 a/One Raffles Place pc/048616`
**Assign Outlet** | `outlet assign CANDIDATE_INDEX [OUTLET_INDEX]` <br> e.g., `outlet assign 2 1` or `outlet assign 2`
**Unassign Outlet** | `outlet unassign CANDIDATE_INDEX` <br> e.g., `outlet unassign 2`
**Delete Outlet** | `outlet delete INDEX` <br> e.g., `outlet delete 1`
**List Outlets** | `outlet list`
