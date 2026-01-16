package org.example.healthcaremanagementsystem.util;

/**
 * Singleton class to monitor system performance metrics.
 * Currently a placeholder for future performance monitoring features.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class PerformanceMonitor {

    private static PerformanceMonitor instance;

    private PerformanceMonitor() {
        // Private constructor for singleton pattern
    }

    /**
     * Returns the singleton instance of PerformanceMonitor.
     * 
     * @return PerformanceMonitor instance
     */
    public static synchronized PerformanceMonitor getInstance() {
        if (instance == null) {
            instance = new PerformanceMonitor();
        }
        return instance;
    }
}
