package pal;

import java.util.Arrays;

class Generator {
    private char[] alphabet;
    private int minLength;
    private int maxLength;
    private int maxDistance;
    private int[] pointers;

    private int currentCase;
    private int currentAbcPos;


    Generator(String alphabet, int minLength, int maxLength, int maxDistance, String baseWord) {
        this.alphabet = alphabet.toCharArray();
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.maxDistance = maxDistance;
        Text.updated = 0;
        Text.baseWord = baseWord;
        this.currentCase = 0;
        init();
    }

    void goNext() {
        currentAbcPos = ++currentAbcPos % alphabet.length;
        Text.helperArr[Text.length - 1] = currentAbcPos;
        Text.arr[Text.length - 1] = alphabet[currentAbcPos];
        Text.updated = Text.length - 1;
        if (currentAbcPos == 0) {
            int pos = Text.length - 2;
            while (pos >= 0) {
                Text.helperArr[pos] = ++Text.helperArr[pos] % alphabet.length;
                Text.arr[pos] = alphabet[Text.helperArr[pos]];
                if (Text.helperArr[pos] == 0) {
                    pos--;
                } else {
                    break;
                }
            }
            Text.updated = pos;
            if (pos < 0) {
                Text.length++;
                Text.updated = 0;
            }
        }
    }

    void initPointers() {
        for (int i = 0; i < maxDistance; i++) {
            pointers[i] = i;
        }
    }

    boolean updatePointers() {
        boolean res = false;
        int last = pointers.length - 1;
        pointers[last] = ++pointers[last] % Text.baseWord.length();
        if (pointers[last] == 0) {
            int pos = last - 1;
            while (pos >= 0) {
                pointers[pos] = ++pointers[pos] % (Text.baseWord.length() - (maxDistance - pos) + 1);
                if (pointers[pos] == 0) {
                    pos--;
                } else {
                    break;
                }
            }
            if (pos < 0) {
                //all possibilities exhausted
                Text.length++;
                currentCase++;
                //System.out.println("loooong loooong maaaaaaaaaaaaaan: " + Text.length);
                initPointers();
                pos = 0;
                res = true;
            }
            for (int i = pos+1; i < pointers.length; i++) {
                pointers[i] = pointers[i - 1] + 1;
            }
        }
        return res;
    }

    boolean updateWord(){
        boolean res = updatePointers();
        int delete, replace, insert;
        switch(currentCase){
            //delete only
            case 0:
                delete = maxDistance;
                replace = 0;
                insert = 0;
                break;
            case 1:
                delete = maxDistance - 1;
                replace = 1;
                insert = 0;
                break;
            case 2:
                delete = maxDistance;
                replace = 0;
                insert = 0;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        StringBuilder sb = new StringBuilder(Text.baseWord);
        for (int i = pointers.length - 1; i >= 0; i--) {
            sb.deleteCharAt(pointers[i]);
        }
        Text.arr = sb.toString().toCharArray();
        return res;
    }

    String getResult() {
        return new String(Text.arr).substring(0, Text.length);
    }

    private void init() {
        Text.length = minLength;
        //Text.trimmedBaseWord = Text.baseWord.substring(0, Text.length);
        Text.arr = new char[maxLength];
        Text.helperArr = new int[maxLength];
        Arrays.fill(Text.arr, alphabet[0]);
        Arrays.fill(Text.helperArr, 0);
        currentAbcPos = 0;
        pointers = new int[maxDistance];
        initPointers();
    }


    static class Text {
        static int length;
        static char[] arr;
        static String trimmedBaseWord;
        static String baseWord;
        static int[] helperArr;
        static int updated;
    }


}
