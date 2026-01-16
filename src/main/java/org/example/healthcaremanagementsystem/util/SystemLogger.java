package org.example.healthcaremanagementsystem.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * System Logger implementing Singleton pattern.
 * Stores logs in memory during runtime and persists to file on shutdown.
 */
public class SystemLogger {

    private static SystemLogger instance;
    private final List<String> inMemoryLogs;
    private static final String LOG_FILE = "system_logs.log";
    private static final DateTimeFormatter DATES_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private SystemLogger() {
        inMemoryLogs = Collections.synchronizedList(new ArrayList<>());

        // Add shutdown hook to persist logs
        Runtime.getRuntime().addShutdownHook(new Thread(this::persistLogs));
    }

    public static synchronized SystemLogger getInstance() {
        if (instance == null) {
            instance = new SystemLogger();
        }
        return instance;
    }

    public void log(String category, String message) {
        String timestamp = LocalDateTime.now().format(DATES_FORMATTER);
        String logEntry = String.format("[%s] [%s] %s", timestamp, category, message);
        inMemoryLogs.add(logEntry);
        System.out.println(logEntry); // Also print to console
    }

    public List<String> getLogs() {
        return new ArrayList<>(inMemoryLogs); // Return copy to avoid concurrent modification issues during iteration
    }

    private void persistLogs() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            for (String log : inMemoryLogs) {
                writer.write(log);
                writer.newLine();
            }
            System.out.println("Logs persisted to " + LOG_FILE);
        } catch (IOException e) {
            System.err.println("Failed to persist logs: " + e.getMessage());
        }
    }
}
