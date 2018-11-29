package pal;

public class Generator {
    private char[] alphabet;
    private int minLength;
    private int maxLength;

    public Generator(String alphabet, int minLength, int maxLength) {
        this.alphabet = alphabet.toCharArray();
        this.minLength = minLength;
        this.maxLength = maxLength;

    }

}
