package pal;

import java.util.*;

public class Pathfinder {
    //private int[][] adjancencyMatrix;


    private Integer numVertices;
    private Queue<Node> queue = new LinkedList<>();
    private Integer destinationV;
    private Integer startV;
    private int[] distances;


    //private List<Edge> edges = new ArrayList<>(1000);
    private List<Edge> buckets[] = (LinkedList<Edge>[]) new LinkedList[201];
    private int[] parents;
    private List<Node> neighbors[];


    public Pathfinder(Integer numVertices) {
        this.numVertices = numVertices;
        //adjancencyMatrix = new int[numVertices][numVertices];
        neighbors = new LinkedList[numVertices];
        for (int i = 0; i < neighbors.length; i++) {
            neighbors[i] = new LinkedList<>();
        }
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }

    }

    public void addEdge(Integer node1, Integer node2, Integer weight) {
        neighbors[node1].add(new Node(node2, weight));
        neighbors[node2].add(new Node(node1, weight));
        //adjancencyMatrix[node1][node2] = weight;
        //adjancencyMatrix[node2][node1] = weight;
        buckets[weight].add(new Edge(node1, node2)); //mozne odstranit weight z Edge objektu, mozna
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

        for(Node n : neighbors[currentNode]){
            if (distances[n.getId()] == depth) {
                List<Integer> newPath = new LinkedList<>();
                newPath.addAll(path);
                res.addAll(getPath(newPath, n.getId(), depth - 1));
            }
        }

//        for (int i = 0; i < numVertices; i++) {
//            if (adjancencyMatrix[currentNode][i] > 0) {
//                if (distances[i] == depth) {
//                    List<Integer> newPath = new LinkedList<>();
//                    newPath.addAll(path);
//                    res.addAll(getPath(newPath, i, depth - 1));
//                }
//            }
//        }
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
            //totalLength += adjancencyMatrix[src][dest];
            for(Node n : neighbors[src]){
                if(n.getId().equals(dest)) {
                    totalLength += n.getDistance();
                    break;
                }
            }
        }

        int startParent;
        int endParent;
        for (int i = 0; i < 201; i++) {
            for (Edge e : buckets[i]) {
                startParent = findSet(e.getStart());
                endParent = findSet(e.getEnd());
                if (startParent != endParent) {
                    unionSet(startParent, endParent);
                    totalLength += i;
                    if (totalLength >= limitLength) {
                        return limitLength;
                    }
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


//    private void processNeighbors(Node currentNode, boolean[] closed, int[] distances) {
//        int newDistance;
//
//        for (int i = 0; i < numVertices; i++) {
//            if (!closed[i] && adjancencyMatrix[currentNode.getId()][i] > 0) {
//                newDistance = distances[currentNode.getId()] + 1;
//                if (distances[i] > newDistance) {
//                    distances[i] = newDistance;
//                    if (newDistance < distances[destinationV])
//                        queue.add(new Node(i, newDistance));
//                }
//            }
//        }
//    }

    private void processNeighbors(Node currentNode, boolean[] closed, int[] distances) {
        int newDistance;

        for (Node n: neighbors[currentNode.getId()]) {
            if (!closed[n.getId()]) {
                newDistance = distances[currentNode.getId()] + 1;
                if (distances[n.getId()] > newDistance) {
                    distances[n.getId()] = newDistance;
                    if (newDistance < distances[destinationV])
                        queue.add(new Node(n.getId(), newDistance));
                }
            }
        }
    }

    public Integer getMaxDistance() {
        return distances[destinationV];
    }

    public void sortEdges() {
        //edges = bucketSort(edges, 5000);
        //edges.sort(new SortByWeight());
    }

    private class Edge {
        int start;
        int end;

        public Edge(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

    }

//    public static List<Edge> bucketSort(List<Edge> edges, int bucketCount) {
//        Edge high = edges.get(0);
//        Edge low = high;
//        for (int i = 1; i < edges.size(); i++) {
//            if (edges.get(i).compareTo(high) > 0) high = edges.get(i);
//            if (edges.get(i).compareTo(low) < 0) low = edges.get(i);
//        }
//        double interval = ((double) (high.getWeight() - low.getWeight() + 1)) / bucketCount; //range of one bucket
//
//        ArrayList<Edge> buckets[] = new ArrayList[bucketCount];
//        for (int i = 0; i < bucketCount; i++) {
//            buckets[i] = new ArrayList<>();
//        }
//
//        for (int i = 0; i < edges.size(); i++) {
//            buckets[(int) ((edges.get(i).getWeight() - low.weight) / interval)].add(edges.get(i));
//        }
//
//        int pointer = 0;
//        for (int i = 0; i < buckets.length; i++) {
//            Collections.sort(buckets[i]);
//            for (int j = 0; j < buckets[i].size(); j++) {
//                edges.set(pointer, buckets[i].get(j));
//                pointer++;
//            }
//        }
//        return edges;
//    }

//
//    public static class SortByWeight implements Comparator<Edge> {
//        @Override
//        public int compare(Edge o1, Edge o2) {
//            return Integer.compare(o1.weight, o2.weight);
//        }
//    }
}
