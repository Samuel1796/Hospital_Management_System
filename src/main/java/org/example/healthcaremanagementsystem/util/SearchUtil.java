package org.example.healthcaremanagementsystem.util;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for searching operations.
 * Implements various search algorithms for different use cases.
 * Follows Single Responsibility Principle by handling only search logic.
 * 
 * Demonstrates search algorithm concepts:
 * - Linear search for unsorted data
 * - Binary search for sorted data (O(log n))
 * - Hash-based search using Java Streams
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
    
    /**
     * Performs binary search on a sorted list.
     * Time complexity: O(log n) - divides search space in half each iteration
     * Requires the list to be sorted in ascending order
     * 
     * @param <T> Type of elements (must implement Comparable)
     * @param sortedList Sorted list to search
     * @param target Element to find
     * @return Index of target element, or -1 if not found
     */
    public static <T extends Comparable<T>> int binarySearch(List<T> sortedList, T target) {
        int left = 0;
        int right = sortedList.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            T midElement = sortedList.get(mid);
            
            int comparison = midElement.compareTo(target);
            
            if (comparison == 0) {
                return mid; // Element found
            } else if (comparison < 0) {
                left = mid + 1; // Search in right half
            } else {
                right = mid - 1; // Search in left half
            }
        }
        
        return -1; // Element not found
    }
    
    /**
     * Case-insensitive search for strings.
     * Uses linear search with case-insensitive comparison.
     * 
     * @param list List of strings to search
     * @param searchTerm Term to search for
     * @return List of matching strings
     */
    public static List<String> caseInsensitiveSearch(List<String> list, String searchTerm) {
        String lowerSearchTerm = searchTerm.toLowerCase();
        return list.stream()
                .filter(item -> item != null && item.toLowerCase().contains(lowerSearchTerm))
                .collect(Collectors.toList());
    }
    
    /**
     * Searches for elements matching multiple criteria (AND condition).
     * 
     * @param <T> Type of elements
     * @param list List to search
     * @param predicates Array of predicates (all must match)
     * @return List of elements matching all predicates
     */
    @SafeVarargs
    public static <T> List<T> searchWithMultipleCriteria(List<T> list, Predicate<T>... predicates) {
        Predicate<T> combinedPredicate = predicates[0];
        for (int i = 1; i < predicates.length; i++) {
            combinedPredicate = combinedPredicate.and(predicates[i]);
        }
        
        return list.stream()
                .filter(combinedPredicate)
                .collect(Collectors.toList());
    }
    
    /**
     * Performs fuzzy search (partial matching) on a list of strings.
     * 
     * @param list List of strings to search
     * @param searchTerm Partial term to match
     * @return List of strings containing the search term
     */
    public static List<String> fuzzySearch(List<String> list, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return list;
        }
        
        String lowerSearchTerm = searchTerm.toLowerCase().trim();
        return list.stream()
                .filter(item -> item != null && item.toLowerCase().contains(lowerSearchTerm))
                .collect(Collectors.toList());
    }
}

