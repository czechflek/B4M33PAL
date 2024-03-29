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

            //Init counts
            String alphabet = in.readLine();
            Integer maxDistance = Integer.parseInt(in.readLine());
            String firstWordString = in.readLine();
            String secondWordString = in.readLine();
            //to speed up
            String longer, shorter;
            if (firstWordString.length() < secondWordString.length()) {
                shorter = firstWordString;
                longer = secondWordString;
            } else {
                shorter = secondWordString;
                longer = firstWordString;
            }

            Word word = new Word(longer, maxDistance);
            Word wordAlt = new WordAlt(shorter, maxDistance);

            Word.DistanceBounds bounds = Word.DistanceBounds.intersect(word.getBounds(), wordAlt.getBounds());
            Generator generator = new Generator(alphabet, bounds.getLower(), bounds.getUpper(), maxDistance, longer);
            word.initCostStore(bounds.getUpper());
            wordAlt.initCostStore(bounds.getUpper());

            boolean correct= false;
            boolean res = false;
            List<String> results = new ArrayList<>();
            while (!res || results.isEmpty()) { //will skip one possible word
                res = generator.updateWord();
                correct = wordAlt.isNearTarget();
                if(correct){
                    //System.out.println(new String(Generator.Text.arr));
                    results.add(new String(Generator.Text.arr));
                }
            }
            Collections.sort(results);
            //System.out.println(word.cc);
            System.out.println(results.get(0));
        } catch (IOException ex) {
            System.err.println("Whopsie");
        }
    }
}
