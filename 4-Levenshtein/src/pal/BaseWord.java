package pal;

abstract class BaseWord {
    char[] text;
    int adjustedLength;
    int maxDistance;
    private DistanceBounds bounds;
    int[][] cost;

    BaseWord(String text, int maxDistance) {
        this.text = text.toCharArray();
        this.maxDistance = maxDistance;
        this.adjustedLength = text.length() + 1;
        bounds = new DistanceBounds(text.length() - maxDistance, text.length() + maxDistance);
    }

    void initCostStore(int maxLen) {
        cost = new int[maxLen][adjustedLength];
        for (int i = 0; i < adjustedLength; i++) cost[0][i] = i;
        for (int i = 0; i < maxLen; i++) cost[i][0] = i;
    }

    void updateCost(int i, int j) {
        int match = (text[j - 1] == Generator.Text.arr[i - 1]) ? 0 : 1;

        int costSub = cost[i - 1][j - 1] + match;
        int costIns = cost[i - 1][j] + 1;
        int costDel = cost[i][j - 1] + 1;

        cost[i][j] = Math.min(Math.min(costIns, costSub), costDel);
    }

    DistanceBounds getBounds() {
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

        static DistanceBounds intersect(DistanceBounds db1, DistanceBounds db2) {
            return new DistanceBounds(Math.max(db1.getLower(), db2.getLower()), Math.min(db1.getUpper(), db2.getUpper()));
        }
    }
}
