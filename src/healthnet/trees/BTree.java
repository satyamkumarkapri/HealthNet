package healthnet.trees;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic B-Tree Implementation for indexing Medical Records.
 * Supports insert and search operations.
 */
public class BTree<T extends Comparable<T>> {

    private int t; // Minimum degree

    public class BTreeNode {
        int n;
        List<T> keys = new ArrayList<>();
        List<BTreeNode> children = new ArrayList<>();
        boolean leaf;

        public BTreeNode(int t, boolean leaf) {
            this.leaf = leaf;
            for (int i = 0; i < 2 * t - 1; i++) {
                keys.add(null);
            }
            for (int i = 0; i < 2 * t; i++) {
                children.add(null);
            }
        }
    }

    private BTreeNode root;

    public BTree(int t) {
        this.t = t;
        root = null;
    }

    public void traverse() {
        if (root != null) {
            traverseRec(root, 0);
        }
    }

    private void traverseRec(BTreeNode node, int level) {
        System.out.println("Level " + level + " | Keys: " + getNonNullKeys(node));
        for (int i = 0; i <= node.n; i++) {
            if (!node.leaf && node.children.get(i) != null) {
                traverseRec(node.children.get(i), level + 1);
            }
        }
    }

    private List<T> getNonNullKeys(BTreeNode node) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < node.n; i++) {
            list.add(node.keys.get(i));
        }
        return list;
    }

    public BTreeNode search(T k) {
        return (root == null) ? null : searchRec(root, k);
    }

    private BTreeNode searchRec(BTreeNode node, T k) {
        int i = 0;
        while (i < node.n && k.compareTo(node.keys.get(i)) > 0)
            i++;

        if (i < node.n && node.keys.get(i).compareTo(k) == 0)
            return node;

        if (node.leaf) return null;

        return searchRec(node.children.get(i), k);
    }

    public void insert(T k) {
        if (root == null) {
            root = new BTreeNode(t, true);
            root.keys.set(0, k);
            root.n = 1;
        } else {
            if (root.n == 2 * t - 1) {
                BTreeNode s = new BTreeNode(t, false);
                s.children.set(0, root);
                splitChild(s, 0, root);
                int i = 0;
                if (s.keys.get(0).compareTo(k) < 0) {
                    i++;
                }
                insertNonFull(s.children.get(i), k);
                root = s;
            } else {
                insertNonFull(root, k);
            }
        }
    }

    private void insertNonFull(BTreeNode node, T k) {
        int i = node.n - 1;
        if (node.leaf) {
            while (i >= 0 && node.keys.get(i).compareTo(k) > 0) {
                node.keys.set(i + 1, node.keys.get(i));
                i--;
            }
            node.keys.set(i + 1, k);
            node.n = node.n + 1;
        } else {
            while (i >= 0 && node.keys.get(i).compareTo(k) > 0) {
                i--;
            }
            if (node.children.get(i + 1).n == 2 * t - 1) {
                splitChild(node, i + 1, node.children.get(i + 1));
                if (node.keys.get(i + 1).compareTo(k) < 0) {
                    i++;
                }
            }
            insertNonFull(node.children.get(i + 1), k);
        }
    }

    private void splitChild(BTreeNode parent, int i, BTreeNode child) {
        BTreeNode z = new BTreeNode(t, child.leaf);
        z.n = t - 1;

        for (int j = 0; j < t - 1; j++) {
            z.keys.set(j, child.keys.get(j + t));
        }

        if (!child.leaf) {
            for (int j = 0; j < t; j++) {
                z.children.set(j, child.children.get(j + t));
            }
        }

        child.n = t - 1;

        for (int j = parent.n; j >= i + 1; j--) {
            parent.children.set(j + 1, parent.children.get(j));
        }
        parent.children.set(i + 1, z);

        for (int j = parent.n - 1; j >= i; j--) {
            parent.keys.set(j + 1, parent.keys.get(j));
        }

        parent.keys.set(i, child.keys.get(t - 1));
        parent.n = parent.n + 1;
    }

    public List<T> rangeQuery(T start, T end) {
        List<T> result = new ArrayList<>();
        if (root != null) {
            rangeQueryRec(root, start, end, result);
        }
        return result;
    }

    private void rangeQueryRec(BTreeNode node, T start, T end, List<T> result) {
        int i;
        for (i = 0; i < node.n; i++) {
            T key = node.keys.get(i);
            if (!node.leaf && key.compareTo(start) > 0) {
                rangeQueryRec(node.children.get(i), start, end, result);
            }
            if (key.compareTo(start) >= 0 && key.compareTo(end) <= 0) {
                result.add(key);
            }
        }
        if (!node.leaf && node.keys.get(i - 1).compareTo(end) < 0) {
            rangeQueryRec(node.children.get(i), start, end, result);
        }
    }
}
