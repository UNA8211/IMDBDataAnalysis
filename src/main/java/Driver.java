import Modeling.Builders.*;
import Modeling.TimeSpan;
import QueryEngine.*;

import static QueryEngine.QueryType.*;

public class Driver {

    private static TimeSpan sixties, seventies, eighties, nineties, zeroes, tens, allTime;

    static {
        sixties = new TimeSpan(1960, 1970);
        seventies = new TimeSpan(1970, 1980);
        eighties = new TimeSpan(1980, 1990);
        nineties = new TimeSpan(1990, 2000);
        zeroes = new TimeSpan(2000, 2010);
        tens = new TimeSpan(2010, 2020);
        allTime = new TimeSpan(1900, 2020);
    }

    public static void main(String[] args) {
        QueryEngine queryEngine = new QueryEngine();
        JSONEngine jsonEngine = new JSONEngine();
        //analyzeActorDiedBeforeMovieRelease(queryEngine);
        awardsPrediction(queryEngine, jsonEngine);
        //analysisActorPerformanceForPrimaryGenre(queryEngine);
        queryEngine.closeConnection();
    }

    private static void analyzeActorDiedBeforeMovieRelease(QueryEngine engine) {
        IModelBuilder modelBuilder = new ActorDeathAnalysis();

        // 1960s
        Dataset sixtiesDead = engine.executeQuery(QueryFactory.buildQuery(ActorDeath, sixties));
        Dataset sixtiesAlive = engine.executeQuery(QueryFactory.buildQuery(ActorNotDeath, sixties));
        modelBuilder.buildModel(sixtiesDead, sixtiesAlive, sixties);

        // 1970s
        Dataset seventiesDead = engine.executeQuery(QueryFactory.buildQuery(ActorDeath, seventies));
        Dataset seventiesAlive = engine.executeQuery(QueryFactory.buildQuery(ActorNotDeath, seventies));
        modelBuilder.buildModel(seventiesDead, seventiesAlive, seventies);

        // 1980s
        Dataset eightiesDead = engine.executeQuery(QueryFactory.buildQuery(ActorDeath, eighties));
        Dataset eightiesAlive = engine.executeQuery(QueryFactory.buildQuery(ActorNotDeath, eighties));
        modelBuilder.buildModel(eightiesDead, eightiesAlive, eighties);

        // 1990s
        Dataset ninetiesDead = engine.executeQuery(QueryFactory.buildQuery(ActorDeath, nineties));
        Dataset ninetiesAlive = engine.executeQuery(QueryFactory.buildQuery(ActorNotDeath, nineties));
        modelBuilder.buildModel(ninetiesDead, ninetiesAlive, nineties);

        // 2000s
        Dataset zerosDead = engine.executeQuery(QueryFactory.buildQuery(ActorDeath, zeroes));
        Dataset zerosAlive = engine.executeQuery(QueryFactory.buildQuery(ActorNotDeath, zeroes));
        modelBuilder.buildModel(zerosDead, zerosAlive, zeroes);

        // 2010s
        Dataset tensDead = engine.executeQuery(QueryFactory.buildQuery(ActorDeath, tens));
        Dataset tensAlive = engine.executeQuery(QueryFactory.buildQuery(ActorNotDeath, tens));
        modelBuilder.buildModel(tensDead, tensAlive, tens);

        // All time
        Dataset allTimeDead = engine.executeQuery(QueryFactory.buildQuery(ActorDeath, allTime));
        Dataset allTimeAlive = engine.executeQuery(QueryFactory.buildQuery(ActorNotDeath, allTime));
        modelBuilder.buildModel(allTimeDead, allTimeAlive, allTime);
    }

    private static void analyzePerformanceOfFrequentActorPairs(QueryEngine engine) {
        IModelBuilder modelBuilder = new ActorPairAnalysis(3);

        // 1960s
        Dataset sixtiesActorPairs = engine.executeQuery(QueryFactory.buildQuery(ActorPair, sixties));
        Dataset sixtiesIndividualActors = engine.executeQuery(QueryFactory.buildQuery(ActorIndividual, sixties));
        modelBuilder.buildModel(sixtiesActorPairs, sixtiesIndividualActors, sixties);

        // 1970s
        Dataset seventiesActorPairs = engine.executeQuery(QueryFactory.buildQuery(ActorPair, seventies));
        Dataset seventiesIndividualActors = engine.executeQuery(QueryFactory.buildQuery(ActorIndividual, seventies));
        modelBuilder.buildModel(seventiesActorPairs, seventiesIndividualActors, seventies);

        // 1980s
        Dataset eightiesActorPairs = engine.executeQuery(QueryFactory.buildQuery(ActorPair, eighties));
        Dataset eightiesIndividualActors = engine.executeQuery(QueryFactory.buildQuery(ActorIndividual, eighties));
        modelBuilder.buildModel(eightiesActorPairs, eightiesIndividualActors, eighties);

        // 1990s
        Dataset ninetiesActorPairs = engine.executeQuery(QueryFactory.buildQuery(ActorPair, nineties));
        Dataset ninetiesIndividualActors = engine.executeQuery(QueryFactory.buildQuery(ActorIndividual, nineties));
        modelBuilder.buildModel(ninetiesActorPairs, ninetiesIndividualActors, nineties);

        // 2000s
        Dataset zerosActorPairs = engine.executeQuery(QueryFactory.buildQuery(ActorPair, zeroes));
        Dataset zerosIndividualActors = engine.executeQuery(QueryFactory.buildQuery(ActorIndividual, zeroes));
        modelBuilder.buildModel(zerosActorPairs, zerosIndividualActors, zeroes);

        // 2010s
        Dataset tensActorPairs = engine.executeQuery(QueryFactory.buildQuery(ActorPair, tens));
        Dataset tensIndividualActors = engine.executeQuery(QueryFactory.buildQuery(ActorIndividual, tens));
        modelBuilder.buildModel(tensActorPairs, tensIndividualActors, tens);

        // All time
        Dataset allTimeActorPairs = engine.executeQuery(QueryFactory.buildQuery(ActorPair, allTime));
        Dataset allTimeIndividualActors = engine.executeQuery(QueryFactory.buildQuery(ActorIndividual, allTime));
        modelBuilder.buildModel(allTimeActorPairs, allTimeIndividualActors, allTime);
    }

    private static void analysisActorPerformanceForPrimaryGenre(QueryEngine engine) {
        IModelBuilder modelBuilder = new PrimaryGenreAnalysis(0.5, 0.7);
        Dataset genres = DatasetBuilder.buildDataset("datasets/primaryGenre.tsv");//engine.executeQuery(QueryFactory.buildQuery(PrimaryGenre, zeroes));
        modelBuilder.buildModel(genres, null, zeroes);
    }

    private static void awardsPrediction(QueryEngine queryEngine, JSONEngine jsonEngine) {
        IModelBuilder modelBuilder = new AwardPredictionAnalysis();
        Dataset movies = queryEngine.executeQuery(QueryFactory.buildQuery(QueryType.Awards, zeroes));

        System.out.println("Begin fetch");
        JSONEngine.fetchData(jsonEngine, movies, "Awards");
        System.out.println("Fetch complete");

        Dataset training = new Dataset(movies.subList(0, (movies.size() * 3) / 4));
        Dataset classifying = new Dataset(movies.subList((movies.size() * 3) / 4, movies.size()));
        modelBuilder.buildModel(training, classifying, zeroes);
    }
}
