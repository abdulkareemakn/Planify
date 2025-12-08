# Planify - Requirements Document

## Project Overview

**Planify** is an Event Management System that allows administrators to create, view, and manage different types of events. The system is built with Java and JavaFX, using SQLite for data persistence.

**Target Audience:** This project is designed to be presented in a viva examination, so the codebase must be clean, well-documented, and easy to explain.

---

## Functional Requirements

### FR1: Authentication (Optional Module)

> **Note:** Authentication is designed as an optional module. A configuration flag (`AUTH_ENABLED`) allows the app to skip login and go directly to the Dashboard. All auth code remains in place for demonstration purposes.

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| FR1.1 | Admin can log in with username and password | High | Exists |
| FR1.2 | Passwords must be securely hashed (not stored in plain text) | High | Exists |
| FR1.3 | Admin can reset/change password | Medium | Exists |
| FR1.4 | Show appropriate error messages for invalid login | High | Exists |
| FR1.5 | Admin can log out and return to login screen | Medium | TODO |
| FR1.6 | Auth can be disabled via configuration flag | Medium | TODO |

### FR2: Dashboard

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| FR2.1 | Display total number of events | High | Exists (broken) |
| FR2.2 | Display count of events by type (Wedding, Birthday, Seminar) | High | Exists (broken) |
| FR2.3 | Quick navigation to View Events page | High | Exists |
| FR2.4 | Quick navigation to Create Event page | High | Exists |
| FR2.5 | Display recent events list | Low | TODO |

### FR3: Event Management

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| FR3.1 | Create new events with basic info (name, date/time, attendance) | High | Exists |
| FR3.2 | Associate events with a client (name, email, phone) | High | Exists |
| FR3.3 | Support multiple event types with type-specific fields | High | Exists |
| FR3.4 | View all events in a table/list format | High | Exists |
| FR3.5 | Delete existing events | High | Exists |
| FR3.6 | Edit existing events | Medium | TODO |
| FR3.7 | Search/filter events | Low | TODO |

### FR4: Event Types

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| FR4.1 | **Wedding Event**: bride name, groom name, photographer required | High | Exists |
| FR4.2 | **Birthday Event**: age, theme, number of kids | High | Exists |
| FR4.3 | **Seminar Event**: chief guest, speaker, topic | High | Exists |
| FR4.4 | **Custom Event**: predefined optional fields (custom field 1, custom field 2, custom field 3) | Medium | TODO |

### FR5: Venue Management

> **Note:** Venues are managed via a dropdown when creating events. Users can select an existing venue or add a new one inline.

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| FR5.1 | Select venue from dropdown when creating event | Medium | TODO |
| FR5.2 | Add new venue inline (name, address, capacity) | Medium | TODO |
| FR5.3 | Associate events with venues | Medium | TODO |

### FR6: Client Management

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| FR6.1 | Store client information with events | High | Exists |
| FR6.2 | View list of all clients | Low | TODO |
| FR6.3 | Reuse existing client for new events | Low | TODO |

---

## Non-Functional Requirements

### NFR1: Code Quality

| ID | Requirement | Priority |
|----|-------------|----------|
| NFR1.1 | Code must be simple and explainable (no advanced concepts like generics) | High |
| NFR1.2 | Clear separation of concerns (MVC pattern) | High |
| NFR1.3 | Consistent naming conventions | High |
| NFR1.4 | Well-commented code where necessary | Medium |
| NFR1.5 | No dead/unused code | High |

### NFR2: User Interface

| ID | Requirement | Priority |
|----|-------------|----------|
| NFR2.1 | Clean, modern UI design | Medium |
| NFR2.2 | Responsive feedback (loading states, error messages) | High |
| NFR2.3 | Consistent styling across all screens | Medium |
| NFR2.4 | Intuitive navigation | High |

### NFR3: Performance

| ID | Requirement | Priority |
|----|-------------|----------|
| NFR3.1 | Database operations must be asynchronous (non-blocking UI) | High |
| NFR3.2 | Application should start within 3 seconds | Low |

### NFR4: Data Integrity

| ID | Requirement | Priority |
|----|-------------|----------|
| NFR4.1 | Input validation for all forms | High |
| NFR4.2 | Prevent SQL injection (use PreparedStatements) | High |
| NFR4.3 | Handle database errors gracefully | Medium |

---

## Technical Decisions

### Confirmed Decisions

| Decision | Rationale |
|----------|-----------|
| Java 21 | Modern LTS version with useful features |
| JavaFX for UI | Standard Java GUI framework |
| SQLite for database | Simple, file-based, no server needed |
| Maven for build | Standard build tool, easy dependency management |
| FXML for layouts | Separates UI design from logic |
| Calculate stats live | Query database on-demand, always accurate, simpler code |
| Simple venue dropdown | Select/add venue when creating events, no separate venue management screen |
| Custom event with predefined fields | 3 optional custom fields, easy to explain |
| Auth as optional module | Toggle via config flag, all code stays for demonstration |
| No HikariCP | Direct connections via DriverManager, simpler for SQLite |
| Simple async helper | Custom Task wrapper without generics, easy to explain |

### Decisions to Make

| Question | Options | Decision |
|----------|---------|----------|
| UI framework/styling? | Plain JavaFX CSS / Material Design library | TBD (decide during UI rebuild) |

---

## Screens/Pages

### Screens (to be rebuilt)

1. **Login Page** - Username/password authentication (optional, can be skipped)
2. **Reset Password Page** - Change admin password
3. **Dashboard** - Statistics and quick links
4. **Events List** - Table view of all events
5. **Create Event** - Form to create new events (includes venue dropdown)

### Future Enhancements (if time permits)

6. **Edit Event** - Modify existing event
7. **Search/Filter** - Filter events by type, date, etc.

---

## Data Model

### Proposed Data Model (After Refactoring)

```
Client
├── id (int)
├── name (String)
├── email (String)
└── phoneNumber (String)

Venue
├── id (int)
├── name (String)
├── address (String)
└── capacity (int)

Event (abstract)
├── id (int)
├── name (String)
├── dateTime (LocalDateTime)
├── attendance (int)
├── eventType (String)
├── client (Client)
└── venue (Venue)

WeddingEvent extends Event
├── brideName (String)
├── groomName (String)
└── photographerRequired (boolean)

BirthdayEvent extends Event
├── age (int)
├── theme (String)
└── numberOfKids (int)

SeminarEvent extends Event
├── chiefGuest (String)
├── speaker (String)
└── topic (String)

CustomEvent extends Event
├── customField1 (String, optional)
├── customField2 (String, optional)
└── customField3 (String, optional)
```

### Changes from Current Model

- Removed `ContactInfo` class - fields moved directly into `Client`
- Simplified `Venue` - removed squareFeet, isOutdoor, hasRestrooms, basePrice
- Added `venue` reference to `Event`
- Added `CustomEvent` type with 3 optional fields

---

## Refactoring Plan

### Phase 1: Clean Foundation
1. Remove dead code (EventInfo, ConcreteEvent, Scanner methods)
2. Remove DBFX framework (deps/ folder)
3. Create simple async database helper
4. Fix Planify.java main class
5. Inline ContactInfo into Client

### Phase 2: Data Layer
1. Update database schema (add venues table, custom_event table, venue_id to events)
2. Rewrite repositories with simple async pattern
3. Implement live statistics calculation

### Phase 3: Rebuild UI (in order)
1. Login Page
2. Dashboard
3. Events List
4. Create Event (with venue dropdown and all event types)
5. Reset Password

### Phase 4: Polish
1. Add logout functionality
2. Add AppConfig for auth toggle
3. Input validation
4. Error handling
5. Update README

---

*Last Updated: December 8, 2025*
