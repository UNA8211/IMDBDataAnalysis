package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;

import java.util.*;

public class PrimaryGenreAnalysis extends ModelBuilderBase {

    private double minPercentage;
    private double maxPercentage;

    public PrimaryGenreAnalysis(double minPercentage, double maxPercentage) {
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
    }

    @Override
    public void buildModel(Dataset actorGenres, Dataset ignored, TimeSpan timeSpan) {
        System.out.println("Begin analysis");
        HashMap<String, Actor> actors = new HashMap<>();
        actorGenres.parallelStream().forEachOrdered(example -> {
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
        actorList.removeIf(actor -> actor.getOtherGenresRating() < 1
                || actor.getPrimaryGenreRating() < 1
                || actor.getPrimaryPercentage() < minPercentage
                || actor.getPrimaryPercentage() > maxPercentage);

        double avgPrimaryRating = 0.0;
        double avgOtherRating = 0.0;
        for (Actor actor : actorList) {
            avgPrimaryRating += actor.getPrimaryGenreRating();
            avgOtherRating += actor.getOtherGenresRating();
        }
        avgPrimaryRating /= actorList.size();
        avgOtherRating /= actorList.size();

        double primaryRatingStdDev = 0.0;
        double otherRatingStdDev = 0.0;
        for (Actor actor : actorList) {
            primaryRatingStdDev += Math.pow(actor.getPrimaryGenreRating() - avgPrimaryRating, 2);
            otherRatingStdDev += Math.pow(actor.getOtherGenresRating() - avgOtherRating, 2);
        }

        primaryRatingStdDev = Math.sqrt(primaryRatingStdDev / actorList.size());
        otherRatingStdDev = Math.sqrt(otherRatingStdDev / actorList.size());

        System.out.println("Time Span: " + timeSpan.startYear + "s");
        System.out.println("Primary Genre: " + avgPrimaryRating + " (" + primaryRatingStdDev + ")");
        System.out.println("Other Genres: " + avgOtherRating + " (" + otherRatingStdDev + ")");
    }
}
