package pal;

import java.util.Scanner;

public class Main {

    private static Integer[][] graph;
    private static Integer[] distance;
    private static Integer[] previous;
    private static Integer numVertices;
    private static Integer numEdges;
    private static Integer startV;
    private static Integer endV;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int a, b, weight;


        //Read data
        try {
            numVertices = s.nextInt();
            numEdges = s.nextInt();
            startV = s.nextInt();
            endV = s.nextInt();

            graph = new Integer[numVertices][numVertices];
            previous = new Integer[numVertices];

            distance = new Integer[numVertices];
            for (int i = 0; i < numVertices; i++) {
                distance[i] = Integer.MAX_VALUE;
            }

            for (int i = 0; i < numEdges; i++) {
                a = s.nextInt();
                b = s.nextInt();
                weight = s.nextInt();

                graph[a][b] = weight;
                graph[b][a] = weight;
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }

    }

    private static void
}
