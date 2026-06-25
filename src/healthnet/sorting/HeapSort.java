package healthnet.sorting;

import healthnet.model.Patient;

/**
 * Heap Sort Implementation. Time Complexity: O(N log N)
 * Manages emergency patient queues based on severity.
 */
public class HeapSort {

    public void sort(Patient[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        for (int i = n - 1; i > 0; i--) {
            Patient temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
    }

    private void heapify(Patient[] arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        // Sort by Severity Score (Descending)
        if (l < n && arr[l].getSeverityScore() < arr[largest].getSeverityScore())
            largest = l;

        if (r < n && arr[r].getSeverityScore() < arr[largest].getSeverityScore())
            largest = r;

        if (largest != i) {
            Patient swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, n, largest);
        }
    }
}
