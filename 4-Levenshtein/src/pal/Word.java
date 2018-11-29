package pal;

class Word {
    private char[] text;
    private int maxDistance;
    private DistanceBounds bounds;

    Word(String text, int maxDistance) {
        this.text = text.toCharArray();
        this.maxDistance = maxDistance;
        bounds = new DistanceBounds(text.length() - maxDistance, text.length() + maxDistance);
    }

    public DistanceBounds getBounds() {
        return bounds;
    }

    static class DistanceBounds {
        private int lower;
        private int upper;

        DistanceBounds(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }

        int getLower() {
            return lower;
        }

        int getUpper() {
            return upper;
        }

        static DistanceBounds intersect(DistanceBounds db1, DistanceBounds db2){
            return new DistanceBounds(Math.max(db1.getLower(), db2.getLower()), Math.min(db1.getUpper(), db2.getUpper()));
        }
    }
}
