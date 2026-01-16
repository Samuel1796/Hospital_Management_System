package org.example.healthcaremanagementsystem.util;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for searching operations.
 * Implements linear search algorithm for filtering data.
 * Follows Single Responsibility Principle by handling only search logic.
 * 
 * Demonstrates search algorithm concepts:
 * - Linear search for unsorted data (O(n))
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class SearchUtil {
    
    /**
     * Performs linear search on a list.
     * Time complexity: O(n) - checks each element sequentially
     * Suitable for unsorted data or small datasets
     * 
     * @param <T> Type of elements in the list
     * @param list List to search
     * @param predicate Condition to match elements
     * @return List of matching elements
     */
    public static <T> List<T> linearSearch(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
