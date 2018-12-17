package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {

            //Init counts
            String sequence = in.readLine();
            Integer dictSize = Integer.parseInt(in.readLine());

            Dictionary dict = new Dictionary(dictSize);
            for (int i = 0; i < dictSize; i++) {
                dict.addWord(in.readLine());
            }

        } catch (IOException ex) {
            System.err.println("Whopsie");
        }
    }
}
