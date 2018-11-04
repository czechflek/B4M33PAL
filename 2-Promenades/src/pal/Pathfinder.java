package pal;

import java.util.*;

class Pathfinder {
    private int numVertices;
    private int numBlueVs;
    List<Integer> blueVs;
    int[][] blueDistances;
    private List<PathPair> blueNeighbors[];
    private List<PathPair> blueIncommingNeighbors[];
    private List<Integer> neighbors[];
    private Set<Integer> blueSet = new HashSet<>();

    Pathfinder(int numVertices, List<Integer> blueVs) {
        this.numVertices = numVertices;
        this.numBlueVs = blueVs.size();
        this.blueVs = blueVs;
        blueSet.addAll(blueVs);

        neighbors = new LinkedList[numVertices];
        for (int i = 0; i < neighbors.length; i++) {
            neighbors[i] = new LinkedList<>();
        }

        blueDistances = new int[numBlueVs][numBlueVs];
    }

    void addEdge(int from, int to) {
        neighbors[from].add(to);
    }

    void calculateBlueDistances() {
        for (int i = 0; i < numBlueVs; i++) {
            bfs(blueVs.get(i), i);
        }
        //System.out.println(total);
        //printBlueDistances();
    }

    Result daBaDeeDaBaDaa() {
        blueNeighbors = new LinkedList[numBlueVs];
        blueIncommingNeighbors = new LinkedList[numBlueVs];
        int incoming[] = new int[numBlueVs];

        for (int i = 0; i < numBlueVs; i++) {
            blueIncommingNeighbors[i] = new LinkedList<>();
        }

        for (int i = 0; i < numBlueVs; i++) {
            blueNeighbors[i] = new LinkedList<>();
            for (int j = 0; j < numBlueVs; j++) {
                Integer b = blueDistances[i][j];
                if (!b.equals(Integer.MAX_VALUE) && !b.equals(0)) {
                    incoming[j]++;
                    blueNeighbors[i].add(new PathPair(j, blueDistances[i][j]));
                    blueIncommingNeighbors[j].add(new PathPair(i, blueDistances[i][j]));
                }
            }
        }
        Result tempRes;
        Result result = new Result(0, 1);
        List<Integer> topoSort = topologicalSort(incoming);

        return getBluestPath(topoSort);
    }


    private List<Integer> topologicalSort(int[] incoming) {
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> result = new LinkedList<>();
        for (int i = 0; i < numBlueVs; i++) {
            if (incoming[i] == 0)
                queue.add(i);
        }

        while (!queue.isEmpty()) {
            Integer n = queue.poll();
            result.add(n);
            for (PathPair m : blueNeighbors[n]) {
                incoming[m.node]--;
                if (incoming[m.node] == 0)
                    queue.add(m.node);
            }
        }
        return result;
    }

    private Result getBluestPath(List<Integer> sorted) {
        int[] distance = new int[numBlueVs];
        int[] nodeDistance = new int[numBlueVs];
        int bestNodeDistance = 0;
        int bestDistance = Integer.MAX_VALUE;

        for (int i = sorted.size() - 1; i >= 0; i--) {
            int current = sorted.get(i);
            for (PathPair n : blueIncommingNeighbors[current]) {
                if (nodeDistance[n.node] < (nodeDistance[current] + 1)) {
                    nodeDistance[n.node] = nodeDistance[current] + 1;
                    distance[n.node] = distance[current] + n.distance;
                    if(nodeDistance[n.node] > bestNodeDistance){
                        bestNodeDistance = nodeDistance[n.node];
                        bestDistance = distance[n.node];
                    } else if(nodeDistance[n.node] == bestNodeDistance){
                        if(distance[n.node] < bestDistance){
                            bestDistance = distance[n.node];
                        }
                    }
                } else if (nodeDistance[n.node] == (nodeDistance[current] + 1)){
                    if(distance[n.node] > (distance[current] + n.distance)){
                        distance[n.node] = distance[current] + n.distance;
                    }
                    if(nodeDistance[n.node] > bestNodeDistance){
                        bestNodeDistance = nodeDistance[n.node];
                        bestDistance = distance[n.node];
                    } else if(nodeDistance[n.node] == bestNodeDistance){
                        if(distance[n.node] < bestDistance){
                            bestDistance = distance[n.node];
                        }
                    }
                }
            }
        }

        return new Result(bestNodeDistance + 1, bestDistance);
    }

//    private Result getBluestPathOld(int current, int distance, int nodeLength) {
//        Result result = new Result(nodeLength, distance);
//        Result tempRes;
//
//        for (PathPair n : blueNeighbors[current]) {
//            tempRes = getBluestPath(n.node, distance + n.distance, nodeLength + 1);
//            result = chooseBetterResult(result, tempRes);
//        }
//
//        return result;
//    }

    private Result chooseBetterResult(Result main, Result candidate) {
        if (main.totalNodes < candidate.totalNodes) {
            return candidate;
        } else if (main.totalNodes == candidate.totalNodes) {
            if (main.totalDistance > candidate.totalDistance) {
                return candidate;
            }
        }
        return main;
    }

    //int total = 0;

    private void bfs(int start, int blueIndex) {
        Queue<Integer> queue = new LinkedList<>();
        int[] distances = new int[numVertices];
        boolean[] open = new boolean[numVertices];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        int currentNode;
        distances[start] = 0;
        queue.add(start);
        open[start] = true;
        while (queue.peek() != null) {
            currentNode = queue.poll();
            //total++;
//            if (closed[currentNode])
//                continue;
//            closed[currentNode] = true;
            bfsProcessNeighbors(currentNode, queue, distances, open);
        }

        for (int i = 0; i < numBlueVs; i++) {
            blueDistances[blueIndex][i] = distances[blueVs.get(i)];
        }

    }

    private void bfsProcessNeighbors(Integer parent, Queue<Integer> queue, int[] distances, boolean[] open) {
        int newDistance;
        for (Integer n : neighbors[parent]) {
            if (!open[n]) {
                newDistance = distances[parent] + 1;
                //if (distances[n] > newDistance) {
                    distances[n] = newDistance;
                //}
                open[n] = true;
                if (!blueSet.contains(n)) {
                    queue.add(n);
                }

            }
        }
    }

//    private void bfs(int start, int blueIndex) {
//        Queue<Integer> queue = new LinkedList<>();
//        int[] distances = new int[numVertices];
//        boolean[] closed = new boolean[numVertices];
//        boolean[] open = new boolean[numVertices];
//        for (int i = 0; i < distances.length; i++) {
//            distances[i] = Integer.MAX_VALUE;
//        }
//
//        int currentNode;
//        distances[start] = 0;
//        queue.add(start);
//        while (queue.peek() != null) {
//            currentNode = queue.poll();
//            if (closed[currentNode])
//                continue;
//            closed[currentNode] = true;
//            bfsProcessNeighbors(currentNode, queue, distances, closed, open);
//        }
//
//        for (int i = 0; i < numBlueVs; i++) {
//            blueDistances[blueIndex][i] = distances[blueVs.get(i)];
//        }
//
//    }
//
//    private void bfsProcessNeighbors(Integer parent, Queue<Integer> queue, int[] distances, boolean[] closed, boolean[] open) {
//        int newDistance;
//        for (Integer n : neighbors[parent]) {
//            if (!closed[n]) {
//                newDistance = distances[parent] + 1;
//                if (distances[n] > newDistance) {
//                    distances[n] = newDistance;
//                }
//                if (!blueSet.contains(n) && !open[n]) {
//                    open[n] = true;
//                    queue.add(n);
//                } else {
//                    closed[n] = true;
//                }
//
//            }
//        }
//    }

    private void printBlueDistances() {
        System.out.println("Blue distances:");
        System.out.println("------------------------------------");
        for (int i = 0; i < numBlueVs; i++) {
            String row = "";
            for (int j = 0; j < numBlueVs; j++) {
                row += blueDistances[i][j] + " ";
            }
            System.out.println(row);
        }
    }

    private class PathPair {
        Integer node;
        Integer distance;

        PathPair(Integer node, Integer distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    class Result {
        int totalNodes;
        int totalDistance;

        Result(int totalNodes, int totalDistance) {
            this.totalNodes = totalNodes;
            this.totalDistance = totalDistance;
        }
    }
}
