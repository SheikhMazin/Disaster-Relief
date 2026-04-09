# Disaster Relief System
**ENSF 380 — Individual Assignment 2**
**Author:** Sheikh Muhammad Mazin
**UCID:** 30243457

---

## Overview

The Disaster Relief System is a Java Swing GUI application that allows relief workers to manage disaster victims, supplies, inquiries, skills, and locations. All data is persisted in a PostgreSQL database and every user-driven change is logged to `data/action_log.txt`. Fatal errors are logged to `data/errorlog.txt`.

---

## Project Structure

```
30243457/
├── src/
│   └── main/
│       ├── java/
│       │   └── edu/ucalgary/oop/     ← all source .java files
│       └── resources/
│           ├── db.config             ← database credentials
│           └── available_requirements.ser  ← cultural/religious options
├── test/
│   └── edu/ucalgary/oop/             ← all test .java files
├── data/                             ← output files (empty on submission)
├── UML.pdf                           ← UML class diagram
└── README.md
```

---

## Prerequisites

- Java 21
- PostgreSQL running on `localhost:5432`
- Database: `ensf380project`
- PostgreSQL JDBC driver (`postgresql-42.x.x.jar`)
- JUnit 4 + Hamcrest (for running tests)

---

## Database Setup

The database must be running with the following credentials:

```
URL:      jdbc:postgresql://localhost/ensf380project
Username: oop
Password: ucalgary
```

These are stored in `src/main/resources/db.config` — one value per line:
```
jdbc:postgresql://localhost/ensf380project
oop
ucalgary
```

---

## Using the Application

When the application starts it connects to the database, loads all data, and opens the main window. The navigation bar at the top has five sections:

### 👤 Victims
- Displays a table of all active (non-deleted) disaster victims
- **Add Victim** — enter first name, last name, gender, and either a date of birth or approximate age
- **View Details** — opens a tabbed dialog for the selected victim with:
    - **Info** — view and edit name, gender, comments, date of birth / approximate age
    - **Medical Records** — view existing records, add new ones with location, treatment details, and date
    - **Family** — view family connections, add relationships to other victims
    - **Requirements** — view and manage cultural/religious requirements loaded from `available_requirements.ser`
    - **Skills** — view and manage registered skills (Medical, Language, Trade)
- **Soft Delete** — hides the victim from all views but keeps their data in the database
- **Hard Delete** — permanently removes the victim and all associated data with a confirmation dialog

### 📦 Supplies
- Displays all supplies in the inventory
- **Add Supply** — enter type, quantity, and optionally mark as perishable with an expiry date
- **Allocate** — assign a non-expired supply to a victim; expired supplies are not shown
- A warning is shown when expired supplies exist in inventory
- Existing supplies can be updated

### 🔍 Inquiries
- Displays all logged inquiries about missing persons
- **Add Inquiry** — enter inquirer details (first name, last name, phone, info), select the missing person, enter inquiry date and information, and optionally select a last known location
- Existing inquiries can be updated

### ⚡ Skills
- Search for victims by skill category: **Medical**, **Language**, or **Trade**
- Select a category from the dropdown and click **Search**
- Results show victim name, skill category, proficiency level, and skill-specific details
- Soft-deleted victims are excluded from all skill search results

### 📍 Locations
- Displays all relief locations loaded from the database (ID, name, address)
- Select a location from the dropdown and click **Search** to see all victims registered at that location

### ✖ Exit
- Prompts for confirmation before closing the application

---

## Logging

**Action log** (`data/action_log.txt`) — records every successful user-driven add, update, or delete. Format:
```
[2026-04-09] ADDED disaster victim | ID: 5 | Name: Jane Smith
[2026-04-09] UPDATED supply | ID: 3 | Type: Water Bottle | Quantity: 10
[2026-04-09] SOFT DELETED disaster victim | ID: 2 | Name: Bob Jones
```

**Error log** (`data/errorlog.txt`) — records fatal unrecoverable errors (e.g., database connection failure). The application exits cleanly after writing to this file.

---

## Cultural/Religious Requirements

Requirements are loaded at startup from `src/main/resources/available_requirements.ser`. This file contains a serialized `CulturalOptions` object with a `HashMap<String, Set<String>>` mapping requirement types (e.g., "dietary restrictions") to valid options (e.g., "halal", "kosher", "vegetarian"). If the file cannot be found or read, the application prints an error and exits.

---

## Design Patterns Used

- **Singleton** — `ActionLogger` and `DatabaseManager` use lazy-initialized singletons
- **MVC** — UI classes (`*UI.java`) are the View, `ReliefController` is the Controller, domain classes are the Model
- **Dependency Inversion** — `DataRepository` interface allows `MockDataRepository` to be injected in tests without a real database

---

## Testing Notes

- All tests use `MockDataRepository` — no database connection required
- Tests are located in `test/edu/ucalgary/oop/`
- One test file per class as required by the deliverables
- Tests follow JUnit 4 with AAA structure, single assertion per test, and meaningful names
