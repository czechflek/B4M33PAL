package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            //Init counts
            String sequence = in.readLine();
            int dictSize = Integer.parseInt(in.readLine());

            /******************LOAD**********************/
            Trie trie = new Trie();
            trie.setSequence(sequence);
            List<Integer> wordLengths = new ArrayList<>(dictSize);
            for (int i = 0; i < dictSize; i++) {
                String word = in.readLine();
                trie.insertWord(word, i);
                wordLengths.add(word.length());
            }

            /******************FIND WORDS**********************/
            List<Integer>[] endPositions = new LinkedList[sequence.length()];
            for (int i = 0; i < endPositions.length; i++) {
                endPositions[i] = new LinkedList<>();
            }
            Trie.Result trieRes;
            for (int offset = 0; offset < sequence.length(); offset++) {
                trieRes = trie.search(offset);
                if(trieRes != null){
                    endPositions[trieRes.endPosition].add(trieRes.wordId);
                }
            }

            /******************FIND LONGEST POSSIBLE CONCAT**********************/
            int total = 0;
            int start = 0;
            for (int i = 0; i < sequence.length(); i++) {
                if(!endPositions[i].isEmpty()){
                    for(Integer wordId : endPositions[i]){
                        if(start <= (i - wordLengths.get(wordId) + 1)){
                            total++;
                            start = i + 1;
                            break;
                        }
                    }
                }
            }
            System.out.println(total);
        } catch (IOException ex) {
            System.err.println("Whopsie");
        }
    }
}
