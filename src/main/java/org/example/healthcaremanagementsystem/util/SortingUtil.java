package org.example.healthcaremanagementsystem.util;

import java.util.Comparator;
import java.util.List;

/**
 * Utility class for sorting operations.
 * Implements sorting using Java's built-in TimSort algorithm.
 * Follows Single Responsibility Principle by handling only sorting logic.
 * 
 * Demonstrates sorting algorithm concepts:
 * - TimSort (hybrid of merge sort and insertion sort) - O(n log n)
 * - Comparator-based sorting for flexibility
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class SortingUtil {

    /**
     * Sorts a list using a custom comparator.
     * Uses Java's built-in TimSort algorithm which provides stable, O(n log n) performance.
     * 
     * @param <T>        Type of elements in the list
     * @param list       List to be sorted
     * @param comparator Comparator defining sort order
     * @return Sorted list (modifies original list)
     */
    public static <T> List<T> sort(List<T> list, Comparator<T> comparator) {
        list.sort(comparator);
        return list;
    }
}
