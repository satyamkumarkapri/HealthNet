package healthnet.sorting;

import healthnet.model.MedicalRecord;

/**
 * Counting Sort Implementation for non-negative integers. Time Complexity: O(N + K)
 * Sorts medical records by their IDs.
 */
public class CountingSort {

    public void sort(MedicalRecord[] arr) {
        if (arr.length == 0) return;
        int max = 0;
        for (MedicalRecord val : arr) {
            if (val.getRecordId() > max) max = val.getRecordId();
        }

        int[] count = new int[max + 1];
        MedicalRecord[] output = new MedicalRecord[arr.length];

        for (int i = 0; i < arr.length; i++) {
            count[arr[i].getRecordId()]++;
        }

        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
        }

        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[arr[i].getRecordId()] - 1] = arr[i];
            count[arr[i].getRecordId()]--;
        }

        System.arraycopy(output, 0, arr, 0, arr.length);
    }
}
