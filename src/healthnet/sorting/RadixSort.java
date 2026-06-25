package healthnet.sorting;

import java.util.Arrays;
import healthnet.model.MedicalRecord;

/**
 * Radix Sort Implementation. Time Complexity: O(d * (N + K))
 * Sorts medical record numbers.
 */
public class RadixSort {

    public void sort(MedicalRecord[] arr) {
        if (arr.length == 0) return;
        int max = getMax(arr);

        for (int exp = 1; max / exp > 0; exp *= 10)
            countSort(arr, exp);
    }

    private int getMax(MedicalRecord[] arr) {
        int max = arr[0].getRecordId();
        for (int i = 1; i < arr.length; i++)
            if (arr[i].getRecordId() > max)
                max = arr[i].getRecordId();
        return max;
    }

    private void countSort(MedicalRecord[] arr, int exp) {
        int n = arr.length;
        MedicalRecord[] output = new MedicalRecord[n];
        int[] count = new int[10];
        Arrays.fill(count, 0);

        for (int i = 0; i < n; i++)
            count[(arr[i].getRecordId() / exp) % 10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i].getRecordId() / exp) % 10] - 1] = arr[i];
            count[(arr[i].getRecordId() / exp) % 10]--;
        }

        System.arraycopy(output, 0, arr, 0, n);
    }
}
