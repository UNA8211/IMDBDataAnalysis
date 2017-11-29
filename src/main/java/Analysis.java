import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Analysis {

    public Analysis() {

    }

    public static void actorDeath(Dataset died, Dataset living, int startYear, int endYear) {
        // Get average rating of each dataset
        float avgDiedRating = 0.f;
        float avgLivingRating = 0.f;
        int ratingIndex = died.get(0).size() - 1;

        for (List<String> example : died) {
            avgDiedRating += Float.parseFloat(example.get(ratingIndex));
        }
        avgDiedRating /= died.size();

        for (List<String> example : living) {
            avgLivingRating += Float.parseFloat(example.get(ratingIndex));
        }
        avgLivingRating /= living.size();

        System.out.println("Year Range: (" + startYear + ", " + endYear + ")");
        System.out.println("Avg rating for dead actors: " + avgDiedRating);
        System.out.println("Avg rating for living actors: " + avgLivingRating);
        System.out.println();
    }

    public static void actorPairs(Dataset actorPairs, Dataset averageActorRatings, int startYear, int endYear) {
        System.out.println("Begin analysis: (" + startYear + ", " + endYear + ")");
        System.out.println("Actor Pairs: " + actorPairs.size());
        System.out.println("Individuals: " + averageActorRatings.size());

        // Collect pairs in hash table computing average ratings
        HashMap<Integer, ActorPair> pairs = new HashMap<>();
        actorPairs.parallelStream().forEachOrdered(actorPair -> {
            String actor1 = actorPair.get(0);
            String actor2 = actorPair.get(1);
            Float rating = Float.parseFloat(actorPair.get(2));
            ActorPair pair = new ActorPair(actor1, actor2, rating);

            ActorPair result = pairs.get(pair.hashCode());
            if (result == null) {
                pairs.put(pair.hashCode(), pair);
            } else {
                result.addPair(rating);
            }
        });

        // Remove pairs with a frequency less than 3
        List<ActorPair> frequentPairs = new ArrayList<>(pairs.values());
        frequentPairs.removeIf(element -> element.getCount() < 3);

        HashMap<String, Float> avgRatings = new HashMap<>();
        averageActorRatings.forEach(element -> avgRatings.put(element.get(0), Float.parseFloat(element.get(1))));

        frequentPairs.forEach(pair -> {
            // Get avg quality of actor 1
            Float actor1AvgRating = avgRatings.get(pair.actor1);
            Float actor2AvgRating = avgRatings.get(pair.actor2);
            if (actor1AvgRating != null && actor2AvgRating != null) {
                pair.setAvgIndividualActorQuality((actor1AvgRating + actor2AvgRating) / 2);
            }
        });

        // Remove elements without individual data, use one because ya know.. 0.0 doesn't equal 0
        frequentPairs.removeIf(actorPair -> actorPair.getAvgIndividualActorQuality() < 1);

        float combinedSum = 0.f;
        float individualSum = 0.f;
        for (ActorPair pair : frequentPairs) {
            combinedSum += pair.getCombinedActorQuality();
            individualSum += pair.getAvgIndividualActorQuality();
            System.out.println(pair.toString());
        }
        combinedSum /= frequentPairs.size();
        individualSum /= frequentPairs.size();

        System.out.println("Avg individual rating: " + individualSum);
        System.out.println("Avg combined rating: " + combinedSum);

        // Todo: Standard Dev
    }
}
