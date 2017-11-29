import Modeling.Builders.ActorDeathAnalysis;
import Modeling.Builders.ActorPairAnalysis;
import Modeling.Builders.IModelBuilder;
import Modeling.Builders.PrimaryGenreAnalysis;
import QueryEngine.Dataset;
import QueryEngine.Queries;
import QueryEngine.QueryEngine;

public class Driver {

    public static void main(String[] args) {
        QueryEngine engine = new QueryEngine();
        performanceOfActorPairs(engine);
        engine.closeConnection();

//        Dataset results = engine.executeQuery(Queries.test);
//        results.print();
//        List<JSONObject> movies = new ArrayList<>();
//        for (List<String> line : results) {
//            movies.add(JSONEngine.readJsonFromUrl(line.get(2)));
//        }
//        for (JSONObject j : movies) {
//            System.out.println(j.toString());
//        }
    }

    private static void actorDiedBeforeMovieRelease(QueryEngine engine) {
        IModelBuilder modelBuilder = new ActorDeathAnalysis();

        // 1960s
        Dataset sixtiesDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(1960, 1970));
        Dataset sixtiesAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(1960, 1970));
        modelBuilder.buildModel(sixtiesDead, sixtiesAlive, 1960, 1970);

        // 1970s
        Dataset seventiesDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(1970, 1980));
        Dataset seventiesAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(1970, 1980));
        modelBuilder.buildModel(seventiesDead, seventiesAlive, 1970, 1980);

        // 1980s
        Dataset eightiesDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(1980, 1990));
        Dataset eightiesAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(1980, 1990));
        modelBuilder.buildModel(eightiesDead, eightiesAlive, 1980, 1990);

        // 1990s
        Dataset ninetiesDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(1990, 2000));
        Dataset ninetiesAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(1990, 2000));
        modelBuilder.buildModel(ninetiesDead, ninetiesAlive, 1990, 2000);

        // 2000s
        Dataset zerosDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(2000, 2010));
        Dataset zerosAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(2000, 2010));
        modelBuilder.buildModel(zerosDead, zerosAlive, 2000, 2010);

        // 2010s
        Dataset tensDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(2010, 2020));
        Dataset tensAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(2010, 2020));
        modelBuilder.buildModel(tensDead, tensAlive, 2010, 2020);

        // All time
        Dataset allTimeDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(0, 2020));
        Dataset allTimeAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(0, 2020));
        modelBuilder.buildModel(allTimeDead, allTimeAlive, 0, 2020);
    }

    private static void performanceOfActorPairs(QueryEngine engine) {
        IModelBuilder modelBuilder = new ActorPairAnalysis();

        // 1960s
        Dataset sixtiesActorPairs = engine.executeQuery(Queries.getActorPairsQuery(1960, 1970));
        Dataset sixtiesIndividualActors = engine.executeQuery(Queries.getActorAverageRatingQuery(1960, 1970));
        modelBuilder.buildModel(sixtiesActorPairs, sixtiesIndividualActors, 1960, 1970);

        // 1970s
        Dataset seventiesActorPairs = engine.executeQuery(Queries.getActorPairsQuery(1970, 1980));
        Dataset seventiesIndividualActors = engine.executeQuery(Queries.getActorAverageRatingQuery(1970, 1980));
        modelBuilder.buildModel(seventiesActorPairs, seventiesIndividualActors, 1970, 1980);

        // 1980s
        Dataset eightiesActorPairs = engine.executeQuery(Queries.getActorPairsQuery(1980, 1990));
        Dataset eightiesIndividualActors = engine.executeQuery(Queries.getActorAverageRatingQuery(1980, 1990));
        modelBuilder.buildModel(eightiesActorPairs, eightiesIndividualActors, 1980, 1990);

        // 1990s
        Dataset ninetiesActorPairs = engine.executeQuery(Queries.getActorPairsQuery(1990, 2000));
        Dataset ninetiesIndividualActors = engine.executeQuery(Queries.getActorAverageRatingQuery(1990, 2000));
        modelBuilder.buildModel(ninetiesActorPairs, ninetiesIndividualActors, 1990, 2000);

        // 2000s
        Dataset zerosActorPairs = engine.executeQuery(Queries.getActorPairsQuery(2000, 2010));
        Dataset zerosIndividualActors = engine.executeQuery(Queries.getActorAverageRatingQuery(2000, 2010));
        modelBuilder.buildModel(zerosActorPairs, zerosIndividualActors, 2000, 2010);

        // 2010s
        Dataset tensActorPairs = engine.executeQuery(Queries.getActorPairsQuery(2010, 2020));
        Dataset tensIndividualActors = engine.executeQuery(Queries.getActorAverageRatingQuery(2010, 2020));
        modelBuilder.buildModel(tensActorPairs, tensIndividualActors, 2010, 2020);

        // All time
        Dataset allTimeActorPairs = engine.executeQuery(Queries.getActorPairsQuery(0, 2020));
        Dataset allTimeIndividualActors = engine.executeQuery(Queries.getActorAverageRatingQuery(0, 2020));
        modelBuilder.buildModel(allTimeActorPairs, allTimeIndividualActors, 0, 2020);
    }

    private static void analysisActorPerformanceForPrimaryGenre(QueryEngine engine) {
        IModelBuilder modelBuilder = new PrimaryGenreAnalysis();

        Dataset genres = engine.executeQuery(Queries.getActorsGenres(1980, 2020));
        modelBuilder.buildModel(genres, null, 1980, 2020);
    }
}
