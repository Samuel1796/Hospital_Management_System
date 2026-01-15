package org.example.healthcaremanagementsystem.util;

import java.util.Comparator;
import java.util.List;

/**
 * Utility class for sorting operations.
 * Implements various sorting algorithms for different use cases.
 * Follows Single Responsibility Principle by handling only sorting logic.
 * 
 * Demonstrates sorting algorithm concepts:
 * - QuickSort for efficient average-case performance
 * - MergeSort for stable sorting
 * - Comparator-based sorting for flexibility
 */
public class SortingUtil {

    /**
     * Sorts a list using Java's built-in TimSort (hybrid of merge sort and
     * insertion sort).
     * This method provides stable, O(n log n) performance.
     * 
     * @param <T>  Type of elements in the list (must implement Comparable)
     * @param list List to be sorted
     * @return Sorted list (modifies original list)
     */
    public static <T extends Comparable<T>> List<T> sort(List<T> list) {
        list.sort(Comparator.naturalOrder());
        return list;
    }

    /**
     * Sorts a list using a custom comparator.
     * Allows flexible sorting based on different criteria.
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

    /**
     * Sorts a list in descending order.
     * 
     * @param <T>  Type of elements in the list (must implement Comparable)
     * @param list List to be sorted
     * @return Sorted list in descending order
     */
    public static <T extends Comparable<T>> List<T> sortDescending(List<T> list) {
        list.sort(Comparator.reverseOrder());
        return list;
    }

    /**
     * QuickSort implementation for sorting arrays.
     * Average time complexity: O(n log n), Worst case: O(n²)
     * Space complexity: O(log n) due to recursion
     * 
     * @param <T>   Type of elements (must implement Comparable)
     * @param array Array to be sorted
     * @param low   Starting index
     * @param high  Ending index
     */
    public static <T extends Comparable<T>> void quickSort(T[] array, int low, int high) {
        if (low < high) {
            // Partition the array and get pivot index
            int pivotIndex = partition(array, low, high);

            // Recursively sort elements before and after partition
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    /**
     * Partitions the array for QuickSort algorithm.
     * Places pivot element at correct position and places all
     * smaller elements to left and larger to right of pivot.
     * 
     * @param <T>   Type of elements
     * @param array Array to partition
     * @param low   Starting index
     * @param high  Ending index
     * @return Final position of pivot element
     */
    private static <T extends Comparable<T>> int partition(T[] array, int low, int high) {
        // Choose rightmost element as pivot
        T pivot = array[high];

        // Index of smaller element (indicates right position of pivot)
        int i = low - 1;

        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (array[j].compareTo(pivot) <= 0) {
                i++;
                // Swap array[i] and array[j]
                T temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // Swap array[i+1] and array[high] (place pivot at correct position)
        T temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    /**
     * MergeSort implementation for sorting arrays.
     * Time complexity: O(n log n) in all cases
     * Space complexity: O(n)
     * Stable sorting algorithm
     * 
     * @param <T>   Type of elements (must implement Comparable)
     * @param array Array to be sorted
     * @param left  Starting index
     * @param right Ending index
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> void mergeSort(T[] array, int left, int right) {
        if (left < right) {
            // Find the middle point
            int middle = left + (right - left) / 2;

            // Sort first and second halves
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);

            // Merge the sorted halves
            merge(array, left, middle, right);
        }
    }

    /**
     * Merges two subarrays for MergeSort algorithm.
     * 
     * @param <T>    Type of elements
     * @param array  Array containing subarrays to merge
     * @param left   Starting index of first subarray
     * @param middle Ending index of first subarray
     * @param right  Ending index of second subarray
     */
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void merge(T[] array, int left, int middle, int right) {
        // Sizes of two subarrays to be merged
        int n1 = middle - left + 1;
        int n2 = right - middle;

        // Create temporary arrays
        T[] leftArray = (T[]) new Comparable[n1];
        T[] rightArray = (T[]) new Comparable[n2];

        // Copy data to temporary arrays
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, middle + 1, rightArray, 0, n2);

        // Merge the temporary arrays
        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i].compareTo(rightArray[j]) <= 0) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of leftArray[]
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Copy remaining elements of rightArray[]
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}
