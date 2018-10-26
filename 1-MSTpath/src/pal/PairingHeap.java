package pal;

import java.util.ArrayList;
import java.util.List;

public class PairingHeap {
    private HeapNode root = null;
    private List<HeapNode> tree = new ArrayList<>(1000);


    public void add(Node in) {
        HeapNode node = new HeapNode(in.getId(), in.getDistance());
        if (root == null) {
            root = node;
        } else {
            root = compareAndLink(root, node);
        }
    }

    public void addAll(List<Node> ins) {
        for (Node n : ins) {
            add(n);
        }
    }

    public Node extractMin() {
        if (empty()) {
            return null;
        }

        Node res = new Node(root.id, root.weight);
        if (root.leftChild == null) {
            root = null;
        } else {
            root = combineSiblings(root.leftChild);
        }
        return res;
    }

    public void clear() {
        root = null;
    }

    public boolean empty() {
        return root == null;
    }

    private HeapNode compareAndLink(HeapNode first, HeapNode second) {
        if (second == null) {
            return first;
        }

        if (second.weight < first.weight) {
            second.parent = first.parent;
            first.parent = second;
            first.nextSibling = second.leftChild;
            if (first.nextSibling != null) {
                first.nextSibling.parent = first;
            }
            second.leftChild = first;
            return second;
        } else {
            second.parent = first;
            first.nextSibling = second.nextSibling;
            if (first.nextSibling != null) {
                first.nextSibling.parent = first;
            }
            second.nextSibling = first.leftChild;
            if (second.nextSibling != null) {
                second.nextSibling.parent = second;
            }
            first.leftChild = second;
            return first;
        }
    }

    private HeapNode combineSiblings(HeapNode left) {
        if (left.nextSibling == null) {
            return left;
        }

        int totalSiblings = 0;
        while (left != null) {
            if (totalSiblings < tree.size()) {
                tree.set(totalSiblings, left);
            } else {
                tree.add(left);
            }

            left.parent.nextSibling = null;
            left = left.nextSibling;
            totalSiblings++;
        }

        int i = 0;
        for (; i +1 < totalSiblings; i += 2) {
            tree.set(i, compareAndLink(tree.get(i), tree.get(i + 1)));
        }

        int j = i - 2;
        if (j == totalSiblings - 3) { // odd number of trees
            tree.set(j, compareAndLink(tree.get(j), tree.get(j + 2)));
        }
        for (; j >= 2; j -= 2) {
            tree.set(j - 2, compareAndLink(tree.get(j - 2), tree.get(j)));
        }
        return tree.get(0);
    }

    public class HeapNode {
        int id;
        int weight;
        HeapNode leftChild = null;
        HeapNode nextSibling = null;
        HeapNode parent = null;

        public HeapNode(int id, int weight) {
            this.id = id;
            this.weight = weight;
        }
    }
}
