---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Prefer desktop apps over other types
* Is reasonably comfortable using CLI apps
* Wants to enter candidates’ credentials, which could be extremely large in number
* Wants to filter and compare candidates by qualifications, distances to workplaces etc
* Wants to use the program to speed up their other workflows, e.g. writing emails
* Strongly values punctuality and is strict about lateness, while still aiming to remain fair


**Value proposition**: 
* Fast and easy search/filter that can check different fields such as salary, qualifications, positions and more. 
* Pin certain entries for fast comparison or easy access.
* Take an input of a .csv file with the correct format and with relevant data and automatically add the candidates to the list. 
* Quickly filter through the candidates for those with the ideal skills (and any other factors). 
* Calculate the distance between a candidate’s address and the available offices such that the candidate can be sent to the nearest office.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                                                | I want to …​                                                                         | So that I can…​                                                     |
|----------|------------------------------------------------------------------------|--------------------------------------------------------------------------------------|---------------------------------------------------------------------|
| `* * *`  | new recruiter                                                          | auto fill-in suggestions with tooltips explaining what each field means              | correctly enter candidate information without making mistakes       |
| `* * *`  | recruiter                                                              | add candidates with their names, phone numbers, email addresses                      | easily contact them for future job openings                         |
| `* * *`  | recruiter                                                              | be able to edit contacts                                                             | fix any minor mistakes I make                                       |
| `* * *`  | recruiter                                                              | be able to delete contacts                                                           | un-track candidates I am no longer interested in                    |
| `* * *`  | recruiter                                                              | be able to filter contacts                                                           | search for tags I am interested in                                  |
| `* * *`  | recruiter                                                              | be able to view all candidates                                                       | know who I have added to the addressbook                            |
| `* * *`  | recruiter                                                              | tag candidates by their skills                                                       | categorise them by their skills                                     |
| `* * *`  | recruiter facing candidates with a variety of names                    | be able to add two candidates with the same name                                     |                                                                     |
| `* * *`  | recruiter                                                              | filter candidates by multiple tags simultaneously                                    | immediately get a shortlisted list of candidates for a job opening  |
| `* * *`  | time-pressed recruiter                                                 | be able to use a spreadsheet to add many candidates to this address book all at once | save time adding entries                                            |
| `* *`    | recruiter                                                              | have a log of recent changes made to a candidate                                     | check for mistakes                                                  |
| `* *`    | careless recruiter                                                     | be able to undo my commands                                                          | my mistakes can be amended quickly                                  |
| `* *`    | careless recruiter                                                     | be able to copy candidate details for use in another program                         | reduce my chance of mis-typing                                      |
| `* *`    | recruiter                                                              | be able to create common tag combinations                                            | reuse the same filters easily                                       |
| `* *`    | recruiter                                                              | be able to assign a status to candidates in the pipeline                             | manage the hiring progress for a particular opening                 |
| `* *`    | recruiter examining hiring options                                     | be able to compare more than one candidate with each other                           | judge correctly while avoiding manual error                         |
| `* *`    | recruiter                                                              | be able to view recently viewed candidates                                           | save time from not searching again                                  |
| `* *`    | recruiter                                                              | be able to assign a rating score to each candidate and filter by rating score        | compare in a quick and objective manner during the final selection  |
| `*`      | recruiter                                                              | have an email template popup in one click                                            | contact candidates easily                                           |
| `*`      | recruiter bulk importing data                                          | be notified if there already exists an identical record                              | don’t mistakenly add duplicates and bloat the database              |
| `*`      | time-pressed recruiter                                                 | be able to edit multiple entries at once                                             | efficiently manage large batches of candidates                      |
| `*`      | be able to see a quick summary of the most common tags in the database | understand the talent pool better                                                    |                                                                     |
| `*`      | recruiter                                                              | be able to compare candidates’ living addresses with potential work addresses        | understand how best to hire and deploy them across our many offices |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `HireLens` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add Contact**

**MSS**

1.  User chooses to add a new candidate.
2.  HireLens requests for details of the candidate.
3.  User enters necessary information, such as name, phone number, address, email and any optional tags.
4.  HireLens displays auto fill-in suggestions and formatting guidance.
5. User confirms the information entered.
6. HireLens adds the candidate to the list of saved candidates and displays the information of the new candidate added.
7. HireLens shows the updated list of the full candidate list with the new candidate added

    Use case ends.

**Extensions**

* 3a. HireLens detects missing or invalid data.

    * 3a1. HireLens informs the user of the error made and requests corrected information.
    * 3a2. User enters corrected data.
    * 
      Steps 3a1-3a2 are repeated until the data entered are correct.
      Use case resumes at step 2.

      
**Use case: Edit Contact**

**MSS**

1.  User chooses to edit the details of a candidate.
2.  HireLens requests for a minimum one parameter to modify and the index of the candidate to be modified.
3.  User provides the requested data above and the modified data. 
4.  HireLens edits the details of the candidate and displays the modified details of the candidate.
5.  HireLens shows the updated list of the candidate list with all filters intact.
 
    Use case ends.


**Use case: Filter by Tag**

**MSS**

1.  User chooses to filter the candidate list by tags.
2.  HireLens requests for a minimum one tag to filter.
3. User provides the tags to filter the candidates by. 
4. HireLens filters the candidate list and shows a message informing the user the list has been filtered successfully.
5. HireLens shows the filtered candidate list.

   Use case ends.

**Extensions**

* 4a. Filter returns empty list (no candidates match the tags given)

    * 4a1. HireLens detects that no candidates match the tags provided. 
    * 4a2. HireLens informs the user that the filter resulted in an empty list.
    * 4a3. HireLens shows the full candidate list without any filters.


*{More to be added}*

### Non-Functional Requirements

1. Efficiency 
   * System should be able to process commands such as add, edit, delete, and list within 1 second.
   * System should be able to handle at least 1000 contacts without significant performance loss
2. Capacity
   * System should be able to store at least 1000 contacts.
   * System should be able to read and enter at least 100 contacts from a csv file at a time.
3. Quality
   * System should be able to be used by a beginner who has never used the system before.
   * System should have clear error messages that explain the source of the error and how to correct the error.
4. Reliability
   * Data should persist between usage sessions.
   * Data should not be corruptible.
5. Compatibility
   * System should run on Windows, macOS, and Linux with Java 17 and above installed.

*{More to be added}*

### Glossary
*{None as of yet}*


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
