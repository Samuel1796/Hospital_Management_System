# NoSQL Data Model Design - Medical Logs

## Overview

This document outlines the NoSQL data model design for the **Medical Logs** feature in the Hospital Management System. While the core entitles (Patients, Doctors, Appointments) are stored in PostgreSQL, the medical activity logs are stored in a NoSQL database (MongoDB). This allows for high-volume, append-only logging of varying structure if needed in the future, although the current implementation uses a structured log format.

## Why NoSQL for Medical Logs?

### 1. **High Volume Write Throughput**
*   Logs are generated frequently (audits, history tracking, notes).
*   NoSQL databases like MongoDB are optimized for high ingestion rates.

### 2. **Scalability**
*   Logs grow indefinitely over time.
*   Horizontal scaling (sharding) is easier with document stores compared to relational databases.

### 3. **Flexible Content**
*   While the current `MedicalLog` has a `content` field, in the future, we may want to store complex objects (JSON) for different types of logs (e.g., "Vital Signs" vs "Consultation Notes") without altering the table schema.

## Data Model Design

### Conceptual Model (Medical Log)

A `MedicalLog` represents a single event or record associated with a patient and a doctor.

*   **Subject**: Patient
*   **Author**: Doctor
*   **Action**: What happened (e.g., "Consultation", "Prescription", "Lab Result")
*   **Content**: Details of the event
*   **Timestamp**: When it happened

### Logical Data Model (Document Structure)

The basic document structure representing the `MedicalLog` class.

**Collection Name**: `medical_logs`

```json
{
  "_id": "ObjectId",
  "patientId": "Integer (FK to PostgreSQL)",
  "doctorId": "Integer (FK to PostgreSQL)",
  "action": "String",
  "content": "String",
  "timestamp": "Date"
}
```

### Physical Data Model (MongoDB)

Implementation details for the MongoDB collection.

#### Collection: `medical_logs`

| Field | Type | Description | Indexing |
|-------|------|-------------|----------|
| `_id` | `ObjectId` | Unique Mongo ID | PK (Default) |
| `patientId` | `int` | Refers to `patients.patient_id` in SQL | Indexed (Ascending) |
| `doctorId` | `int` | Refers to `doctors.doctor_id` in SQL | Indexed (Ascending) |
| `action` | `string` | Type of log activity | - |
| `content` | `string` | Free text details | Text Index (optional) |
| `timestamp` | `date` | Log creation time | Indexed (Descending) |

#### Example Document

```json
{
  "_id": { "$oid": "65b12a...7f" },
  "patientId": 101,
  "doctorId": 5,
  "action": "Consultation",
  "content": "Patient reported mild fever and headaches. Prescribed Paracetamol.",
  "timestamp": { "$date": "2024-01-15T14:30:00Z" }
}
```

## Hybrid Architecture & Integration

The system uses a **Polyglot Persistence** architecture:

1.  **PostgreSQL**: Stores relational data (Patients, Doctors, Appointments, Billing).
2.  **MongoDB**: Stores historical logs and medical notes.

### Data Consistency
*   **Patient Existence**: Before creating a log, the application checks if the `patientId` exists in PostgreSQL (Service Layer validation).
*   **Doctor Existence**: similarly checked.
*   **Deletions**: If a Patient is deleted in PostgreSQL, a cascading cleanup *should* ideally run for MongoDB logs, or they can be kept for archival purposes.

### Java Implementation (`MedicalLog.java`)

The application maps the JSON document directly to the `MedicalLog` POJO.

```java
public class MedicalLog {
    private String id;        // Mapped to _id
    private int patientId;
    private int doctorId;
    private String action;
    private String content;
    private Date timestamp;
    // ...
}
```
