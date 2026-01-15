package org.example.healthcaremanagementsystem.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Singleton class to monitor system performance metrics.
 * Tracks database query execution times and cache statistics.
 */
public class PerformanceMonitor {

    private static PerformanceMonitor instance;
    private final AtomicLong totalDbQueryTime = new AtomicLong(0);
    private final AtomicLong totalDbQueries = new AtomicLong(0);

    // Map to store query counts by type (e.g., "SELECT", "INSERT", "UPDATE")
    private final Map<String, AtomicLong> queryTypeCounts = new ConcurrentHashMap<>();

    // Reference to CacheManager for cache stats
    private final CacheManager cacheManager;

    private PerformanceMonitor() {
        this.cacheManager = new CacheManager(); // Or inject existing instance if possible
    }

    public static synchronized PerformanceMonitor getInstance() {
        if (instance == null) {
            instance = new PerformanceMonitor();
        }
        return instance;
    }

    /**
     * Records a database query execution.
     * 
     * @param queryType  The type/description of query (e.g. "SELECT Patient")
     * @param durationMs Duration in milliseconds
     */
    public void recordQuery(String queryType, long durationMs) {
        totalDbQueryTime.addAndGet(durationMs);
        totalDbQueries.incrementAndGet();

        queryTypeCounts.computeIfAbsent(queryType, k -> new AtomicLong(0))
                .incrementAndGet();
    }

    public long getTotalDbQueryTime() {
        return totalDbQueryTime.get();
    }

    public long getTotalDbQueries() {
        return totalDbQueries.get();
    }

    public double getAverageQueryTime() {
        long queries = totalDbQueries.get();
        return queries == 0 ? 0 : (double) totalDbQueryTime.get() / queries;
    }
}
