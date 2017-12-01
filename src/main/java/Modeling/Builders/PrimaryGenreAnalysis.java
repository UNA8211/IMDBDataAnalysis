package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;

import java.util.*;

public class PrimaryGenreAnalysis extends ModelBuilderBase {

    public PrimaryGenreAnalysis() {

    }

    @Override
    public void buildModel(Dataset actorGenres, Dataset ignored, TimeSpan timeSpan) {
        System.out.println("Begin analysis");
        HashMap<String, Actor> actors = new HashMap<>();
        actorGenres.forEach(example -> {
            String nConst = example.get(0);
            String genre = example.get(1);
            double rating = Double.parseDouble(example.get(2));

            Actor existing = actors.get(nConst);
            if (existing == null) {
                actors.put(nConst, new Actor(nConst, genre, rating));
            } else {
                existing.addExample(genre, rating);
            }
        });

        List<Actor> actorList = new ArrayList<>(actors.values());
        actorList.forEach(Actor::computeAverages);
        actorList.removeIf(actor -> actor.getOtherGenresRating() < 1 || actor.getPrimaryGenreRating() < 1);

        double avgPrimaryRating = 0.0;
        double avgOtherRating = 0.0;
        for (Actor actor : actorList) {
            avgPrimaryRating += actor.getPrimaryGenreRating();
            avgOtherRating += actor.getOtherGenresRating();
        }

        double primaryRatingStdDev = 0.0;
        double otherRatingStdDev = 0.0;
        for (Actor actor : actorList) {
            primaryRatingStdDev += Math.pow(actor.getPrimaryGenreRating() - avgPrimaryRating, 2);
            otherRatingStdDev += Math.pow(actor.getOtherGenresRating() - avgOtherRating, 2);
        }

        System.out.println("Time Span: " + timeSpan.startYear + "s");
        System.out.println("Primary Genre: " + avgPrimaryRating + " (" + primaryRatingStdDev + ")");
        System.out.println("Other Genres: " + avgOtherRating + " (" + otherRatingStdDev + ")");
    }
}
