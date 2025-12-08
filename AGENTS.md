# Agent Guidelines for Planify

## Project Context
This project is being refactored for a friend to present in a viva examination. The original codebase was developed with AI assistance but needs cleanup and simplification.

### Refactoring Goals
- **Simplicity First**: Avoid advanced Java concepts (e.g., generics) that are hard to explain in a viva
- **Clean Architecture**: Well-organized, maintainable code with clear separation of concerns
- **Easy to Understand**: Code should be straightforward enough for someone to explain confidently
- **Collaborative Approach**: Discuss architectural decisions before implementing major changes

### Development Philosophy
- Prefer simple, explicit code over clever abstractions
- Use straightforward patterns that are easy to explain
- Focus on readability and maintainability
- Keep business logic clear and testable

## Build & Test Commands
- **Build**: `mvn clean install`
- **Run**: `mvn javafx:run`
- **Test**: `mvn test` (run all tests)
- **Single test**: `mvn test -Dtest=ClassName#methodName`
- **Compile**: `mvn compile`

## Code Style

### Package Structure
- `controller/`: JavaFX FXML controllers (implement Initializable)
- `model/`: Domain models (abstract Event with concrete subclasses)
- `repository/`: DAO classes for database operations
- `util/`: Utility classes (Authentication, SceneManager, Statistics, PasswordUtil)
- `deps/`: Database framework dependencies (DBUtils, DBConnectionPool)

### Imports & Formatting
- Java 21 features preferred (pattern matching, text blocks, records where applicable)
- Standard import order: java.*, javax.*, javafx.*, org.example.planifyfx.*
- Use text blocks (""") for multi-line SQL queries
- 4-space indentation

### Database Operations
- Use DBUtils async methods: `executeUpdateAsync`, `executeInsertWithGeneratedKeyAsync`, `executeQueryAsync`
- Always use try-with-resources for Connection, PreparedStatement, ResultSet
- HikariCP connection pool via `DatabaseUtil.getConnection()`
- PreparedStatements for all SQL (never string concatenation)

### Error Handling
- printStackTrace() for async database errors
- Try-catch blocks for synchronous database operations
- Display user-friendly error messages in UI via error labels
- RuntimeException wrapping for critical failures (e.g., password hashing)

### Naming Conventions
- Classes: PascalCase (Event, LoginPageController, EventRepository)
- Methods: camelCase (handleLogin, fetchAllEvents, showError)
- FXML fields: camelCase with @FXML annotation (usernameField, passwordField)
- Constants: UPPER_SNAKE_CASE (ITERATIONS, KEY_LENGTH)
- Database columns: snake_case (event_id, client_name)

### Type Safety
- Use Java 21 pattern matching: `if (event instanceof WeddingEvent weddingEvent)`
- Abstract methods for polymorphic behavior
- Explicit null checks for ResultSet nullable columns: `if (rs.wasNull())`
