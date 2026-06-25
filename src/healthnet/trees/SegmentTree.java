package healthnet.trees;

/**
 * Segment Tree Implementation for range sum queries.
 * Used for analytics such as patient inflow across time intervals.
 *
 * Time Complexity:
 *   Build:  O(N)
 *   Query:  O(log N)
 *   Update: O(log N)
 * Space Complexity: O(4N)
 */
public class SegmentTree {

    private int[] tree;
    private int n;

    /**
     * Constructs a Segment Tree from the given data array.
     * @param data the source array (e.g., daily patient counts)
     */
    public SegmentTree(int[] data) {
        this.n = data.length;
        this.tree = new int[4 * n];
        build(data, 0, 0, n - 1);
    }

    private void build(int[] data, int node, int start, int end) {
        if (start == end) {
            tree[node] = data[start];
        } else {
            int mid = (start + end) / 2;
            build(data, 2 * node + 1, start, mid);
            build(data, 2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }

    /**
     * Range sum query: returns the sum of elements in [left, right].
     * @param left  left index (0-indexed, inclusive)
     * @param right right index (0-indexed, inclusive)
     * @return sum of elements in the range
     */
    public int query(int left, int right) {
        return queryRec(0, 0, n - 1, left, right);
    }

    private int queryRec(int node, int start, int end, int left, int right) {
        if (right < start || end < left) {
            return 0; // Out of range
        }
        if (left <= start && end <= right) {
            return tree[node]; // Fully within range
        }
        int mid = (start + end) / 2;
        int leftSum = queryRec(2 * node + 1, start, mid, left, right);
        int rightSum = queryRec(2 * node + 2, mid + 1, end, left, right);
        return leftSum + rightSum;
    }

    /**
     * Point update: sets the value at the given index.
     * @param index the position to update (0-indexed)
     * @param value the new value
     */
    public void update(int index, int value) {
        updateRec(0, 0, n - 1, index, value);
    }

    private void updateRec(int node, int start, int end, int index, int value) {
        if (start == end) {
            tree[node] = value;
        } else {
            int mid = (start + end) / 2;
            if (index <= mid) {
                updateRec(2 * node + 1, start, mid, index, value);
            } else {
                updateRec(2 * node + 2, mid + 1, end, index, value);
            }
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }

    /**
     * Returns the number of elements in the original data array.
     */
    public int getSize() {
        return n;
    }
}
