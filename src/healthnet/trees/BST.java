package healthnet.trees;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic Binary Search Tree implementation.
 */
public class BST<T extends Comparable<T>> {

    protected class Node {
        public T data;
        public Node left, right;

        public Node(T data) {
            this.data = data;
        }
    }

    protected Node root;

    public void insert(T data) {
        root = insertRec(root, data);
    }

    private Node insertRec(Node root, T data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }
        if (data.compareTo(root.data) < 0)
            root.left = insertRec(root.left, data);
        else if (data.compareTo(root.data) > 0)
            root.right = insertRec(root.right, data);
        return root;
    }

    public T search(T data) {
        Node res = searchRec(root, data);
        return res != null ? res.data : null;
    }

    private Node searchRec(Node root, T data) {
        if (root == null || root.data.compareTo(data) == 0)
            return root;
        if (root.data.compareTo(data) > 0)
            return searchRec(root.left, data);
        return searchRec(root.right, data);
    }

    public void delete(T data) {
        root = deleteRec(root, data);
    }

    private Node deleteRec(Node root, T data) {
        if (root == null) return root;

        if (data.compareTo(root.data) < 0)
            root.left = deleteRec(root.left, data);
        else if (data.compareTo(root.data) > 0)
            root.right = deleteRec(root.right, data);
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;

            root.data = minValue(root.right);
            root.right = deleteRec(root.right, root.data);
        }
        return root;
    }

    private T minValue(Node root) {
        T minv = root.data;
        while (root.left != null) {
            minv = root.left.data;
            root = root.left;
        }
        return minv;
    }

    public List<T> inorder() {
        List<T> list = new ArrayList<>();
        inorderRec(root, list);
        return list;
    }

    private void inorderRec(Node root, List<T> list) {
        if (root != null) {
            inorderRec(root.left, list);
            list.add(root.data);
            inorderRec(root.right, list);
        }
    }
    
    public List<T> preorder() {
        List<T> list = new ArrayList<>();
        preorderRec(root, list);
        return list;
    }

    private void preorderRec(Node root, List<T> list) {
        if (root != null) {
            list.add(root.data);
            preorderRec(root.left, list);
            preorderRec(root.right, list);
        }
    }

    public List<T> postorder() {
        List<T> list = new ArrayList<>();
        postorderRec(root, list);
        return list;
    }

    private void postorderRec(Node root, List<T> list) {
        if (root != null) {
            postorderRec(root.left, list);
            postorderRec(root.right, list);
            list.add(root.data);
        }
    }

    public int height() {
        return heightRec(root);
    }

    protected int heightRec(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(heightRec(node.left), heightRec(node.right));
    }
}
