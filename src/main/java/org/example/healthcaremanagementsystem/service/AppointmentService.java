package org.example.healthcaremanagementsystem.service;

import org.example.healthcaremanagementsystem.dao.AppointmentDAO;
import org.example.healthcaremanagementsystem.dao.AppointmentDAOImpl;
import org.example.healthcaremanagementsystem.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Appointment business logic.
 * Handles appointment scheduling and management operations.
 */
public class AppointmentService {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(AppointmentService.class.getName());
    private final AppointmentDAO appointmentDAO;
    private final org.example.healthcaremanagementsystem.util.CacheManager cacheManager;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAOImpl();
        this.cacheManager = org.example.healthcaremanagementsystem.util.CacheManager.getInstance();
    }

    public Appointment createAppointment(Appointment appointment) throws Exception {
        validateAppointment(appointment);
        Appointment created = appointmentDAO.create(appointment);

        cacheManager.cacheAppointment(created.getAppointmentId(), created);
        cacheManager.invalidateQuery("all_appointments");
        cacheManager.invalidateQueryPattern("appointments_page_");

        return created;
    }

    public Optional<Appointment> getAppointmentById(Integer appointmentId) throws Exception {
        return appointmentDAO.findById(appointmentId);
    }

    public List<Appointment> getAllAppointments() throws Exception {
        long startTime = System.currentTimeMillis();
        @SuppressWarnings("unchecked")
        List<Appointment> cached = (List<Appointment>) cacheManager.getQuery("all_appointments");

        if (cached != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info(String.format("[CACHE] HIT - All Appointments - %d records - %d ms", cached.size(), duration));
            return cached;
        }

        logger.info("[CACHE] MISS - All Appointments - Fetching from DB");
        List<Appointment> appointments = appointmentDAO.findAll();

        cacheManager.cacheQuery("all_appointments", appointments);
        long duration = System.currentTimeMillis() - startTime;
        logger.info(
                String.format("[CACHE] STORED - All Appointments - %d records - %d ms", appointments.size(), duration));

        return appointments;
    }

    public boolean updateAppointment(Appointment appointment) throws Exception {
        validateAppointment(appointment);
        boolean updated = appointmentDAO.update(appointment);

        if (updated) {
            cacheManager.invalidateAppointment(appointment.getAppointmentId());
            cacheManager.invalidateQuery("all_appointments");
            cacheManager.invalidateQueryPattern("appointments_page_");
        }

        return updated;
    }

    public boolean deleteAppointment(Integer appointmentId) throws Exception {
        boolean deleted = appointmentDAO.delete(appointmentId);

        if (deleted) {
            cacheManager.invalidateAppointment(appointmentId);
            cacheManager.invalidateQuery("all_appointments");
            cacheManager.invalidateQueryPattern("appointments_page_");
        }

        return deleted;
    }

    // ... getAppointmentsByPatient, getAppointmentsByDoctor,
    // getAppointmentsByDateRange are fine ...

    public List<Appointment> getAppointmentsByPatient(Integer patientId) throws Exception {
        return appointmentDAO.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(Integer doctorId) throws Exception {
        return appointmentDAO.findByDoctorId(doctorId);
    }

    public List<Appointment> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate)
            throws Exception {
        return appointmentDAO.findByDateRange(startDate, endDate);
    }

    /**
     * Retrieves appointments with pagination.
     * 
     * @param page     Page number (0-based)
     * @param pageSize Number of records per page
     * @return List of appointments for the specified page
     */
    public List<Appointment> getAppointmentsPaginated(int page, int pageSize) throws Exception {
        long startTime = System.currentTimeMillis();
        String cacheKey = "appointments_page_" + page + "_size_" + pageSize;

        @SuppressWarnings("unchecked")
        List<Appointment> cached = (List<Appointment>) cacheManager.getQuery(cacheKey);

        if (cached != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info(String.format("[CACHE] HIT - Appointments Page %d - %d records - %d ms", page, cached.size(),
                    duration));
            return cached;
        }

        logger.info(String.format("[CACHE] MISS - Appointments Page %d - Fetching from DB", page));
        List<Appointment> appointments = appointmentDAO.findAllPaginated(page, pageSize);

        cacheManager.cacheQuery(cacheKey, appointments);
        long duration = System.currentTimeMillis() - startTime;
        logger.info(String.format("[CACHE] STORED - Appointments Page %d - %d records - %d ms", page,
                appointments.size(), duration));

        return appointments;
    }

    /**
     * Gets the total count of appointments for pagination.
     * 
     * @return Total number of appointments
     */
    public int getTotalAppointmentCount() throws Exception {
        return appointmentDAO.getCount();
    }

    private void validateAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null.");
        }

        if (appointment.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required.");
        }

        if (appointment.getDoctorId() == null) {
            throw new IllegalArgumentException("Doctor ID is required.");
        }

        if (appointment.getAppointmentDate() == null) {
            throw new IllegalArgumentException("Appointment date is required.");
        }

        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past.");
        }
    }
}
