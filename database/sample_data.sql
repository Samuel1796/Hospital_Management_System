-- ============================================================================
-- Healthcare Management System - Sample Data
-- PostgreSQL Database Script
-- ============================================================================

-- Insert sample departments
INSERT INTO departments (department_name, description, location, phone_number) VALUES
('Cardiology', 'Heart and cardiovascular system care', 'Building A, Floor 2', '0302123456'),
('Neurology', 'Brain and nervous system disorders', 'Building A, Floor 3', '0302123457'),
('Orthopedics', 'Bone, joint, and muscle treatment', 'Building B, Floor 1', '0302123458'),
('Pediatrics', 'Medical care for infants and children', 'Building B, Floor 2', '0302123459'),
('Emergency', 'Emergency medical services', 'Building C, Ground Floor', '0302123460'),
('General Medicine', 'Primary care and general health', 'Building A, Floor 1', '0302123461');

-- Insert sample doctors
INSERT INTO doctors (first_name, last_name, email, phone_number, specialization, department_id, license_number, hire_date, status) VALUES
('Kwame', 'Mensah', 'kwame.mensah@hospital.com', '0244123456', 'Cardiologist', 1, 'LIC-CARD-001', '2020-01-15', 'Active'),
('Akosua', 'Asante', 'akosua.asante@hospital.com', '0244123457', 'Neurologist', 2, 'LIC-NEUR-001', '2019-03-20', 'Active'),
('Kofi', 'Osei', 'kofi.osei@hospital.com', '0244123458', 'Orthopedic Surgeon', 3, 'LIC-ORTH-001', '2018-06-10', 'Active'),
('Ama', 'Boateng', 'ama.boateng@hospital.com', '0244123459', 'Pediatrician', 4, 'LIC-PED-001', '2021-02-01', 'Active'),
('Yaw', 'Adjei', 'yaw.adjei@hospital.com', '0244123460', 'Emergency Medicine', 5, 'LIC-EMER-001', '2017-09-15', 'Active'),
('Efua', 'Darko', 'efua.darko@hospital.com', '0244123461', 'General Practitioner', 6, 'LIC-GEN-001', '2020-11-01', 'Active'),
('Kojo', 'Appiah', 'kojo.appiah@hospital.com', '0244123462', 'Cardiologist', 1, 'LIC-CARD-002', '2019-08-20', 'Active'),
('Abena', 'Owusu', 'abena.owusu@hospital.com', '0244123463', 'Neurologist', 2, 'LIC-NEUR-002', '2021-04-10', 'Active');

-- Insert sample patients
INSERT INTO patients (first_name, last_name, email, phone_number, date_of_birth, address, gender, blood_group, emergency_contact, emergency_phone) VALUES
('Adjoa', 'Mensah', 'adjoa.mensah@email.com', '0244567890', '1985-05-15', 'House No. 12, Ring Road East, Accra, Greater Accra Region', 'Female', 'O+', 'Kwame Mensah', '0244567891'),
('Kweku', 'Asante', 'kweku.asante@email.com', '0244567892', '1990-08-22', 'Plot 45, Adum, Kumasi, Ashanti Region', 'Male', 'A+', 'Akosua Asante', '0244567893'),
('Akua', 'Osei', 'akua.osei@email.com', '0244567894', '1988-12-03', 'Block 3, Tamale Central, Tamale, Northern Region', 'Female', 'B+', 'Kofi Osei', '0244567895'),
('Yaw', 'Boateng', 'yaw.boateng@email.com', '0244567896', '1992-03-18', 'No. 8, Cape Coast Road, Cape Coast, Central Region', 'Male', 'AB+', 'Ama Boateng', '0244567897'),
('Efua', 'Adjei', 'efua.adjei@email.com', '0244567898', '1987-07-25', 'House 23, Tema Community 5, Tema, Greater Accra Region', 'Female', 'O-', 'Yaw Adjei', '0244567899'),
('Kojo', 'Darko', 'kojo.darko@email.com', '0244567900', '1995-11-30', 'Plot 67, Sunyani Main Road, Sunyani, Bono Region', 'Male', 'A-', 'Efua Darko', '0244567901'),
('Abena', 'Appiah', 'abena.appiah@email.com', '0244567902', '1993-02-14', 'No. 15, Takoradi Harbour Road, Takoradi, Western Region', 'Female', 'B-', 'Kojo Appiah', '0244567903'),
('Kwabena', 'Owusu', 'kwabena.owusu@email.com', '0244567904', '1989-09-07', 'House 34, Koforidua Central, Koforidua, Eastern Region', 'Male', 'O+', 'Abena Owusu', '0244567905'),
('Ama', 'Agyeman', 'ama.agyeman@email.com', '0244567906', '1991-06-20', 'Block 12, Bolgatanga Main Street, Bolgatanga, Upper East Region', 'Female', 'A+', 'Kwabena Agyeman', '0244567907'),
('Kofi', 'Tetteh', 'kofi.tetteh@email.com', '0244567908', '1994-04-12', 'Plot 89, Wa Central Market, Wa, Upper West Region', 'Male', 'B+', 'Ama Tetteh', '0244567909');

-- Insert sample appointments
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_type, status, notes) VALUES
(1, 1, '2024-12-20 10:00:00', 'Consultation', 'Scheduled', 'Annual heart checkup'),
(2, 2, '2024-12-20 11:00:00', 'Follow-up', 'Scheduled', 'Follow-up on previous treatment'),
(3, 3, '2024-12-21 09:00:00', 'Consultation', 'Scheduled', 'Knee pain evaluation'),
(4, 4, '2024-12-21 10:30:00', 'Check-up', 'Scheduled', 'Child wellness check'),
(5, 5, '2024-12-19 14:00:00', 'Emergency', 'Completed', 'Emergency visit'),
(6, 6, '2024-12-22 13:00:00', 'Consultation', 'Scheduled', 'General health consultation'),
(7, 1, '2024-12-22 15:00:00', 'Follow-up', 'Scheduled', 'Cardiac follow-up'),
(8, 2, '2024-12-23 09:30:00', 'Consultation', 'Scheduled', 'Headache evaluation'),
(1, 6, '2024-12-18 11:00:00', 'Consultation', 'Completed', 'General checkup completed'),
(2, 1, '2024-12-19 10:00:00', 'Consultation', 'Completed', 'Cardiac screening');

-- Insert sample medical inventory
INSERT INTO medical_inventory (item_name, item_type, category, quantity, reorder_level, unit_price, supplier, expiry_date, storage_location, status) VALUES
('Aspirin 100mg', 'Medication', 'Pain Relief', 500, 100, 0.50, 'Pharmatrust Ghana Ltd.', '2025-12-31', 'Pharmacy A-101', 'Available'),
('Paracetamol 500mg', 'Medication', 'Pain Relief', 750, 150, 0.30, 'Ernest Chemists Ltd.', '2026-06-30', 'Pharmacy A-102', 'Available'),
('Amoxicillin 250mg', 'Medication', 'Antibiotic', 300, 50, 2.50, 'PharmaTrust Ghana Ltd.', '2025-08-15', 'Pharmacy A-103', 'Available'),
('Blood Pressure Monitor', 'Equipment', 'Diagnostic', 25, 5, 150.00, 'MedEquip Ghana', NULL, 'Equipment Room B-201', 'Available'),
('Stethoscope', 'Equipment', 'Diagnostic', 50, 10, 75.00, 'MedEquip Ghana', NULL, 'Equipment Room B-202', 'Available'),
('Surgical Gloves', 'Supply', 'Surgical', 2000, 500, 0.25, 'MedSupply Ghana', '2026-03-31', 'Supply Room C-301', 'Available'),
('Bandages', 'Supply', 'Wound Care', 1000, 200, 0.10, 'HealthCare Supplies Ghana', '2027-01-31', 'Supply Room C-302', 'Available'),
('Insulin 100IU', 'Medication', 'Diabetes', 200, 50, 25.00, 'PharmaTrust Ghana Ltd.', '2025-05-20', 'Pharmacy A-104', 'Available'),
('X-Ray Film', 'Supply', 'Imaging', 500, 100, 5.00, 'Imaging Solutions Ghana', '2026-12-31', 'Imaging Room D-401', 'Available'),
('Antiseptic Solution', 'Medication', 'Antiseptic', 150, 30, 3.50, 'Ernest Chemists Ltd.', '2025-09-30', 'Pharmacy A-105', 'Available');

-- Insert sample prescriptions
INSERT INTO prescriptions (patient_id, doctor_id, appointment_id, prescription_date, diagnosis, instructions, valid_until) VALUES
(1, 1, 9, '2024-12-18', 'Hypertension', 'Take medication as prescribed. Monitor blood pressure daily.', '2025-01-18'),
(2, 1, 10, '2024-12-19', 'Mild Arrhythmia', 'Follow-up in 2 weeks. Avoid caffeine.', '2025-01-19'),
(5, 5, 5, '2024-12-19', 'Acute Pain', 'Rest and take pain medication. Return if symptoms worsen.', '2024-12-26');

-- Insert sample prescription items
INSERT INTO prescription_items (prescription_id, inventory_id, medication_name, dosage, quantity, frequency, duration) VALUES
(1, 1, 'Aspirin 100mg', '100mg', 30, 'Once daily', '30 days'),
(1, 4, 'Blood Pressure Monitor', 'N/A', 1, 'Daily monitoring', 'Ongoing'),
(2, 1, 'Aspirin 100mg', '100mg', 60, 'Twice daily', '30 days'),
(3, 2, 'Paracetamol 500mg', '500mg', 20, 'Every 6 hours as needed', '5 days');

-- Insert sample patient feedback
INSERT INTO patient_feedback (patient_id, appointment_id, doctor_id, rating, comment, category) VALUES
(1, 9, 6, 5, 'Excellent service and very professional staff. Medaase!', 'Service'),
(2, 10, 1, 4, 'Good consultation, doctor was thorough and explained everything well.', 'Treatment'),
(5, 5, 5, 5, 'Quick response in emergency situation. Very satisfied with the care received.', 'Service'),
(1, 9, 6, 5, 'Clean facility and friendly environment. Staff were very helpful.', 'Facility');

