package Modeling.Builders;

import Modeling.ActorPair;
import Modeling.TimeSpan;
import QueryEngine.Dataset;

import java.util.*;

public class ActorPairModelBuilder extends ModelBuilderBase {

    private int minFrequency;

    public ActorPairModelBuilder(int minFrequency) {
        this.minFrequency = minFrequency;
    }

    @Override
    public void buildModel(Dataset actorPairs, Dataset averageActorRatings, TimeSpan timeSpan) {
        // Parsing the SQL dataset, collect actor pairs as a HashMap
        HashMap<Integer, ActorPair> pairs = new HashMap<>();
        actorPairs.parallelStream().forEachOrdered(actorPair -> {
            String actor1 = actorPair.get(0);
            String actor2 = actorPair.get(1);
            Float rating = Float.parseFloat(actorPair.get(2));
            ActorPair pair = new ActorPair(actor1, actor2, rating);

            // Add a new entry if the pair doesn't exist, else update the existing pair with the occurrence's rating
            ActorPair result = pairs.get(pair.hashCode());
            if (result == null) {
                pairs.put(pair.hashCode(), pair);
            } else {
                result.addPair(rating);
            }
        });

        // Remove pairs with a frequency less than minFrequency
        List<ActorPair> frequentPairs = new ArrayList<>(pairs.values());
        frequentPairs.removeIf(element -> element.getCount() < minFrequency);

        // Create a HashMap to look up the average rating of a given actor
        HashMap<String, Float> avgRatings = new HashMap<>();
        averageActorRatings.forEach(element -> avgRatings.put(element.get(0), Float.parseFloat(element.get(1))));

        // Set the pair and individual ratings of each actor / actor pair
        frequentPairs.parallelStream().forEach(pair -> {
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
