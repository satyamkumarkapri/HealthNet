package healthnet.patterns.strategy;

public interface SortingStrategy<T> {
    void sort(T[] items);
}
