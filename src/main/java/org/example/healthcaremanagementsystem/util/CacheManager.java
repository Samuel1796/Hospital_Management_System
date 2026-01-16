package org.example.healthcaremanagementsystem.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory cache manager for frequently accessed data.
 * Implements caching using HashMap for O(1) lookup performance.
 * Follows Single Responsibility Principle by handling only caching logic.
 * 
 * This class demonstrates the hashing concept where data is stored in
 * hash-based data structures for fast retrieval.
 */
public class CacheManager {

    // Cache for patients - uses patient ID as key
    private final Map<Integer, Object> patientCache;

    // Cache for doctors - uses doctor ID as key
    private final Map<Integer, Object> doctorCache;

    // Cache for departments - uses department ID as key
    private final Map<Integer, Object> departmentCache;

    // Cache for appointments - uses appointment ID as key
    private final Map<Integer, Object> appointmentCache;

    // Cache generic queries (e.g. "all_patients")
    private final Map<String, Object> queryCache;

    // Cache TTL (Time To Live) in milliseconds - default 5 minutes
    private static final long DEFAULT_TTL = 300000; // 5 minutes

    // Map to store cache entry timestamps
    private final Map<String, Long> cacheTimestamps;

    // Cache statistics for monitoring
    private final AtomicLong patientCacheHits = new AtomicLong(0);
    private final AtomicLong patientCacheMisses = new AtomicLong(0);
    private final AtomicLong doctorCacheHits = new AtomicLong(0);
    private final AtomicLong doctorCacheMisses = new AtomicLong(0);
    private final AtomicLong departmentCacheHits = new AtomicLong(0);
    private final AtomicLong departmentCacheMisses = new AtomicLong(0);
    private final AtomicLong appointmentCacheHits = new AtomicLong(0);
    private final AtomicLong appointmentCacheMisses = new AtomicLong(0);
    private final AtomicLong queryCacheHits = new AtomicLong(0);
    private final AtomicLong queryCacheMisses = new AtomicLong(0);

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes thread-safe concurrent hash maps.
     */
    private static CacheManager instance;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes thread-safe concurrent hash maps.
     */
    public CacheManager() {
        this.patientCache = new ConcurrentHashMap<>();
        this.doctorCache = new ConcurrentHashMap<>();
        this.departmentCache = new ConcurrentHashMap<>();
        this.appointmentCache = new ConcurrentHashMap<>();
        this.queryCache = new ConcurrentHashMap<>();
        this.cacheTimestamps = new ConcurrentHashMap<>();
    }

    /**
     * Returns the global singleton instance of CacheManager.
     * 
     * @return CacheManager instance
     */
    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    /**
     * Stores a patient in the cache.
     * Uses hashing for O(1) average-case time complexity.
     * 
     * @param patientId Patient identifier (used as hash key)
     * @param patient   Patient object to cache
     */
    public void cachePatient(Integer patientId, Object patient) {
        patientCache.put(patientId, patient);
        cacheTimestamps.put("patient_" + patientId, System.currentTimeMillis());
    }

    /**
     * Stores a doctor in the cache.
     * 
     * @param doctorId Doctor identifier (used as hash key)
     * @param doctor   Doctor object to cache
     */
    public void cacheDoctor(Integer doctorId, Object doctor) {
        doctorCache.put(doctorId, doctor);
        cacheTimestamps.put("doctor_" + doctorId, System.currentTimeMillis());
    }

    /**
     * Retrieves a doctor from cache.
     * Tracks cache hits and misses for monitoring.
     * 
     * @param doctorId Doctor identifier
     * @return Cached doctor object or null if not found or expired
     */
    public Object getDoctor(Integer doctorId) {
        if (isExpired("doctor_" + doctorId)) {
            doctorCache.remove(doctorId);
            doctorCacheMisses.incrementAndGet();
            return null;
        }
        Object cached = doctorCache.get(doctorId);
        if (cached != null) {
            doctorCacheHits.incrementAndGet();
        } else {
            doctorCacheMisses.incrementAndGet();
        }
        return cached;
    }

    /**
     * Stores an appointment in the cache.
     * 
     * @param appointmentId Appointment identifier
     * @param appointment   Appointment object to cache
     */
    public void cacheAppointment(Integer appointmentId, Object appointment) {
        appointmentCache.put(appointmentId, appointment);
        cacheTimestamps.put("appointment_" + appointmentId, System.currentTimeMillis());
    }

    /**
     * Checks if a cache entry has expired based on TTL.
     * 
     * @param key Cache entry key
     * @return true if expired, false otherwise
     */
    private boolean isExpired(String key) {
        Long timestamp = cacheTimestamps.get(key);
        if (timestamp == null) {
            return true;
        }
        return (System.currentTimeMillis() - timestamp) > DEFAULT_TTL;
    }

    /**
     * Invalidates (removes) a patient from cache.
     * Called when patient data is updated or deleted.
     * 
     * @param patientId Patient identifier
     */
    public void invalidatePatient(Integer patientId) {
        patientCache.remove(patientId);
        cacheTimestamps.remove("patient_" + patientId);
    }

    /**
     * Invalidates (removes) a doctor from cache.
     * 
     * @param doctorId Doctor identifier
     */
    public void invalidateDoctor(Integer doctorId) {
        doctorCache.remove(doctorId);
        cacheTimestamps.remove("doctor_" + doctorId);
    }

    /**
     * Invalidates (removes) a department from cache.
     * 
     * @param departmentId Department identifier
     */
    public void invalidateDepartment(Integer departmentId) {
        departmentCache.remove(departmentId);
        cacheTimestamps.remove("department_" + departmentId);
    }

    /**
     * Invalidates (removes) an appointment from cache.
     * 
     * @param appointmentId Appointment identifier
     */
    public void invalidateAppointment(Integer appointmentId) {
        appointmentCache.remove(appointmentId);
        cacheTimestamps.remove("appointment_" + appointmentId);
    }

    /**
     * Stores a generic query result in the cache.
     * 
     * @param key   Query identifier (e.g. "all_patients")
     * @param value Query result to cache
     */
    public void cacheQuery(String key, Object value) {
        queryCache.put(key, value);
        cacheTimestamps.put("query_" + key, System.currentTimeMillis());
    }

    /**
     * Retrieves a generic query result from cache.
     * 
     * @param key Query identifier
     * @return Cached result or null
     */
    public Object getQuery(String key) {
        if (isExpired("query_" + key)) {
            queryCache.remove(key);
            queryCacheMisses.incrementAndGet();
            return null;
        }
        Object cached = queryCache.get(key);
        if (cached != null) {
            queryCacheHits.incrementAndGet();
        } else {
            queryCacheMisses.incrementAndGet();
        }
        return cached;
    }

    /**
     * Invalidates a generic query cache entry.
     * 
     * @param key Query identifier
     */
    public void invalidateQuery(String key) {
        queryCache.remove(key);
        cacheTimestamps.remove("query_" + key);
    }

    /**
     * Invalidates all query cache entries that start with the given prefix.
     * Useful for clearing related queries (e.g. "patients_page_") when data
     * changes.
     * 
     * @param prefix Key prefix to match
     */
    public void invalidateQueryPattern(String prefix) {
        queryCache.keySet().removeIf(key -> key.startsWith(prefix));
        cacheTimestamps.keySet().removeIf(key -> key.startsWith("query_" + prefix));
    }

    /**
     * Calculates cache hit rate for patients.
     * 
     * @return Hit rate as a percentage (0-100)
     */
    public double getPatientCacheHitRate() {
        long hits = patientCacheHits.get();
        long misses = patientCacheMisses.get();
        long total = hits + misses;
        return total > 0 ? (hits * 100.0 / total) : 0.0;
    }

    /**
     * Calculates cache hit rate for doctors.
     * 
     * @return Hit rate as a percentage (0-100)
     */
    public double getDoctorCacheHitRate() {
        long hits = doctorCacheHits.get();
        long misses = doctorCacheMisses.get();
        long total = hits + misses;
        return total > 0 ? (hits * 100.0 / total) : 0.0;
    }

    /**
     * Calculates cache hit rate for appointments.
     * 
     * @return Hit rate as a percentage (0-100)
     */
    public double getAppointmentCacheHitRate() {
        long hits = appointmentCacheHits.get();
        long misses = appointmentCacheMisses.get();
        long total = hits + misses;
        return total > 0 ? (hits * 100.0 / total) : 0.0;
    }

    /**
     * Calculates cache hit rate for queries.
     * 
     * @return Hit rate as a percentage (0-100)
     */
    public double getQueryCacheHitRate() {
        long hits = queryCacheHits.get();
        long misses = queryCacheMisses.get();
        long total = hits + misses;
        return total > 0 ? (hits * 100.0 / total) : 0.0;
    }
}
