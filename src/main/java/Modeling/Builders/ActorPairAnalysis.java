package Modeling.Builders;

import Modeling.ActorPair;
import Modeling.TimeSpan;
import QueryEngine.Dataset;

import java.util.*;

public class ActorPairAnalysis extends ModelBuilderBase {

    public ActorPairAnalysis() {

    }

    @Override
    public void buildModel(Dataset actorPairs, Dataset averageActorRatings, TimeSpan timeSpan) {
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

        System.out.println(timeSpan.startYear != 0 ? "TimeSpan: " + timeSpan.startYear + "s" : "All time");
        computeStats(frequentPairs);
    }
}
