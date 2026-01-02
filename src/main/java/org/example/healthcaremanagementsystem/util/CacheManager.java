package org.example.healthcaremanagementsystem.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory cache manager for frequently accessed data.
 * Implements caching using HashMap for O(1) lookup performance.
 * Follows Single Responsibility Principle by handling only caching logic.
 * 
 * This class demonstrates the hashing concept where data is stored in
 * hash-based data structures for fast retrieval.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class CacheManager {
    
    // Cache for patients - uses patient ID as key
    private final Map<Integer, Object> patientCache;
    
    // Cache for doctors - uses doctor ID as key
    private final Map<Integer, Object> doctorCache;
    
    // Cache for departments - uses department ID as key
    private final Map<Integer, Object> departmentCache;
    
    // Cache TTL (Time To Live) in milliseconds - default 5 minutes
    private static final long DEFAULT_TTL = 300000; // 5 minutes
    
    // Map to store cache entry timestamps
    private final Map<String, Long> cacheTimestamps;
    
    /**
     * Constructor initializes thread-safe concurrent hash maps.
     * Uses ConcurrentHashMap for thread safety in multi-threaded environments.
     */
    public CacheManager() {
        this.patientCache = new ConcurrentHashMap<>();
        this.doctorCache = new ConcurrentHashMap<>();
        this.departmentCache = new ConcurrentHashMap<>();
        this.cacheTimestamps = new ConcurrentHashMap<>();
    }
    
    /**
     * Stores a patient in the cache.
     * Uses hashing for O(1) average-case time complexity.
     * 
     * @param patientId Patient identifier (used as hash key)
     * @param patient Patient object to cache
     */
    public void cachePatient(Integer patientId, Object patient) {
        patientCache.put(patientId, patient);
        cacheTimestamps.put("patient_" + patientId, System.currentTimeMillis());
    }
    
    /**
     * Retrieves a patient from cache.
     * 
     * @param patientId Patient identifier
     * @return Cached patient object or null if not found or expired
     */
    public Object getPatient(Integer patientId) {
        if (isExpired("patient_" + patientId)) {
            patientCache.remove(patientId);
            return null;
        }
        return patientCache.get(patientId);
    }
    
    /**
     * Stores a doctor in the cache.
     * 
     * @param doctorId Doctor identifier (used as hash key)
     * @param doctor Doctor object to cache
     */
    public void cacheDoctor(Integer doctorId, Object doctor) {
        doctorCache.put(doctorId, doctor);
        cacheTimestamps.put("doctor_" + doctorId, System.currentTimeMillis());
    }
    
    /**
     * Retrieves a doctor from cache.
     * 
     * @param doctorId Doctor identifier
     * @return Cached doctor object or null if not found or expired
     */
    public Object getDoctor(Integer doctorId) {
        if (isExpired("doctor_" + doctorId)) {
            doctorCache.remove(doctorId);
            return null;
        }
        return doctorCache.get(doctorId);
    }
    
    /**
     * Stores a department in the cache.
     * 
     * @param departmentId Department identifier (used as hash key)
     * @param department Department object to cache
     */
    public void cacheDepartment(Integer departmentId, Object department) {
        departmentCache.put(departmentId, department);
        cacheTimestamps.put("department_" + departmentId, System.currentTimeMillis());
    }
    
    /**
     * Retrieves a department from cache.
     * 
     * @param departmentId Department identifier
     * @return Cached department object or null if not found or expired
     */
    public Object getDepartment(Integer departmentId) {
        if (isExpired("department_" + departmentId)) {
            departmentCache.remove(departmentId);
            return null;
        }
        return departmentCache.get(departmentId);
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
     * Clears all caches.
     * Useful for cache invalidation after bulk updates.
     */
    public void clearAll() {
        patientCache.clear();
        doctorCache.clear();
        departmentCache.clear();
        cacheTimestamps.clear();
    }
    
    /**
     * Returns the current size of patient cache.
     * Useful for monitoring cache performance.
     * 
     * @return Number of cached patients
     */
    public int getPatientCacheSize() {
        return patientCache.size();
    }
    
    /**
     * Returns the current size of doctor cache.
     * 
     * @return Number of cached doctors
     */
    public int getDoctorCacheSize() {
        return doctorCache.size();
    }
}

