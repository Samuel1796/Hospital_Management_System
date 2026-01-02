# Healthcare Management System

A comprehensive database-driven hospital management system built with JavaFX, PostgreSQL, and JDBC. This application demonstrates professional software architecture following MVC, DAO, DTO patterns and SOLID principles.

## Project Overview

This Healthcare Management System is designed to manage hospital operations including patient registration, doctor management, appointment scheduling, prescription management, and medical inventory tracking. The system implements advanced database concepts including normalization, indexing, caching, and performance optimization.

## Features

### Core Functionality
- **Patient Management**: Complete CRUD operations for patient records
- **Doctor Management**: Manage doctor information and department assignments
- **Appointment Scheduling**: Schedule and manage patient-doctor appointments
- **Prescription Management**: Create and manage prescriptions with medication items
- **Medical Inventory**: Track medical supplies, medications, and equipment
- **Patient Feedback**: Collect and manage patient feedback and ratings

### Technical Features
- **Database Normalization**: Schema normalized to Third Normal Form (3NF)
- **Indexing**: Optimized indexes on frequently searched columns
- **Caching**: In-memory caching using HashMap for fast data retrieval
- **Search Optimization**: Case-insensitive search with database indexing
- **Sorting Algorithms**: Implementation of QuickSort and MergeSort
- **Parameterized Queries**: SQL injection prevention using JDBC PreparedStatements
- **MVC Architecture**: Separation of concerns with Model-View-Controller pattern
- **DAO Pattern**: Data Access Object pattern for database abstraction
- **DTO Pattern**: Data Transfer Objects for clean data transfer between layers

## Technology Stack

- **Java**: 23
- **JavaFX**: 21.0.6 (UI Framework)
- **PostgreSQL**: Database Management System
- **JDBC**: Database connectivity
- **Maven**: Build and dependency management

## Project Structure

```
Healthcare Management System/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/healthcaremanagementsystem/
│   │   │       ├── config/          # Database configuration
│   │   │       ├── model/           # DTO classes (Patient, Doctor, etc.)
│   │   │       ├── dao/              # Data Access Object interfaces and implementations
│   │   │       ├── service/         # Business logic layer
│   │   │       ├── controller/      # JavaFX controllers (MVC)
│   │   │       ├── util/            # Utility classes (Caching, Sorting, Searching)
│   │   │       └── HealthcareApplication.java
│   │   └── resources/
│   │       └── org/example/healthcaremanagementsystem/
│   │           └── *.fxml           # JavaFX UI definitions
│   └── test/
├── database/
│   ├── schema.sql                   # Database schema with indexes
│   └── sample_data.sql              # Sample data for testing
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## Prerequisites

1. **Java Development Kit (JDK)**: Version 23 or higher
2. **PostgreSQL**: Version 12 or higher
3. **Maven**: Version 3.6 or higher
4. **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

## Database Setup

### Step 1: Install PostgreSQL
Ensure PostgreSQL is installed and running on your system.

### Step 2: Create Database
```sql
CREATE DATABASE healthcare_db;
```

### Step 3: Configure Database Connection
Update the database connection settings in `src/main/java/org/example/healthcaremanagementsystem/config/DatabaseConfig.java`:

```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/healthcare_db";
private static final String DB_USER = "postgres";  // Your PostgreSQL username
private static final String DB_PASSWORD = "postgres";  // Your PostgreSQL password
```

### Step 4: Run Database Scripts
Execute the SQL scripts in order:

1. **Create Schema**:
   ```bash
   psql -U postgres -d healthcare_db -f database/schema.sql
   ```

2. **Insert Sample Data**:
   ```bash
   psql -U postgres -d healthcare_db -f database/sample_data.sql
   ```

Alternatively, you can use a PostgreSQL client like pgAdmin or DBeaver to execute these scripts.

## Building and Running the Application

### Using Maven

1. **Build the project**:
   ```bash
   mvn clean compile
   ```

2. **Run the application**:
   ```bash
   mvn javafx:run
   ```

### Using IDE

1. **Import the project** into your IDE (IntelliJ IDEA, Eclipse, etc.)
2. **Build the project** using Maven
3. **Run** `HealthcareApplication.java` as the main class

### Using Command Line

1. **Compile**:
   ```bash
   javac --module-path <path-to-javafx> --add-modules javafx.controls,javafx.fxml <source-files>
   ```

2. **Run**:
   ```bash
   java --module-path <path-to-javafx> --add-modules javafx.controls,javafx.fxml org.example.healthcaremanagementsystem.HealthcareApplication
   ```

## Usage Guide

### Patient Management
1. Click **"Patient Management"** from the main menu
2. **Create**: Fill in patient details and click "Create"
3. **Search**: Enter patient name in search field and click "Search"
4. **Update**: Select a patient from the table, modify details, and click "Update"
5. **Delete**: Select a patient and click "Delete" (with confirmation)

### Doctor Management
1. Click **"Doctor Management"** from the main menu
2. Similar CRUD operations as Patient Management

### Appointment Management
1. Click **"Appointment Management"** from the main menu
2. Schedule appointments by selecting patient and doctor
3. View appointment history and status

## Database Schema

The database schema includes the following entities:
- **departments**: Hospital departments
- **patients**: Patient information
- **doctors**: Doctor credentials and assignments
- **appointments**: Patient-doctor appointments
- **prescriptions**: Prescription records
- **prescription_items**: Medications in prescriptions
- **patient_feedback**: Patient feedback and ratings
- **medical_inventory**: Medical supplies and equipment

All tables are normalized to 3NF with appropriate foreign key constraints and indexes for optimal performance.

## Performance Optimization

### Indexing
- Indexes on frequently searched columns (patient name, doctor name, appointment date)
- Composite indexes for common query patterns
- Foreign key indexes for join operations

### Caching
- In-memory caching using HashMap for frequently accessed data
- Cache invalidation on data updates
- Time-to-live (TTL) for cache entries

### Query Optimization
- Parameterized queries for SQL injection prevention
- Efficient join operations
- Proper use of database indexes

## SOLID Principles Implementation

- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Extensible through interfaces (DAO pattern)
- **Liskov Substitution**: Interface implementations are interchangeable
- **Interface Segregation**: Focused interfaces (PatientDAO, DoctorDAO, etc.)
- **Dependency Inversion**: Depend on abstractions (DAO interfaces), not concretions

## Testing

The application includes sample data for testing. You can:
1. Test CRUD operations for all entities
2. Test search functionality with various criteria
3. Test caching performance
4. Verify database constraints and referential integrity

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running: `pg_isready`
- Check database credentials in `DatabaseConfig.java`
- Ensure database `healthcare_db` exists

### Build Issues
- Ensure Java 23 is installed: `java -version`
- Verify Maven is configured: `mvn -version`
- Clean and rebuild: `mvn clean install`

### Runtime Issues
- Check JavaFX module path configuration
- Verify all dependencies are downloaded
- Check console for error messages

## Future Enhancements

- [ ] NoSQL integration for unstructured data (patient notes)
- [ ] Performance monitoring dashboard
- [ ] Advanced reporting and analytics
- [ ] User authentication and authorization
- [ ] Email notifications for appointments
- [ ] Export functionality (PDF, Excel)

## Contributing

This is an educational project demonstrating database design, JavaFX application development, and software architecture principles.

## License

This project is for educational purposes.

## Author

Healthcare Management System Team

## Acknowledgments

- PostgreSQL Community
- JavaFX Team
- Maven Community

---

**Note**: This application is designed for educational purposes to demonstrate database design, JavaFX development, and software engineering principles. For production use, additional security, error handling, and testing would be required.

