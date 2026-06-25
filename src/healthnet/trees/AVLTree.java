package healthnet.trees;

/**
 * Generic AVL Tree implementation extending BST to reuse common code.
 */
public class AVLTree<T extends Comparable<T>> extends BST<T> {

    private class AVLNode extends Node {
        int height;
        public AVLNode(T data) {
            super(data);
            this.height = 1;
        }
    }

    private int height(Node N) {
        if (N == null) return 0;
        return ((AVLNode) N).height;
    }

    private int getBalance(Node N) {
        if (N == null) return 0;
        return height(N.left) - height(N.right);
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        ((AVLNode) y).height = Math.max(height(y.left), height(y.right)) + 1;
        ((AVLNode) x).height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        ((AVLNode) x).height = Math.max(height(x.left), height(x.right)) + 1;
        ((AVLNode) y).height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    @Override
    public void insert(T data) {
        root = insertRecAVL(root, data);
    }

    private Node insertRecAVL(Node node, T data) {
        if (node == null) return new AVLNode(data);

        if (data.compareTo(node.data) < 0)
            node.left = insertRecAVL(node.left, data);
        else if (data.compareTo(node.data) > 0)
            node.right = insertRecAVL(node.right, data);
        else
            return node;

        ((AVLNode) node).height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && data.compareTo(node.left.data) < 0)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && data.compareTo(node.right.data) > 0)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }
    
    // Note: Deletion with balancing can be similarly implemented.
    // For simplicity, we stick to standard BST delete if not rebalanced, 
    // or we can override delete as well.
}
