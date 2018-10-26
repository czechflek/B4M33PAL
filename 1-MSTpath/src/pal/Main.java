package pal;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        long startT = System.currentTimeMillis();
        int a, b, weight;

        //Read data
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line = in.readLine();
            String[] tokens = line.split(" ");
            int numVertices = Integer.parseInt(tokens[0]);
            int numEdges = Integer.parseInt(tokens[1]);
            int startV = Integer.parseInt(tokens[2]) - 1;
            int destinationV = Integer.parseInt(tokens[3]) - 1;

            Pathfinder pathfinder = new Pathfinder(numVertices);

            for (int i = 0; i < numEdges; i++) {
                line = in.readLine();
                tokens = line.split(" ");
                a = Integer.parseInt(tokens[0]) - 1;
                b = Integer.parseInt(tokens[1]) - 1;
                weight = Integer.parseInt(tokens[2]);

                pathfinder.addEdge(a, b, weight);
            }

            pathfinder.findEdgeDistance(startV, destinationV);
            List<List<Integer>> res = pathfinder.getPath(new LinkedList<>(), destinationV, pathfinder.getMaxDistance() - 1);

            Integer bestLength = Integer.MAX_VALUE;
            for (List<Integer> m : res) {
                bestLength = pathfinder.runModifiedPrimm(m, bestLength);
            }
            System.out.println(pathfinder.getMaxDistance() + " " + bestLength);
            System.out.println(System.currentTimeMillis() - startT);
        } catch (IOException ex) {
            System.err.println("Whopsie");
        }

    }
}
