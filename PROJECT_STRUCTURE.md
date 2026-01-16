# Healthcare Management System - Project Structure

This document provides a comprehensive explanation of the entire project structure, including the purpose and functionality of each component.

## Table of Contents

1. [Root Directory Structure](#root-directory-structure)
2. [Source Code Organization](#source-code-organization)
3. [Database Layer](#database-layer)
4. [Application Layers](#application-layers)
5. [UI Components](#ui-components)
6. [Utility Classes](#utility-classes)
7. [Configuration](#configuration)
8. [Documentation](#documentation)

---

## Root Directory Structure

```
Healthcare Management System/
├── database/              # Database scripts and setup
├── docs/                  # Project documentation and diagrams
├── src/                   # Source code
├── target/                # Compiled classes (generated)
├── pom.xml                # Maven configuration
├── README.md              # Main project documentation
└── system_logs.log        # Application logs
```

---

## Source Code Organization

### Main Package Structure

```
src/main/java/org/example/healthcaremanagementsystem/
├── config/                # Configuration classes
├── model/                 # Data Transfer Objects (DTOs)
├── dao/                   # Data Access Objects
├── service/               # Business logic layer
├── controller/            # JavaFX controllers (MVC)
├── util/                  # Utility classes
├── HealthcareApplication.java  # Main application entry point
└── Launcher.java          # Application launcher
```

---

## Database Layer

### Location: `database/`

#### `schema.sql`
**Purpose**: Defines the complete database schema for PostgreSQL.

**Contents**:
- Table definitions (8 tables: departments, patients, doctors, appointments, prescriptions, prescription_items, patient_feedback, medical_inventory)
- Primary keys and foreign key constraints
- Check constraints for data validation
- Indexes for performance optimization
- Table comments for documentation

**Key Features**:
- Normalized to 3NF (Third Normal Form)
- Comprehensive indexing strategy
- Referential integrity constraints
- Data type optimization

#### `sample_data.sql`
**Purpose**: Provides sample data for testing and demonstration.

**Contents**:
- Sample departments (Cardiology, Neurology, etc.)
- Sample patients with Ghanaian names and addresses
- Sample doctors with specializations
- Sample appointments
- Sample prescriptions and inventory items

**Note**: All data follows Ghanaian localization (names, addresses, phone numbers).

#### `create_database.sql`
**Purpose**: Script to create the database.

**Contents**:
- Database creation command
- Connection instructions

#### `README.md`
**Purpose**: Database setup instructions.

**Contents**:
- Step-by-step setup guide
- Connection configuration
- Script execution order

---

## Application Layers

### 1. Configuration Layer (`config/`)

#### `DatabaseConfig.java`
**Purpose**: Manages PostgreSQL database connections.

**Responsibilities**:
- Singleton pattern for connection management
- Connection pooling
- Connection lifecycle management
- Connection testing
- Error handling

**Key Methods**:
- `getConnection()` - Returns database connection
- `closeConnection()` - Closes connection
- `testConnection()` - Tests database connectivity

#### `MongoDBConnection.java`
**Purpose**: MongoDB connection configuration (for NoSQL integration).

**Responsibilities**:
- MongoDB client setup
- Database and collection access
- Connection management for unstructured data

---

### 2. Model Layer (`model/`)

**Purpose**: Data Transfer Objects (DTOs) representing database entities.

**Classes**:
- `Patient.java` - Patient entity with all demographic fields
- `Doctor.java` - Doctor entity with credentials and department
- `Department.java` - Hospital department information
- `Appointment.java` - Appointment scheduling entity
- `Prescription.java` - Prescription records
- `PrescriptionItem.java` - Individual medications in prescriptions
- `PatientFeedback.java` - Patient feedback and ratings
- `MedicalInventory.java` - Medical supplies and equipment
- `MedicalLog.java` - Medical logs (for NoSQL storage)

**Each Model Class Contains**:
- Private fields matching database columns
- Constructors (default, parameterized)
- Getter and setter methods
- `equals()`, `hashCode()`, and `toString()` methods
- Validation logic where applicable

**Design Pattern**: DTO (Data Transfer Object) pattern for clean data transfer between layers.

---

### 3. Data Access Layer (`dao/`)

**Purpose**: Abstracts database operations using DAO pattern.

**Structure**:
- **Interfaces**: Define contract for data operations (e.g., `PatientDAO.java`)
- **Implementations**: Concrete JDBC implementations (e.g., `PatientDAOImpl.java`)

#### DAO Interfaces
Each interface defines standard operations:
- `create()` / `save()` - Insert new record
- `findById()` - Retrieve by primary key
- `findAll()` - Retrieve all records
- `update()` - Update existing record
- `delete()` - Delete record
- Specialized methods: `searchByName()`, `findByEmail()`, etc.

#### DAO Implementations
**Key Features**:
- JDBC PreparedStatements for SQL injection prevention
- Parameterized queries
- ResultSet mapping to DTOs
- Exception handling
- Resource cleanup (try-with-resources)
- Pagination support
- Search functionality

**DAO Classes**:
- `PatientDAO` / `PatientDAOImpl` - Patient data operations
- `DoctorDAO` / `DoctorDAOImpl` - Doctor data operations
- `AppointmentDAO` / `AppointmentDAOImpl` - Appointment operations
- `DepartmentDAO` / `DepartmentDAOImpl` - Department operations
- `PrescriptionDAO` / `PrescriptionDAOImpl` - Prescription operations
- `InventoryDAO` / `InventoryDAOImpl` - Inventory operations
- `MedicalLogDAO` - Medical log operations (NoSQL)

**Design Pattern**: DAO (Data Access Object) pattern for database abstraction.

---

### 4. Service Layer (`service/`)

**Purpose**: Business logic layer that coordinates between controllers and DAOs.

**Responsibilities**:
- Business rule validation
- Data transformation
- Caching coordination
- Sorting operations
- Error handling and logging
- Transaction management

#### `PatientService.java`
**Features**:
- Patient creation with validation
- Search by name with pagination
- Caching integration
- Sorting by name
- Total count and search count methods

#### `DoctorService.java`
**Features**:
- Doctor management operations
- Department relationship handling
- Status management
- Search and pagination

#### `AppointmentService.java`
**Features**:
- Appointment scheduling
- Date range queries
- Patient/doctor appointment history
- Status management
- Caching for performance

**Design Pattern**: Service layer pattern for business logic separation.

---

### 5. Controller Layer (`controller/`)

**Purpose**: JavaFX controllers handling UI interactions (MVC pattern).

**Architecture**: Controllers are lightweight coordinators that delegate to specialized handlers, following SOLID principles. This reduces controller complexity from 400-800 lines to 200-300 lines.

#### Base Controllers

##### `base/BaseController.java`
**Purpose**: Base class providing common functionality.

**Features**:
- Success/error alert dialogs
- Confirmation dialogs
- Reusable UI operations

#### Main Controllers

##### `MainController.java`
**Purpose**: Main dashboard controller.

**Features**:
- Navigation between modules
- Dashboard statistics display
- Module launching

##### `PatientController.java`
**Purpose**: Patient management UI controller.

**Features**:
- Form handling (create, update, delete)
- Table view management
- Search functionality
- Pagination
- Input validation
- **Line Count**: ~227 lines (reduced from 383 through refactoring)
- **Uses**: `PatientFormHandler`, `PatientControllerHandler`, `PatientSetupHandler`, `PaginationHandler`, `PatientValidator`

**Architecture**: Delegates all operations to specialized handlers following SOLID principles.

##### `DoctorController.java`
**Purpose**: Doctor management UI controller.

**Features**:
- Doctor CRUD operations
- Department selection
- Status management
- Search and pagination
- **Line Count**: ~234 lines (reduced from 439 through refactoring)
- **Uses**: `DoctorFormHandler`, `DoctorControllerHandler`, `DoctorSetupHandler`, `PaginationHandler`, `DoctorValidator`

**Architecture**: Delegates all operations to specialized handlers following SOLID principles.

##### `AppointmentController.java`
**Purpose**: Appointment scheduling controller.

**Features**:
- Appointment scheduling
- Patient/doctor selection dialogs
- Date/time validation
- Status management
- Search functionality
- **Line Count**: ~317 lines (reduced from 566 through refactoring)
- **Uses**: `AppointmentFormHandler`, `AppointmentControllerHandler`, `AppointmentSetupHandler`, `PaginationHandler`, `AppointmentValidator`

**Architecture**: Delegates all operations to specialized handlers following SOLID principles.

##### `PerformanceController.java`
**Purpose**: Performance and analytics dashboard.

**Features**:
- System statistics display
- Cache performance metrics
- Query optimization results
- Charts and visualizations

#### Specialized Handlers

##### `handler/` Package
**Purpose**: Specialized handler classes following SOLID principles.

**Base Handlers**:
- `BaseSetupHandler.java` - Common setup operations (button actions, pagination, button states)
- `BaseControllerHandler.java` - Base class for controller handlers with common search/pagination logic

**Form Handlers**:
- `PatientFormHandler.java` - Patient form operations (create, populate, clear)
- `DoctorFormHandler.java` - Doctor form operations (create, populate, clear)
- `AppointmentFormHandler.java` - Appointment form operations (create, populate, clear)
- `FormHandler.java` - Generic form clearing utilities

**Controller Handlers**:
- `PatientControllerHandler.java` - Patient CRUD, search, and data loading operations
- `DoctorControllerHandler.java` - Doctor CRUD, search, and data loading operations
- `AppointmentControllerHandler.java` - Appointment CRUD, search, and data loading operations

**Setup Handlers**:
- `PatientSetupHandler.java` - Patient UI setup (combo boxes, placeholders, validation, table selection)
- `DoctorSetupHandler.java` - Doctor UI setup (combo boxes, placeholders, validation, table selection)
- `AppointmentSetupHandler.java` - Appointment UI setup (combo boxes, placeholders, validation, table selection)

**Utility Handlers**:
- `TableSetupHandler.java` - Table column configuration

##### `pagination/PaginationHandler.java`
**Purpose**: Manages pagination state and controls.

**Features**:
- Page navigation (first, previous, next, last)
- Page size management
- Total records tracking
- Control state management

##### `validator/` Package
**Purpose**: Input validation utilities.

**Classes**:
- `InputValidator.java` - Generic validation (email, phone)
- `PatientValidator.java` - Patient-specific validation
- `DoctorValidator.java` - Doctor-specific validation
- `AppointmentValidator.java` - Appointment-specific validation

#### Dialog Controllers

##### `SearchableSelectionDialog.java`
**Purpose**: Reusable searchable dialog for selecting patients/doctors.

**Features**:
- Table view with search
- Filter functionality
- Double-click selection
- Generic implementation

##### `AppointmentDetailsController.java`
**Purpose**: Displays detailed appointment information.

**Features**:
- Appointment details view
- Patient and doctor information
- Prescription linking

#### Other Controllers

- `ConsultationController.java` - Clinical consultation workflow
- `HistoryController.java` - Patient/doctor history
- `InventoryController.java` - Medical inventory management
- `SystemLogsController.java` - System log viewer
- `PatientHistoryDashboardController.java` - Patient history analytics
- `DoctorHistoryDashboardController.java` - Doctor history analytics

**Design Pattern**: MVC (Model-View-Controller) pattern.

---

## UI Components

### Location: `src/main/resources/org/example/healthcaremanagementsystem/`

#### FXML Files (JavaFX UI Definitions)

**Main Views**:
- `main-view.fxml` - Main dashboard with navigation
- `patient-management.fxml` - Patient management interface
- `doctor-management.fxml` - Doctor management interface
- `appointment-management.fxml` - Appointment scheduling interface

**Analytics & Reports**:
- `performance-analytics.fxml` - Performance dashboard
- `patient-history-dashboard.fxml` - Patient history view
- `doctor-history-dashboard.fxml` - Doctor history view

**Specialized Views**:
- `appointment-details.fxml` - Appointment detail view
- `consultation-view.fxml` - Clinical consultation interface
- `inventory.fxml` - Inventory management
- `system-logs.fxml` - System log viewer
- `history-view.fxml` - General history view
- `reports.fxml` - Reports interface

#### Styles
- `styles.css` - Cascading Style Sheets for UI styling

**UI Features**:
- Modern, professional design
- Responsive layouts
- Form validation feedback
- Table views with sorting
- Search bars
- Pagination controls
- Button groups for actions

---

## Utility Classes (`util/`)

### `CacheManager.java`
**Purpose**: In-memory caching for performance optimization.

**Features**:
- HashMap-based caching (O(1) lookup)
- Separate caches for patients, doctors, departments, appointments
- Query result caching
- TTL (Time To Live) for cache entries
- Cache invalidation strategies
- Cache hit/miss statistics
- Thread-safe implementation (ConcurrentHashMap)

**Algorithm**: Hashing for fast data retrieval

### `SortingUtil.java`
**Purpose**: Sorting algorithm implementations.

**Algorithms**:
- **QuickSort**: O(n log n) average, O(n²) worst case
- **MergeSort**: O(n log n) stable sorting
- **TimSort**: Java's built-in hybrid algorithm
- Comparator-based sorting for flexibility

**Usage**: Used in service layer for sorting patient/doctor lists

### `SearchUtil.java`
**Purpose**: Search algorithm implementations.

**Algorithms**:
- **Linear Search**: O(n) for unsorted data
- **Binary Search**: O(log n) for sorted data
- Case-insensitive string search
- Multi-criteria search with predicates

**Usage**: Used throughout controllers for search functionality

### `PerformanceMonitor.java`
**Purpose**: Performance measurement and monitoring.

**Features**:
- Query execution time tracking
- Performance metrics collection
- Logging performance data

### `SystemLogger.java`
**Purpose**: Application-wide logging system.

**Features**:
- Singleton pattern
- Log levels (INFO, ERROR, WARNING)
- File and console logging
- Categorized logging (PATIENT, DOCTOR, SYSTEM, etc.)

---

## Configuration

### `pom.xml`
**Purpose**: Maven project configuration.

**Dependencies**:
- JavaFX Controls (21.0.6)
- JavaFX FXML (21.0.6)
- PostgreSQL JDBC Driver
- Maven Compiler Plugin
- JavaFX Maven Plugin

**Java Version**: 23

### `module-info.java`
**Purpose**: Java module system configuration.

**Exports**:
- Model package
- Controller package

**Opens**:
- Controller package for JavaFX FXML
- Model package for JavaFX binding

**Requires**:
- JavaFX modules
- Java SQL module

---

## Documentation

### Location: `docs/`

#### `DIAGRAMS.md`
**Purpose**: Comprehensive system diagrams using Mermaid syntax.

**Contents**:
- Conceptual ERD
- Logical ERD
- Physical ERD
- System Architecture Diagram
- Data Flow Diagram
- Class Diagram
- Sequence Diagrams
- Use Case Diagrams
- Component Diagrams
- Performance Optimization Flow

#### `NOSQL_DESIGN.md`
**Purpose**: NoSQL database design documentation.

**Contents**:
- Comparison of relational vs NoSQL
- MongoDB document structure
- Integration strategy
- Use cases for unstructured data
- Query patterns

#### Visual Diagrams
- `CONCEPTUAL DATA MODEL.png`
- `LOGICAL MODEL.png`
- `PHYSICAL DATA MODEL.png`
- `ER Diagram.png`

### `README.md`
**Purpose**: Main project documentation.

**Contents**:
- Project overview
- Features list
- Technology stack
- Setup instructions
- Usage guide
- Troubleshooting
- Requirements compliance

---

## Application Entry Points

### `HealthcareApplication.java`
**Purpose**: Main JavaFX application class.

**Responsibilities**:
- Application initialization
- Database connection testing
- FXML loading
- Stage setup
- Application lifecycle management

### `Launcher.java`
**Purpose**: Application launcher (required for JavaFX with Maven).

**Responsibilities**:
- Launches `HealthcareApplication`
- Handles JavaFX module path

---

## Build Output

### `target/`
**Purpose**: Maven build output directory (generated).

**Contents**:
- Compiled classes
- Resources
- Generated sources
- Test classes

---

## Design Patterns Used

1. **MVC (Model-View-Controller)**: Separation of UI, business logic, and data
2. **DAO (Data Access Object)**: Database abstraction
3. **DTO (Data Transfer Object)**: Clean data transfer
4. **Singleton**: Database config, cache manager, logger
5. **Factory**: Service layer creates DAOs
6. **Strategy**: Different sorting/searching algorithms
7. **Template Method**: Base controller pattern

---

## SOLID Principles Implementation

1. **Single Responsibility**: 
   - Each class has one clear purpose
   - Controllers delegate to specialized handlers (FormHandler, ControllerHandler, SetupHandler)
   - Base handlers eliminate code duplication

2. **Open/Closed**: 
   - Extensible through interfaces (DAO pattern)
   - Base handlers allow extension without modification

3. **Liskov Substitution**: 
   - Interface implementations are interchangeable
   - BaseControllerHandler can be extended by specific handlers

4. **Interface Segregation**: 
   - Focused interfaces (PatientDAO, DoctorDAO, etc.)
   - Handlers have specific responsibilities (form, setup, operations)

5. **Dependency Inversion**: 
   - Depend on abstractions (DAO interfaces), not concretions
   - Controllers depend on handler abstractions, not implementations

---

## Data Flow

```
User Input (JavaFX UI)
    ↓
Controller (handles events, validation)
    ↓
Service Layer (business logic, caching, sorting)
    ↓
DAO Layer (database operations)
    ↓
Database (PostgreSQL)
```

**Reverse Flow** (for data retrieval):
```
Database
    ↓
DAO (ResultSet → DTO)
    ↓
Service (caching, sorting)
    ↓
Controller (ObservableList)
    ↓
JavaFX UI (Table View, Labels, etc.)
```

---

## Key Features by Layer

### Presentation Layer (Controllers)
- User interaction handling
- Form validation
- UI state management
- Error display

### Business Layer (Services)
- Business rule enforcement
- Caching coordination
- Data sorting
- Transaction management

### Data Layer (DAOs)
- SQL query execution
- ResultSet mapping
- Connection management
- Error handling

### Utility Layer
- Performance optimization
- Algorithm implementations
- Logging
- Common operations

---

This structure ensures:
- **Maintainability**: Clear separation of concerns
- **Scalability**: Easy to add new features
- **Testability**: Each layer can be tested independently
- **Reusability**: Utility classes and handlers are reusable
- **Professional Standards**: Follows industry best practices

