package healthnet.patterns.strategy;

import healthnet.model.Patient;
import healthnet.sorting.QuickSort;

public class PatientQuickSortStrategy implements SortingStrategy<Patient> {
    private QuickSort quickSort = new QuickSort();

    @Override
    public void sort(Patient[] items) {
        if (items != null && items.length > 0) {
            quickSort.sort(items, 0, items.length - 1);
        }
    }
}
