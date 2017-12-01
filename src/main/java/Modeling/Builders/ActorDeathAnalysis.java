package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;

import java.util.List;

public class ActorDeathAnalysis extends ModelBuilderBase {

    @Override
    public void buildModel(Dataset died, Dataset living, TimeSpan timeSpan) {
        System.out.println("Begin Analysis");
        // Get average rating of each dataset
        double avgDiedRating = 0.f;
        double avgLivingRating = 0.f;
        int diedRatingIndex = died.get(0).size() - 1;
        int livingRatingIndex = living.get(0).size() - 1;

        for (List<String> example : died) {
            avgDiedRating += Double.parseDouble(example.get(diedRatingIndex));
        }
        avgDiedRating /= died.size();

        for (List<String> example : living) {
            avgLivingRating += Double.parseDouble(example.get(livingRatingIndex));
        }
        avgLivingRating /= living.size();

        // Get standard deviation for each dataset
        double diedStdDev = 0.f;
        double livingStdDev = 0.f;
        for (List<String> example : died) {
            diedStdDev += Math.pow(Double.parseDouble(example.get(diedRatingIndex)) - avgDiedRating, 2);
        }
        for (List<String> example : living) {
            livingStdDev += Math.pow(Double.parseDouble(example.get(livingRatingIndex)) - avgLivingRating, 2);
        }
        diedStdDev = Math.sqrt(diedStdDev / died.size());
        livingStdDev = Math.sqrt(livingStdDev / living.size());

        System.out.println("Year Range: (" + timeSpan.startYear + ", " + timeSpan.endYear + ")");
        System.out.println("Rating for dead actors: " + avgDiedRating + " (" + diedStdDev + ")");
        System.out.println("Rating for living actors: " + avgLivingRating + " (" + livingStdDev + ")");
        System.out.println();
    }
}
