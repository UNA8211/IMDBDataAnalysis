public class Analysis {

    public Analysis() {

    }

    private static void findCommonPairs(Dataset actorPairs) {
        Dataset uniquePairs = new Dataset(actorPairs);
        for (int i = 0; i < actorPairs.size(); i++) {
            String nConst1 = actorPairs.get(i).get(1);
            String nConst2 = actorPairs.get(i).get(3);
            for (int j = 0; j < actorPairs.size(); j++) {
                if (true) {

                }
            }
        }
    }
}
