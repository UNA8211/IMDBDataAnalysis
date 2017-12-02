package Modeling;

import java.util.*;

/**
 * The Actor class represents a single instance of an actor, A HashMap of Actors is used to collect genre and performance
 * data for each actor over the entire dataset, allowing for isolated analysis of individual actors as well as collective
 * data on the set as a whole.
 */
public class Actor {

    private HashMap<String, Genre> genres;
    private String primaryGenre;
    private double primaryGenreRating;
    private double otherGenreRating;

    public Actor(String genre, double rating) {
        this.genres = new HashMap<>();
        this.addExample(genre, rating);
    }

    public void addExample(String genre, double rating) {
        Genre existing = genres.get(genre);
        if (existing == null) {
            this.genres.put(genre, new Genre(genre, rating));
        } else {
            existing.add(rating);
        }
    }

    public void computeAverages() {
        this.primaryGenreRating = getPrimaryGenreRating();
        this.otherGenreRating = getNonPrimaryGenreRating();
    }

    public double getPrimaryPercentage() {
        int totalCount = 0;
        int primaryCount = 0;
        List<Genre> examples = new ArrayList<>(this.genres.values());
        for (Genre genre : examples) {
            if (Objects.equals(genre.name, this.primaryGenre)) {
                primaryCount += genre.count;
                totalCount += genre.count;
            } else {
                totalCount += genre.count;
            }
        }

        return primaryCount / (double) totalCount;
    }

    public double getPrimaryGenreRating() {
        return this.primaryGenreRating;
    }

    public double getOtherGenresRating() {
        return this.otherGenreRating;
    }

    private double getNonPrimaryGenreRating() {
        if (primaryGenre == null) {
            computePrimaryGenre();
        }
        int numExamples = 0;
        double avgRating = 0;
        List<Genre> genres = new ArrayList<>(this.genres.values());

        for (Genre genre : genres) {
            if (!genre.name.equals(this.primaryGenre)) {
                avgRating = ((avgRating * numExamples) + (genre.count * genre.rating)) / (genre.count + numExamples);
                numExamples += genre.count;
            }
        }

        return avgRating;
    }

    private void computePrimaryGenre() {
        String primaryGenre = null;
        int primaryCount = 0;

        List<Genre> genres = new ArrayList<>(this.genres.values());
        for (Genre genre : genres) {
            if (genre.count > primaryCount) {
                primaryGenre = genre.name;
                primaryCount = genre.count;
            }
        }
        this.primaryGenre = primaryGenre;
        this.primaryGenreRating = this.genres.get(primaryGenre).rating;
    }

    private class Genre {

        String name;
        double rating;
        int count;

        public Genre(String name, double rating) {
            this.name = name;
            this.add(rating);
        }

        void add(double newRating) {
            this.rating = (((this.rating * count++) + newRating) / count);
        }
    }
}
