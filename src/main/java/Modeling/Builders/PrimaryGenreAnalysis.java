package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;

import java.util.HashMap;

public class PrimaryGenreAnalysis extends ModelBuilderBase {

    public PrimaryGenreAnalysis() {

    }

    @Override
    public void buildModel(Dataset actorGenres, Dataset ignored, TimeSpan timeSpan) {
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

        actors.forEach((String actorName, Actor actor) -> {
            double primaryGenreRating = actor.getPrimaryGenreRating();
            double otherGenreRating = actor.getNonPrimaryGenreRating();
            if (primaryGenreRating > 0 && otherGenreRating > 0) {
                System.out.println(actorName + ", Primary rating: " + primaryGenreRating + ", Other rating: " + otherGenreRating);
            }
        });
    }
}
