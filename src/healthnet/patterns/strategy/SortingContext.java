package healthnet.patterns.strategy;

public class SortingContext<T> {
    private SortingStrategy<T> strategy;

    public void setStrategy(SortingStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void sortItems(T[] items) {
        if (strategy != null) {
            strategy.sort(items);
        } else {
            System.out.println("No sorting strategy set.");
        }
    }
}
