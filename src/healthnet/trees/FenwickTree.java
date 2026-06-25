package healthnet.trees;

/**
 * Fenwick Tree (Binary Indexed Tree) Implementation.
 * Used for cumulative statistics such as daily patient counts.
 *
 * Time Complexity:
 *   Update:     O(log N)
 *   PrefixSum:  O(log N)
 *   RangeSum:   O(log N)
 * Space Complexity: O(N)
 */
public class FenwickTree {

    private int[] bit; // Binary Indexed Tree array (1-indexed internally)
    private int n;

    /**
     * Constructs an empty Fenwick Tree of the given size.
     * @param n the number of elements
     */
    public FenwickTree(int n) {
        this.n = n;
        this.bit = new int[n + 1]; // 1-indexed
    }

    /**
     * Constructs a Fenwick Tree from the given data array.
     * @param data the source array (0-indexed)
     */
    public FenwickTree(int[] data) {
        this.n = data.length;
        this.bit = new int[n + 1];
        for (int i = 0; i < n; i++) {
            update(i, data[i]);
        }
    }

    /**
     * Adds delta to the element at the given index.
     * @param index 0-indexed position
     * @param delta value to add
     */
    public void update(int index, int delta) {
        index++; // Convert to 1-indexed
        while (index <= n) {
            bit[index] += delta;
            index += index & (-index); // Move to parent
        }
    }

    /**
     * Returns the prefix sum from index 0 to the given index (inclusive).
     * @param index 0-indexed position
     * @return prefix sum [0, index]
     */
    public int prefixSum(int index) {
        int sum = 0;
        index++; // Convert to 1-indexed
        while (index > 0) {
            sum += bit[index];
            index -= index & (-index); // Move to predecessor
        }
        return sum;
    }

    /**
     * Returns the sum of elements in the range [left, right] (0-indexed).
     * @param left  left index (inclusive)
     * @param right right index (inclusive)
     * @return range sum
     */
    public int rangeSum(int left, int right) {
        if (left == 0) {
            return prefixSum(right);
        }
        return prefixSum(right) - prefixSum(left - 1);
    }

    /**
     * Returns the number of elements.
     */
    public int getSize() {
        return n;
    }
}
