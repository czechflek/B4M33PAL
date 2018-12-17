package pal;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    private TrieNode root = new TrieNode();
    private String sequence;

    void insertWord(String word, int wordId) {
        TrieNode currentNode = root;
        for (int i = 0; i < word.length(); i++) {
            currentNode = currentNode.getChildren().computeIfAbsent(word.charAt(i), k -> new TrieNode());
            if(currentNode.isWord())
                return;
        }
        currentNode.setWordId(wordId);
    }

    Result search(int offset) {
        TrieNode currentNode = root;
        for (int i = offset; i < sequence.length(); i++) {
            currentNode = currentNode.getChildren().get(sequence.charAt(i));
            if (currentNode == null) {
                return null;
            } else if (currentNode.isWord()) {
                return new Result(currentNode.wordId, i);
            }
        }
        return null;
    }

    void setSequence(String sequence) {
        this.sequence = sequence;
    }

    class Result {
        int wordId;
        int endPosition;

        Result(int wordId, int endPosition) {
            this.wordId = wordId;
            this.endPosition = endPosition;
        }
    }

    private class TrieNode {
        private static final int NOT_WORD = -1;

        private Map<Character, TrieNode> children = new HashMap<>();
        int wordId = NOT_WORD;

        Map<Character, TrieNode> getChildren() {
            return children;
        }

        boolean isWord() {
            return wordId > NOT_WORD;
        }

        int getWordId() {
            return wordId;
        }

        void setWordId(int wordId) {
            this.wordId = wordId;
        }
    }
}
