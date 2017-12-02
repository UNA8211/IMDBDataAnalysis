package Modeling.Builders;

import Modeling.Actor;
import Modeling.TimeSpan;
import QueryEngine.Dataset;

import java.util.*;

public class PrimaryGenreModelBuilder extends ModelBuilderBase {

    private double minPercentage;
    private double maxPercentage;

    public PrimaryGenreModelBuilder(double minPercentage, double maxPercentage) {
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
    }

    @Override
    public void buildModel(Dataset dataset, Dataset ignored, TimeSpan timeSpan) {
        // Filter values outside of time span
        Dataset actorGenres = new Dataset(dataset);
        int yearIndex = actorGenres.get(0).size() - 1;
        actorGenres.removeIf(genre -> {
            Integer year = Integer.parseInt(genre.get(yearIndex));
            return year < timeSpan.startYear || year > timeSpan.endYear;
        });
        // Parse dataset and collect actor entities as a HashMap
        HashMap<String, Actor> actors = new HashMap<>();
        actorGenres.parallelStream().forEachOrdered(example -> {
            String name = example.get(0);
            String genre = example.get(1);
            double rating = Double.parseDouble(example.get(3));

            // If the actor already exists update, otherwise create new
            Actor existing = actors.get(name);
            if (existing == null) {
                actors.put(name, new Actor(genre, rating));
            } else {
                existing.addExample(genre, rating);
            }
        });

        // Compute average actor performance with respect to particular genres,
        // Filter the set if the actor does not meet the criteria
        List<Actor> actorList = new ArrayList<>(actors.values());
        actorList.forEach(Actor::computeAverages);
        actorList.removeIf(actor -> actor.getOtherGenresRating() < 1
                || actor.getPrimaryGenreRating() < 1
                || actor.getPrimaryPercentage() < minPercentage
                || actor.getPrimaryPercentage() > maxPercentage);

        // Todo: Push stats magic to base class
        // Compute means
        double avgPrimaryRating = 0.0;
        double avgOtherRating = 0.0;
        for (Actor actor : actorList) {
            avgPrimaryRating += actor.getPrimaryGenreRating();
            avgOtherRating += actor.getOtherGenresRating();
        }
        avgPrimaryRating /= actorList.size();
        avgOtherRating /= actorList.size();

        // Compute standard deviations
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
