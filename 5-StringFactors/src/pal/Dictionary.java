package pal;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private final List<String> dict;
    private final List<Integer> wordLengths;
    private final int dictSize;

    public Dictionary(int dictSize) {
        this.dict = new ArrayList<>(dictSize);
        this.wordLengths = new ArrayList<>(dictSize);
        this.dictSize = dictSize;
    }

    public void addWord(String word){
        dict.add(word);
        wordLengths.add(word.length());
    }

}
