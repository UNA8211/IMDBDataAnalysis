package Modeling.Builders;

import java.util.*;

public class Actor {

    private String nConst;
    private HashMap<String, Genre> genres;

    private String primaryGenre;

    public Actor(String nConst, String genre, double rating) {
        this.nConst = nConst;
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

    public double getNonPrimaryGenreRating() {
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

    public double getPrimaryGenreRating() {
        if (primaryGenre == null) {
            computePrimaryGenre();
        }
        return this.genres.get(primaryGenre).rating;
    }

    public String getNConst() {
        return this.nConst;
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
