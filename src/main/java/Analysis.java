import java.util.ArrayList;
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

    public static void actorPairs(Dataset actorPairs, int startYear, int endYear) {
        System.out.println("Begin analysis: (" + startYear + ", " + endYear + ")");
        List<ActorPair> pairs = new ArrayList<>();
        for (List<String> actorPair : actorPairs) {
            ActorPair pair = new ActorPair(actorPair.get(0), actorPair.get(1));
            int index = pairs.indexOf(pair);
            if (index != -1) {
                pairs.get(index).addPair(Float.parseFloat(actorPair.get(2)));
            }
        }

        pairs.forEach(pair -> System.out.println(pair.toString()));
    }
}
