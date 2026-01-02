# Database Setup Instructions

## Quick Setup

### Step 1: Create Database
```bash
psql -U postgres -c "CREATE DATABASE healthcare_db;"
```

### Step 2: Run Schema Script
```bash
psql -U postgres -d healthcare_db -f schema.sql
```

### Step 3: Insert Sample Data
```bash
psql -U postgres -d healthcare_db -f sample_data.sql
```

## Using pgAdmin or DBeaver

1. Create a new database named `healthcare_db`
2. Execute `schema.sql` to create tables and indexes
3. Execute `sample_data.sql` to insert sample data

## Verify Setup

Run this query to verify the setup:
```sql
SELECT 
    (SELECT COUNT(*) FROM patients) as patient_count,
    (SELECT COUNT(*) FROM doctors) as doctor_count,
    (SELECT COUNT(*) FROM departments) as department_count,
    (SELECT COUNT(*) FROM appointments) as appointment_count;
```

Expected results:
- patient_count: 10
- doctor_count: 8
- department_count: 6
- appointment_count: 10

