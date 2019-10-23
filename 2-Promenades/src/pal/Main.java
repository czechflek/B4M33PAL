package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Yo listen up, here's the story
 * About a little guy that lives in a blue world
 * And all day and all night and everything he sees is just blue
 * Like him, inside and outside
 * Blue his house with a blue little window
 * And a blue Corvette
 * And everything is blue for him
 * And himself and everybody around
 * 'Cause he ain't got nobody to listen
 * <p>
 * I'm blue da ba dee da ba daa
 * Da ba dee da ba daa, da ba dee da ba daa, da ba dee da ba daa
 * Da ba dee da ba daa, da ba dee da ba daa, da ba dee da ba daa
 */
public class Main {

    public static void main(String[] args) {
        int from, to;

        //Read data
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            //Init counts
            String line = in.readLine();
            String[] tokens = line.split(" ");
            int numVs = Integer.parseInt(tokens[0]);
            int numBlueVs = Integer.parseInt(tokens[1]);
            int numEdges = Integer.parseInt(tokens[2]);

            //Blue vertices
            line = in.readLine();
            tokens = line.split(" ");
            int[] blueVs = new int[numBlueVs];
            for (int i = 0; i < numBlueVs; i++) {
                blueVs[i] = Integer.parseInt(tokens[i]);
            }

            Pathfinder pathfinder = new Pathfinder(numVs, blueVs);

            for (int i = 0; i < numEdges; i++) {
                line = in.readLine();
                tokens = line.split(" ");
                from = Integer.parseInt(tokens[0]);
                to = Integer.parseInt(tokens[1]);
                pathfinder.addEdge(from, to);
            }

            pathfinder.findComponents();
            Pathfinder.Result result = pathfinder.daBaDeeDaBaDaa();
            System.out.println(result.totalNodes + " " + result.totalDistance);
        } catch (IOException ex) {
            System.err.println("Whopsie");
        }
    }
}
