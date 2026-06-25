package healthnet.patterns.strategy;

import healthnet.model.MedicalRecord;
import healthnet.sorting.RadixSort;

public class RecordRadixSortStrategy implements SortingStrategy<MedicalRecord> {
    private RadixSort radixSort = new RadixSort();

    @Override
    public void sort(MedicalRecord[] items) {
        if (items != null && items.length > 0) {
            radixSort.sort(items);
        }
    }
}
