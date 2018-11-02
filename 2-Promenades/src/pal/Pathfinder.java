package pal;

import java.util.LinkedList;
import java.util.List;

public class Pathfinder {
    private int numVertices;
    private int numBlueVs;
    int[] blueVs;
    int[][] blueDistances;

    private List<Integer> neighbors[];

    public Pathfinder(int numVertices, int[] blueVs) {
        this.numVertices = numVertices;
        this.numBlueVs = blueVs.length;
        this.blueVs = blueVs;

        neighbors = new LinkedList[numVertices];
        for (int i = 0; i < neighbors.length; i++) {
            neighbors[i] = new LinkedList<>();
        }

        blueDistances = new int[numBlueVs][numBlueVs];
    }

    public void addEdge(int from, int to){
        neighbors[from].add(to);
    }

    private void dijkstra(int start){
        int[] distances = new int[numVertices];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        
    }


}
