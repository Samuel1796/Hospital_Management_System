-- ============================================================================
-- Healthcare Management System Database Schema
-- PostgreSQL Database Script
-- Normalized to Third Normal Form (3NF)
-- ============================================================================

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS prescription_items CASCADE;
DROP TABLE IF EXISTS prescriptions CASCADE;
DROP TABLE IF EXISTS patient_feedback CASCADE;
DROP TABLE IF EXISTS appointments CASCADE;
DROP TABLE IF EXISTS medical_inventory CASCADE;
DROP TABLE IF EXISTS doctors CASCADE;
DROP TABLE IF EXISTS patients CASCADE;
DROP TABLE IF EXISTS departments CASCADE;

-- ============================================================================
-- Table: departments
-- Description: Hospital departments (Cardiology, Neurology, etc.)
-- Normalization: 3NF - No transitive dependencies
-- ============================================================================
CREATE TABLE departments (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    location VARCHAR(200),
    phone_number VARCHAR(20),
    head_doctor_id VARCHAR(50), -- Can be null if no head assigned
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- Table: patients
-- Description: Patient information and demographics
-- Normalization: 3NF - All attributes depend only on primary key
-- ============================================================================
CREATE TABLE patients (
    patient_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    date_of_birth DATE,
    address TEXT,
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other')),
    blood_group VARCHAR(5),
    emergency_contact VARCHAR(100),
    emergency_phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- Table: doctors
-- Description: Doctor information and credentials
-- Normalization: 3NF - Foreign key to departments maintains referential integrity
-- ============================================================================
CREATE TABLE doctors (
    doctor_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    department_id INTEGER NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    hire_date DATE,
    status VARCHAR(20) DEFAULT 'Active' CHECK (status IN ('Active', 'Inactive', 'On Leave')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doctor_department FOREIGN KEY (department_id) 
        REFERENCES departments(department_id) ON DELETE RESTRICT
);

-- ============================================================================
-- Table: appointments
-- Description: Patient-doctor appointment scheduling
-- Normalization: 3NF - Foreign keys maintain referential integrity
-- ============================================================================
CREATE TABLE appointments (
    appointment_id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    doctor_id INTEGER NOT NULL,
    appointment_date TIMESTAMP NOT NULL,
    appointment_type VARCHAR(50) DEFAULT 'Consultation' 
        CHECK (appointment_type IN ('Consultation', 'Follow-up', 'Emergency', 'Check-up')),
    status VARCHAR(20) DEFAULT 'Scheduled' 
        CHECK (status IN ('Scheduled', 'Completed', 'Cancelled', 'No-show')),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) 
        REFERENCES patients(patient_id) ON DELETE CASCADE,
    CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) 
        REFERENCES doctors(doctor_id) ON DELETE RESTRICT
);

-- ============================================================================
-- Table: prescriptions
-- Description: Prescription records for patients
-- Normalization: 3NF - Links patients, doctors, and appointments
-- ============================================================================
CREATE TABLE prescriptions (
    prescription_id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    doctor_id INTEGER NOT NULL,
    appointment_id INTEGER,
    prescription_date DATE NOT NULL DEFAULT CURRENT_DATE,
    diagnosis TEXT,
    instructions TEXT,
    valid_until DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prescription_patient FOREIGN KEY (patient_id) 
        REFERENCES patients(patient_id) ON DELETE CASCADE,
    CONSTRAINT fk_prescription_doctor FOREIGN KEY (doctor_id) 
        REFERENCES doctors(doctor_id) ON DELETE RESTRICT,
    CONSTRAINT fk_prescription_appointment FOREIGN KEY (appointment_id) 
        REFERENCES appointments(appointment_id) ON DELETE SET NULL
);

-- ============================================================================
-- Table: prescription_items
-- Description: Individual medications within a prescription
-- Normalization: 3NF - Composite key with prescription_id
-- ============================================================================
CREATE TABLE prescription_items (
    item_id SERIAL PRIMARY KEY,
    prescription_id INTEGER NOT NULL,
    inventory_id INTEGER,
    medication_name VARCHAR(200) NOT NULL,
    dosage VARCHAR(100),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    frequency VARCHAR(50), -- e.g., "Twice daily", "Once a week"
    duration VARCHAR(50), -- e.g., "7 days", "2 weeks"
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prescription_item_prescription FOREIGN KEY (prescription_id) 
        REFERENCES prescriptions(prescription_id) ON DELETE CASCADE
);

-- ============================================================================
-- Table: patient_feedback
-- Description: Patient feedback and ratings
-- Normalization: 3NF - Links patients, doctors, and appointments
-- ============================================================================
CREATE TABLE patient_feedback (
    feedback_id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    appointment_id INTEGER,
    doctor_id INTEGER NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    category VARCHAR(50) DEFAULT 'Service' 
        CHECK (category IN ('Service', 'Treatment', 'Facility', 'Other')),
    feedback_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_feedback_patient FOREIGN KEY (patient_id) 
        REFERENCES patients(patient_id) ON DELETE CASCADE,
    CONSTRAINT fk_feedback_appointment FOREIGN KEY (appointment_id) 
        REFERENCES appointments(appointment_id) ON DELETE SET NULL,
    CONSTRAINT fk_feedback_doctor FOREIGN KEY (doctor_id) 
        REFERENCES doctors(doctor_id) ON DELETE RESTRICT
);

-- ============================================================================
-- Table: medical_inventory
-- Description: Medical supplies, medications, and equipment inventory
-- Normalization: 3NF - All attributes depend only on primary key
-- ============================================================================
CREATE TABLE medical_inventory (
    inventory_id SERIAL PRIMARY KEY,
    item_name VARCHAR(200) NOT NULL,
    item_type VARCHAR(50) NOT NULL 
        CHECK (item_type IN ('Medication', 'Equipment', 'Supply')),
    category VARCHAR(100),
    quantity INTEGER NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    reorder_level INTEGER DEFAULT 10 CHECK (reorder_level >= 0),
    unit_price DECIMAL(10, 2) CHECK (unit_price >= 0),
    supplier VARCHAR(200),
    expiry_date DATE,
    storage_location VARCHAR(200),
    status VARCHAR(20) DEFAULT 'Available' 
        CHECK (status IN ('Available', 'Low Stock', 'Out of Stock', 'Expired')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- INDEXES
-- Description: Indexes on frequently searched and joined columns
-- Improves query performance for search operations
-- ============================================================================

-- Index on patient name columns for fast name-based searches
CREATE INDEX idx_patients_first_name ON patients(first_name);
CREATE INDEX idx_patients_last_name ON patients(last_name);
CREATE INDEX idx_patients_email ON patients(email);
CREATE INDEX idx_patients_phone ON patients(phone_number);

-- Index on doctor name columns for fast name-based searches
CREATE INDEX idx_doctors_first_name ON doctors(first_name);
CREATE INDEX idx_doctors_last_name ON doctors(last_name);
CREATE INDEX idx_doctors_department ON doctors(department_id);
CREATE INDEX idx_doctors_specialization ON doctors(specialization);

-- Index on appointment date for date range queries
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_appointments_doctor ON appointments(doctor_id);
CREATE INDEX idx_appointments_status ON appointments(status);

-- Index on prescription patient and doctor for quick lookups
CREATE INDEX idx_prescriptions_patient ON prescriptions(patient_id);
CREATE INDEX idx_prescriptions_doctor ON prescriptions(doctor_id);
CREATE INDEX idx_prescriptions_date ON prescriptions(prescription_date);

-- Index on inventory for search and filtering
CREATE INDEX idx_inventory_name ON medical_inventory(item_name);
CREATE INDEX idx_inventory_type ON medical_inventory(item_type);
CREATE INDEX idx_inventory_status ON medical_inventory(status);

-- Index on feedback for reporting
CREATE INDEX idx_feedback_patient ON patient_feedback(patient_id);
CREATE INDEX idx_feedback_doctor ON patient_feedback(doctor_id);
CREATE INDEX idx_feedback_rating ON patient_feedback(rating);

-- ============================================================================
-- COMMENTS ON TABLES
-- Documentation for database schema understanding
-- ============================================================================
COMMENT ON TABLE departments IS 'Hospital departments and their information';
COMMENT ON TABLE patients IS 'Patient demographic and contact information';
COMMENT ON TABLE doctors IS 'Doctor credentials and department assignments';
COMMENT ON TABLE appointments IS 'Patient-doctor appointment scheduling';
COMMENT ON TABLE prescriptions IS 'Medical prescriptions issued to patients';
COMMENT ON TABLE prescription_items IS 'Individual medications in prescriptions';
COMMENT ON TABLE patient_feedback IS 'Patient feedback and service ratings';
COMMENT ON TABLE medical_inventory IS 'Medical supplies and equipment inventory';

