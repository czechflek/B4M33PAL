package pal;

import java.util.Arrays;

class Generator {
    private char[] alphabet;
    private int minLength;
    private int maxLength;
    private int maxDistance;
    int[] pointers;

    private int currentAbcPos;


    Generator(String alphabet, int minLength, int maxLength, int maxDistance) {
        this.alphabet = alphabet.toCharArray();
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.maxDistance = maxDistance;
        Text.updated = 0;
        init();
    }

    void goNext(){
        currentAbcPos = ++currentAbcPos % alphabet.length;
        Text.helperArr[Text.length - 1] = currentAbcPos;
        Text.arr[Text.length - 1] = alphabet[currentAbcPos];
        Text.updated = Text.length - 1;
        if(currentAbcPos == 0) {
            int pos = Text.length - 2;
            while (pos >= 0){
                Text.helperArr[pos] = ++Text.helperArr[pos] % alphabet.length;
                Text.arr[pos] = alphabet[Text.helperArr[pos]];
                if(Text.helperArr[pos] == 0){
                    pos--;
                } else {
                    break;
                }
            }
            Text.updated = pos;
            if(pos <  0){
                Text.length++;
                Text.updated = 0;
            }
        }
    }

//    void initPointers(){
//        for (int i = 0; i < maxDistance; i++) {
//            pointers[i] = i;
//        }
//    }

//    void updatePointers(){
//        pointers
//
//
//    }

    String getResult(){
        return new String(Text.arr).substring(0,Text.length);
    }

    private void init(){
        Text.length = minLength;
        Text.arr = new char[maxLength];
        Text.helperArr = new int[maxLength];
        Arrays.fill(Text.arr, alphabet[0]);
        Arrays.fill(Text.helperArr, 0);
        currentAbcPos = 0;
        pointers = new int[maxDistance];
//        initPointers();
    }


    static class Text{
        static int length;
        static char[] arr;
        static String baseWord;
        static int[] helperArr;
        static int updated;
    }


}
