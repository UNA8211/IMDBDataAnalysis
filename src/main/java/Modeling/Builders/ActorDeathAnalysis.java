package Modeling.Builders;

import QueryEngine.Dataset;

import java.util.List;

public class ActorDeathAnalysis extends ModelBuilderBase {

    @Override
    public void buildModel(Dataset died, Dataset living, Integer startYear, Integer endYear) {
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
}
