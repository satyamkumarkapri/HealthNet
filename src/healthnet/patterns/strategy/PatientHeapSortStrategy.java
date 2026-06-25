package healthnet.patterns.strategy;

import healthnet.model.Patient;
import healthnet.sorting.HeapSort;

public class PatientHeapSortStrategy implements SortingStrategy<Patient> {
    private HeapSort heapSort = new HeapSort();

    @Override
    public void sort(Patient[] items) {
        if (items != null && items.length > 0) {
            heapSort.sort(items);
        }
    }
}
