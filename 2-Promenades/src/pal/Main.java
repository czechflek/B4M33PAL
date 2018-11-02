package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        int from, to;

        //Read data
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            //Init counts
            String line = in.readLine();
            String[] tokens = line.split(" ");
            int numV = Integer.parseInt(tokens[0]);
            int numBlueVs = Integer.parseInt(tokens[1]);
            int numEdges = Integer.parseInt(tokens[2]);

            //Blue vertices
            line = in.readLine();
            tokens = line.split(" ");
            int[] blueVs = new int[numBlueVs];
            for (int i = 0; i < numBlueVs; i++) {
                blueVs[i] = Integer.parseInt(tokens[i]);
            }

            for (int i = 0; i < numEdges; i++) {
                line = in.readLine();
                tokens = line.split(" ");
                from = Integer.parseInt(tokens[0]);
                to = Integer.parseInt(tokens[1]);
            }
        } catch (IOException ex) {
            System.err.println("Whopsie");
        }
    }
}
