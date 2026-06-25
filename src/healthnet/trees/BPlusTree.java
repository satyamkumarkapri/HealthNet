package healthnet.trees;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic B+ Tree Implementation for indexing Medical Records.
 * Optimized for range queries by linking leaf nodes.
 */
public class BPlusTree<T extends Comparable<T>> {
    
    private int m; // degree
    private InternalNode root;
    private LeafNode firstLeaf;

    private abstract class Node {
        List<T> keys;
        Node() {
            keys = new ArrayList<>();
        }
        abstract boolean isOverflowed();
    }

    private class InternalNode extends Node {
        List<Node> children;
        InternalNode() {
            super();
            children = new ArrayList<>();
        }
        @Override
        boolean isOverflowed() {
            return children.size() > m;
        }
    }

    private class LeafNode extends Node {
        LeafNode next;
        LeafNode() {
            super();
        }
        @Override
        boolean isOverflowed() {
            return keys.size() > m - 1;
        }
    }

    public BPlusTree(int degree) {
        this.m = degree;
        this.firstLeaf = new LeafNode();
        this.root = null;
    }

    public void insert(T key) {
        if (root == null) {
            firstLeaf.keys.add(key);
            firstLeaf.keys.sort(Comparable::compareTo);
            if (firstLeaf.isOverflowed()) {
                splitFirstLeaf();
            }
            return;
        }
        
        LeafNode leaf = findLeafNode(root, key);
        leaf.keys.add(key);
        leaf.keys.sort(Comparable::compareTo);
        
        if (leaf.isOverflowed()) {
            splitLeafNode(leaf);
        }
    }

    private LeafNode findLeafNode(InternalNode node, T key) {
        int i = 0;
        while (i < node.keys.size() && key.compareTo(node.keys.get(i)) >= 0) {
            i++;
        }
        Node child = node.children.get(i);
        if (child instanceof LeafNode) {
            return (LeafNode) child;
        } else {
            return findLeafNode((InternalNode) child, key);
        }
    }

    private void splitFirstLeaf() {
        int mid = firstLeaf.keys.size() / 2;
        LeafNode newLeaf = new LeafNode();
        newLeaf.keys.addAll(firstLeaf.keys.subList(mid, firstLeaf.keys.size()));
        firstLeaf.keys.subList(mid, firstLeaf.keys.size()).clear();
        
        newLeaf.next = firstLeaf.next;
        firstLeaf.next = newLeaf;
        
        InternalNode newRoot = new InternalNode();
        newRoot.keys.add(newLeaf.keys.get(0));
        newRoot.children.add(firstLeaf);
        newRoot.children.add(newLeaf);
        
        root = newRoot;
    }

    private void splitLeafNode(LeafNode leaf) {
        // Simplified insert implementation. In a full B+ tree, you recursively propagate splits upwards.
        // For demonstration of concept, let's keep it limited or assume a shallow tree.
        int mid = leaf.keys.size() / 2;
        LeafNode newLeaf = new LeafNode();
        newLeaf.keys.addAll(leaf.keys.subList(mid, leaf.keys.size()));
        leaf.keys.subList(mid, leaf.keys.size()).clear();

        newLeaf.next = leaf.next;
        leaf.next = newLeaf;

        T upKey = newLeaf.keys.get(0);
        insertIntoParent(leaf, upKey, newLeaf);
    }

    private void insertIntoParent(Node left, T key, Node right) {
        if (left == firstLeaf && root == null) {
            // Handled in insert
            return;
        }
        InternalNode parent = findParent(root, left);
        if (parent == null) {
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(key);
            newRoot.children.add(left);
            newRoot.children.add(right);
            root = newRoot;
            return;
        }

        int i = 0;
        while (i < parent.children.size() && parent.children.get(i) != left) {
            i++;
        }
        parent.keys.add(i, key);
        parent.children.add(i + 1, right);

        if (parent.isOverflowed()) {
            splitInternalNode(parent);
        }
    }

    private InternalNode findParent(InternalNode current, Node child) {
        if (current.children.contains(child)) return current;
        for (Node c : current.children) {
            if (c instanceof InternalNode) {
                InternalNode p = findParent((InternalNode) c, child);
                if (p != null) return p;
            }
        }
        return null;
    }

    private void splitInternalNode(InternalNode node) {
        int mid = node.keys.size() / 2;
        InternalNode newNode = new InternalNode();
        
        newNode.keys.addAll(node.keys.subList(mid + 1, node.keys.size()));
        newNode.children.addAll(node.children.subList(mid + 1, node.children.size()));
        
        T upKey = node.keys.get(mid);
        
        node.keys.subList(mid, node.keys.size()).clear();
        node.children.subList(mid + 1, node.children.size()).clear();
        
        insertIntoParent(node, upKey, newNode);
    }

    public List<T> rangeQuery(T start, T end) {
        List<T> result = new ArrayList<>();
        LeafNode current = firstLeaf;
        
        // Find starting leaf
        while (current != null) {
            boolean startAdding = false;
            for (T key : current.keys) {
                if (key.compareTo(start) >= 0 && key.compareTo(end) <= 0) {
                    result.add(key);
                }
                if (key.compareTo(end) > 0) {
                    return result; // Early exit since keys are sorted
                }
            }
            current = current.next;
        }
        return result;
    }

    public void traverse() {
        LeafNode current = firstLeaf;
        System.out.println("B+ Tree Leaves (Linked List Traversal):");
        while (current != null) {
            System.out.print(current.keys + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
}
