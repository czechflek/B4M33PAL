package pal;

import java.util.*;

public class Pathfinder {
    private int[][] adjancencyMatrix;


    private Integer numVertices;
    private Integer numEdges = 0;
    private Queue<Node> queue = new LinkedList<>();
    private Integer destinationV;
    private Integer startV;
    private int[] distances;


    private List<Edge> edges = new ArrayList<>(1000);
    int[] parents;


    public Pathfinder(Integer numVertices) {
        this.numVertices = numVertices;
        adjancencyMatrix = new int[numVertices][numVertices];


    }

    public void addEdge(Integer node1, Integer node2, Integer weight) {
        adjancencyMatrix[node1][node2] = weight;
        adjancencyMatrix[node2][node1] = weight;
        edges.add(new Edge(weight, node1, node2));
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
        for (int i = 0; i < numVertices; i++) {
            if (adjancencyMatrix[currentNode][i] > 0) {
                if (distances[i] == depth) {
                    List<Integer> newPath = new LinkedList<>();
                    newPath.addAll(path);
                    res.addAll(getPath(newPath, i, depth - 1));
                }
            }
        }
        return res;
    }


    public int runKruskal(List<Integer> tree, int limitLength) {
        int totalLength = 0;
        parents = new int[numVertices];
        for (int i = 0; i < parents.length; i++) {
            parents[i] = i;
        }


        for (int i = 0; i < tree.size() - 1; i++) {
            int src = tree.get(i);
            int dest = tree.get(i + 1);
            parents[src] = startV;
            parents[dest] = startV;
            totalLength += adjancencyMatrix[src][dest];


        }

        int startParent;
        int endParent;
        for (Edge e : edges) {
            startParent = findSet(e.getStart());
            endParent = findSet(e.getEnd());
            if (startParent != endParent) {
                unionSet(startParent, endParent);
                totalLength += e.weight;
                if (totalLength >= limitLength) {
                    return limitLength;
                }
            }
        }
        return totalLength;
    }

    private int findSet(int start) {
        int i = start;
        while (parents[i] != i) {
            i = parents[i];
        }
        int end = i;
        i = start;
        int j;
        while (parents[i] != i) {
            j = parents[i];
            parents[i] = end;
            i = j;
        }
        return i;
    }

    private void unionSet(int i, int j) {
        parents[i] = parents[j];
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

    public void sortEdges() {
        edges.sort(new SortByWeight());
    }

    private void adjacencyToEdges() {
        edges = new ArrayList<>(numEdges);
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (adjancencyMatrix[i][j] > 0) {
                    edges.add(new Edge(adjancencyMatrix[i][j], i, j));
                }
            }
        }
    }

    private class Edge {
        int weight;
        int start;
        int end;

        public Edge(int weight, int start, int end) {
            this.weight = weight;
            this.start = start;
            this.end = end;
        }

        public int getWeight() {
            return weight;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }

    public static class SortByWeight implements Comparator<Edge> {
        @Override
        public int compare(Edge o1, Edge o2) {
            return Integer.compare(o1.weight, o2.weight);
        }
    }
}
