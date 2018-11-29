package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            int from, to;

            //Init counts
            String alphabet = in.readLine();
            Integer maxDistance = Integer.parseInt(in.readLine());
            String firstWordString = in.readLine();
            String secondWordString = in.readLine();

            Word word1 = new Word(firstWordString, maxDistance);
            Word word2 = new Word(secondWordString, maxDistance);

            Word.DistanceBounds bounds = Word.DistanceBounds.intersect(word1.getBounds(), word2.getBounds());
            Generator generator = new Generator(alphabet, bounds.getLower(), bounds.getUpper());

        } catch (IOException ex) {
            System.err.println("Whopsie");
        }
    }
}
