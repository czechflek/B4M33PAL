package pal;

import java.util.*;

public class Pathfinder {
    private int[][] adjancencyMatrix;

    public List<List<Node>> neighbors = new ArrayList<>();

    private Integer numVertices;
    private Queue<Node> queue = new LinkedList<>();
    private Integer destinationV;
    private Integer startV;
    private int[] distances;
    private PairingHeap priority = new PairingHeap();


    public Pathfinder(Integer numVertices) {
        this.numVertices = numVertices;
        adjancencyMatrix = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            neighbors.add(new ArrayList<>());
        }
    }

    public void addEdge(Integer node1, Integer node2, Integer weight) {
        adjancencyMatrix[node1][node2] = weight;
        adjancencyMatrix[node2][node1] = weight;
        neighbors.get(node1).add(new Node(node2, weight));
        neighbors.get(node2).add(new Node(node1, weight));
    }

    public Integer findEdgeDistance(Integer startV, Integer destinationV) {
        this.destinationV = destinationV;
        this.startV = startV;
        boolean[] closed = new boolean[numVertices];
        distances = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        distances[startV] = 0;
        queue.add(new Node(startV, 0));

        Node currentNode;
        while (queue.peek() != null) {
            currentNode = queue.poll();
            if (currentNode.getId() == destinationV)
                break;

            closed[currentNode.getId()] = true;
            processNeighbors(currentNode, closed, distances);

        }
        return distances[destinationV];
    }

    public List<List<Integer>> getPath(List<Integer> path, Integer currentNode, Integer depth) {
        List<List<Integer>> res = new LinkedList<>();
        path.add(currentNode);
        if (currentNode.equals(startV)) {
            res.add(path);
            return res;
        }
        for (Node n : neighbors.get(currentNode)) {
            if (distances[n.getId()] == depth) {
                List<Integer> newPath = new LinkedList<>();
                newPath.addAll(path);
                res.addAll(getPath(newPath, n.getId(), depth - 1));
            }
        }
        return res;
    }

    public Integer runModifiedPrimm(List<Integer> tree, Integer limitLength) {
        Integer totalDistance = getBaseDistance(tree);
        for (Integer n : tree) {
            priority.addAll(neighbors.get(n));
        }

        boolean closed[] = new boolean[numVertices];
        for (Integer n : tree) {
            closed[n] = true;
        }

        for (int i = tree.size(); i < numVertices; i++) {
            Node n;
            do {
                n = priority.extractMin();
            } while (closed[n.getId()]);
            priority.addAll(neighbors.get(n.getId()));
            closed[n.getId()] = true;
            totalDistance += n.getDistance();
            if (totalDistance >= limitLength) {
                return limitLength;
            }
        }
        return totalDistance;
    }

    public Integer getBaseDistance(List<Integer> tree) {
        Integer total = 0;
        for (int i = 0; i < tree.size() - 1; i++) {
            total += adjancencyMatrix[tree.get(i)][tree.get(i + 1)];
        }
        return total;
    }

    private void processNeighbors(Node currentNode, boolean[] closed, int[] distances) {
        int newDistance;

        for (int i = 0; i < numVertices; i++) {
            if (!closed[i] && adjancencyMatrix[currentNode.getId()][i] > 0) {
                newDistance = distances[currentNode.getId()] + 1;
                if (distances[i] > newDistance) {
                    distances[i] = newDistance;
                    if (newDistance < distances[destinationV])
                        queue.add(new Node(i, newDistance));
                }
            }
        }
    }

    public Integer getMaxDistance() {
        return distances[destinationV];
    }
}
