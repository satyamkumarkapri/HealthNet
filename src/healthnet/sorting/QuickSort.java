package healthnet.sorting;

import healthnet.model.Patient;

/**
 * Quick Sort Implementation. Time Complexity: O(N log N) average
 * Ranks patients based on severity.
 */
public class QuickSort {

    public void sort(Patient[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);

            sort(arr, low, pi - 1);
            sort(arr, pi + 1, high);
        }
    }

    private int partition(Patient[] arr, int low, int high) {
        // Pivot is severity score of the last element
        int pivot = arr[high].getSeverityScore();
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            // Sort in descending order (highest severity first)
            if (arr[j].getSeverityScore() > pivot) {
                i++;
                Patient temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        Patient temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}
