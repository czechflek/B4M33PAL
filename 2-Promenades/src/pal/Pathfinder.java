package pal;

import java.util.*;

class Pathfinder {
    private int numVertices;
    private int numBlueVs;
    private List<Integer> blueVs;
    private int[][] blueDistances;
    private int[] maxDistanceToBlue;
    private int[] distances;
    private int[] reversedDistances;
    private boolean[] open;
    private boolean[] reversedOpen;
    private List<PathPair> blueNeighbors[];
    private List<PathPair> blueIncommingNeighbors[];
    private List<Integer> neighbors[];
    private List<Integer> reversedNeighbors[];
    private Set<Integer> blueSet = new HashSet<>();
    //Queue<Integer> outsideNode = new LinkedList<>();

    //TARJAN
    private static final int UNVISITED = -1;
    private Deque<Integer> tarjanStack = new LinkedList<>();
    private boolean[] onStack;
    private int[] visited;
    private int[] low;
    private int id = 0;
    private int sccTotal;
    //    private  Map<Integer, Integer> componentToBlue;
    private int[] componentToBlue;
    private List<List<Integer>> components;

    Pathfinder(int numVertices, List<Integer> blueVs) {
        this.numVertices = numVertices;
        this.numBlueVs = blueVs.size();
        this.blueVs = blueVs;
        blueSet.addAll(blueVs);

        neighbors = new LinkedList[numVertices];
        reversedNeighbors = new LinkedList[numVertices];
        for (int i = 0; i < neighbors.length; i++) {
            neighbors[i] = new LinkedList<>();
            reversedNeighbors[i] = new LinkedList<>();
        }

        blueDistances = new int[numBlueVs][numBlueVs];

    }

    void addEdge(int from, int to) {
        neighbors[from].add(to);
        reversedNeighbors[to].add(from);
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
                    if (nodeDistance[n.node] > bestNodeDistance) {
                        bestNodeDistance = nodeDistance[n.node];
                        bestDistance = distance[n.node];
                    } else if (nodeDistance[n.node] == bestNodeDistance) {
                        if (distance[n.node] < bestDistance) {
                            bestDistance = distance[n.node];
                        }
                    }
                } else if (nodeDistance[n.node] == (nodeDistance[current] + 1)) {
                    if (distance[n.node] > (distance[current] + n.distance)) {
                        distance[n.node] = distance[current] + n.distance;
                    }
                    if (nodeDistance[n.node] > bestNodeDistance) {
                        bestNodeDistance = nodeDistance[n.node];
                        bestDistance = distance[n.node];
                    } else if (nodeDistance[n.node] == bestNodeDistance) {
                        if (distance[n.node] < bestDistance) {
                            bestDistance = distance[n.node];
                        }
                    }
                }
            }
        }

        return new Result(bestNodeDistance + 1, bestDistance);
    }

    void findComponents() {
        tarjan();
        buildComponents();
        getIntraComponentDistances();
        calculateAllBlueDistances();
        //printBlueDistances();
    }

    private void tarjan() {
        onStack = new boolean[numVertices];
        visited = new int[numVertices];
        low = new int[numVertices];

        for (int i = 0; i < visited.length; i++) {
            visited[i] = UNVISITED;
        }

        for (int i = 0; i < visited.length; i++) {
            if (visited[i] == UNVISITED) {
                dfs(i);
            }
        }
    }

    private void dfs(int node) {
        tarjanStack.push(node);
        onStack[node] = true;
        visited[node] = id;
        low[node] = id;
        id++;

        for (Integer next : neighbors[node]) {
            if (visited[next] == UNVISITED) {
                dfs(next);
            }
            if (onStack[next]) {
                low[node] = min(low[node], low[next]);
            }
        }

        if (visited[node] == low[node]) {
            Integer n;
            while (!tarjanStack.isEmpty()) {
                n = tarjanStack.pop();
                onStack[n] = false;
                low[n] = visited[node];
                if (n == node) {
                    break;
                }
            }
            sccTotal++;
        }


    }

    private static int min(int first, int second) {
        if (first > second)
            return second;
        else
            return first;
    }

    private void buildComponents() {
        components = new ArrayList<>(numBlueVs);
        componentToBlue = new int[id];
        int componentId;
        for (int i = 0; i < numBlueVs; i++) {
            componentId = low[blueVs.get(i)];
            componentToBlue[componentId] = i;
            components.add(new LinkedList<>());
        }

        for (int i = 0; i < low.length; i++) {
            componentId = componentToBlue[low[i]];
            components.get(componentId).add(i);
        }
    }

    private void getIntraComponentDistances() {
        distances = new int[numVertices];
        reversedDistances = new int[numVertices];
        open = new boolean[numVertices];
        reversedOpen = new boolean[numVertices];
        maxDistanceToBlue = new int[numBlueVs];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
            reversedDistances[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < numBlueVs; i++) {
            twoWayComponentBFS(i);
        }
    }

    private List<Integer[]> twoWayComponentBFS(int blueIndex) {
        List<Integer[]> res = new ArrayList<>();
        componentBFS(blueIndex, false);
        componentBFS(blueIndex, true);
        return res;
    }

    private void componentBFS(int blueIndex, boolean reversed) {
        Queue<Integer> queue = new LinkedList<>();
        boolean[] open;
        int[] distances;

        if (reversed) {
            distances = this.reversedDistances;
            open = this.reversedOpen;
        } else {
            distances = this.distances;
            open = this.open;
        }

        int currentNode = blueVs.get(blueIndex);
        distances[currentNode] = 0;
        queue.add(currentNode);
        open[currentNode] = true;
        while (queue.peek() != null) {
            currentNode = queue.poll();
            componentBfsProcessNeighbors(currentNode, queue, distances, open, reversed);
        }

    }

    private void componentBfsProcessNeighbors(Integer parent, Queue<Integer> queue, int[] distances, boolean[] open, boolean reversed) {
        int newDistance;
        List<Integer> myNeighbors[];

        if (reversed) {
            myNeighbors = reversedNeighbors;
        } else {
            myNeighbors = neighbors;
        }

        for (Integer n : myNeighbors[parent]) {
            if (!open[n] && low[n] == low[parent]) {
                newDistance = distances[parent] + 1;
                distances[n] = newDistance;
                open[n] = true;
                queue.add(n);
            }
        }
    }

    private void calculateAllBlueDistances() {
        for (int thisBlueId = 0; thisBlueId < components.size(); thisBlueId++) {
            List<Integer> component = components.get(thisBlueId);
            for (Integer node : component) {
                for (Integer next : neighbors[node]) {
                    if (low[next] != low[node]) {
                        int nextBlueId = componentToBlue[low[next]];
                        int newDistance = distances[node] + reversedDistances[next] + 1;

                        if (blueDistances[thisBlueId][nextBlueId] == 0 || blueDistances[thisBlueId][nextBlueId] > newDistance) {
                            blueDistances[thisBlueId][nextBlueId] = newDistance;
                        }
                    }
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
