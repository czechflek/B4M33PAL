package pal;

import java.util.*;

class Pathfinder {
    private int numVertices;
    private int numBlueVs;
    private int[] blueVs;
    private int[][] blueDistances;
    private int[] distances;
    private boolean[] open;
    private List<Integer> neighbors[];

    //TARJAN
    private static final int UNVISITED = -1;
    private Deque<Integer> tarjanStack = new LinkedList<>();
    private boolean[] onStack;
    private int[] visited;
    private int[] low;
    private int id = 0;
    private int[] componentToBlue;
    private List<List<Integer>> components;

    private int topoId = 0;
    private int[] topoOrder;

    private boolean[] componentClosed;
    private boolean[] isBlue;
    private int[] blueNodeDistance;
    private int bestNodeDistance = 0;
    private int bestDistance = Integer.MAX_VALUE;


    Pathfinder(int numVertices, int[] blueVs) {
        this.numVertices = numVertices;
        this.numBlueVs = blueVs.length;
        this.blueVs = blueVs;
        componentClosed = new boolean[numBlueVs];
        isBlue = new boolean[numVertices];
        blueNodeDistance = new int[numBlueVs];

        for (int i = 0; i < numBlueVs; i++) {
            isBlue[blueVs[i]] = true;
        }

        neighbors = new LinkedList[numVertices];
        for (int i = 0; i < neighbors.length; i++) {
            neighbors[i] = new LinkedList<>();
        }

        blueDistances = new int[numBlueVs][numBlueVs];

    }

    void addEdge(int from, int to) {
        neighbors[from].add(to);
    }

    void findComponents() {
        tarjan();
        buildComponents();
        topoComponentsToBlue();
        remapLowToBlue();
    }

    Result daBaDeeDaBaDaa() {
        distances = new int[numVertices];
        open = new boolean[numVertices];
        for (int i = topoOrder.length - 1; i > 0; i--) {
            int current = topoOrder[i];
            componentBFS(current);

        }

        return new Result(bestNodeDistance + 1, bestDistance);
    }

    private void tarjan() {
        onStack = new boolean[numVertices];
        visited = new int[numVertices];
        low = new int[numVertices];
        topoOrder = new int[numBlueVs];

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
            topoOrder[topoId] = low[node];
            topoId++;
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
            componentId = low[blueVs[i]];
            componentToBlue[componentId] = i;
            components.add(new LinkedList<>());
        }

        for (int i = 0; i < low.length; i++) {
            componentId = componentToBlue[low[i]];
            components.get(componentId).add(i);
        }
    }

    private void componentBFS(int blueIndex) {
        Queue<Integer> queue = new LinkedList<>();
        boolean[] openNodes = new boolean[numVertices];
        List<Integer> adjacentComponents = new LinkedList<>();
        int currentNode = blueVs[blueIndex];
        queue.add(currentNode);
        open[currentNode] = true;
        while (queue.peek() != null) {
            currentNode = queue.poll();
            componentBfsProcessNeighbors(currentNode, queue, distances, openNodes, blueIndex, adjacentComponents);
        }
        for(Integer i: adjacentComponents){
            componentClosed[i] = false;
        }
        componentClosed[blueIndex] = true;

    }

    private void componentBfsProcessNeighbors(Integer parent, Queue<Integer> queue, int[] distances, boolean[] open, int startingBlueIndex, List<Integer> adjacentComponents) {
        int newDistance;

        for (Integer n : neighbors[parent]) {
            if (!open[n]) {
                if(!componentClosed[low[n]]) {
                    if(low[n] != low[parent] && low[parent] != startingBlueIndex){
                        adjacentComponents.add(low[n]);
                        componentClosed[low[n]] = true;
                    } else {
                        newDistance = distances[parent] + 1;

                        open[n] = true;
                        if (isBlue[n] && low[n] != startingBlueIndex) {
                            int blueIndex = low[n];
                            adjacentComponents.add(blueIndex);
                            componentClosed[blueIndex] = true;
                            if (blueNodeDistance[blueIndex] == 0 || blueNodeDistance[blueIndex] < (blueNodeDistance[startingBlueIndex] + 1)) {
                                distances[n] = newDistance;
                                blueNodeDistance[blueIndex] = blueNodeDistance[startingBlueIndex] + 1;
                                if (bestNodeDistance < blueNodeDistance[blueIndex]) {
                                    bestNodeDistance = blueNodeDistance[blueIndex];
                                    bestDistance = distances[n];
                                } else if (bestNodeDistance == blueNodeDistance[blueIndex]) {
                                    if (bestDistance > distances[n]) {
                                        bestDistance = distances[n];
                                    }
                                }
                            } else if (blueNodeDistance[blueIndex] == (blueNodeDistance[startingBlueIndex] + 1)) {
                                if (newDistance < distances[n]) {
                                    distances[n] = newDistance;
                                    if (bestNodeDistance < blueNodeDistance[blueIndex]) {
                                        bestNodeDistance = blueNodeDistance[blueIndex];
                                        bestDistance = distances[n];
                                    } else if (bestNodeDistance == blueNodeDistance[blueIndex]) {
                                        if (bestDistance > distances[n]) {
                                            bestDistance = distances[n];
                                        }
                                    }
                                }
                            }
                        } else {
                            distances[n] = newDistance;
                            queue.add(n);
                        }
                    }
                }
            }
        }
    }

    private void topoComponentsToBlue() {
        for (int i = 0; i < topoOrder.length; i++) {
            topoOrder[i] = componentToBlue[topoOrder[i]];
        }
    }

    private void remapLowToBlue() {
        for (int i = 0; i < low.length; i++) {
            low[i] = componentToBlue[low[i]];
        }
    }

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

    class Result {
        int totalNodes;
        int totalDistance;

        Result(int totalNodes, int totalDistance) {
            this.totalNodes = totalNodes;
            this.totalDistance = totalDistance;
        }
    }
}
