package pal;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    private TrieNode root = new TrieNode();
    private String sequence;

    void insertWord(String word, int wordId) {
        TrieNode currentNode = root;
        TrieNode next;
        for (int i = 0; i < word.length(); i++) {
            next = currentNode.getChild(word.charAt(i));
            if (next == null) {
                currentNode = currentNode.setChild(word.charAt(i));
            } else {
                currentNode = next;
                if (currentNode.isWord())
                    return;
            }

        }
        currentNode.setWordId(wordId);
    }

    Result search(int offset) {
        TrieNode currentNode = root;
        for (int i = offset; i < sequence.length(); i++) {
            currentNode = currentNode.getChild(sequence.charAt(i));;
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

        //private Map<Character, TrieNode> children = new HashMap<>();
        private TrieNode[] children = new TrieNode[26];
        int wordId = NOT_WORD;

        TrieNode getChild(char c) {
            return children[c - 'a'];
        }

        TrieNode setChild(char c) {
            TrieNode node = new TrieNode();
            children[c - 'a'] = node;
            return node;
        }

        boolean isWord() {
            return wordId > NOT_WORD;
        }

        void setWordId(int wordId) {
            this.wordId = wordId;
        }
    }
}
