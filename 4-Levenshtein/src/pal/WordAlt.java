package pal;

class WordAlt extends Word{
    WordAlt(String text, int maxDistance) {
        super(text, maxDistance);
    }

    boolean isNearTarget() {
        int adjustedTargetLength = Generator.Text.arr.length + 1;

        for (int i = 1; i < adjustedTargetLength; i++) {
            for (int j = 1; j < adjustedLength; j++) {
                updateCost(i, j);
            }
        }
        ///////////////////////////////////////////////////////
//        //if (cost[adjustedTargetLength - 1][adjustedLength - 1] <= maxDistance) {
//            System.out.print("   ");
//            for (int j = 0; j < text.length; j++) {
//                System.out.print("|" + text[j]);
//            }
//            System.out.println();
//            for (int i = 0; i < adjustedTargetLength; i++) {
//                if (i > 0)
//                    System.out.print(Generator.Text.arr[i - 1]);
//                else
//                    System.out.print(" ");
//                for (int j = 0; j < adjustedLength; j++) {
//                    System.out.print("|" + cost[i][j]);
//                }
//                System.out.println();
//            }
//            System.out.println(new String(Generator.Text.arr).substring(0, Generator.Text.length) + ": " + cost[adjustedTargetLength - 1][adjustedLength - 1]);
//      //  }
//        //////////////////////////////////////////////////////////
        return cost[adjustedTargetLength - 1][adjustedLength - 1] <= maxDistance;
    }
}
