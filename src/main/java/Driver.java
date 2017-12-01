import Modeling.Builders.*;
import Modeling.TimeSpan;
import QueryEngine.*;

import java.util.ArrayList;
import java.util.List;

import static QueryEngine.QueryType.*;

public class Driver {

    private static List<TimeSpan> decades;

    static {
        decades = new ArrayList<>();
        decades.add(new TimeSpan(1960, 1970));
        decades.add(new TimeSpan(1970, 1980));
        decades.add(new TimeSpan(1980, 1990));
        decades.add(new TimeSpan(1990, 2000));
        decades.add(new TimeSpan(2000, 2010));
        decades.add(new TimeSpan(2010, 2020));
        decades.add(new TimeSpan(1900, 2020));
    }

    public static void main(String[] args) {
        QueryEngine queryEngine = new QueryEngine();

        analyzeEffectOfCrewDeath(queryEngine);
        analyzePredictabilityOfAwards(queryEngine);
        analyzePerformanceOfPrimaryGenre(queryEngine, true);

        queryEngine.closeConnection();
    }

    private static void analyzeEffectOfCrewDeath(QueryEngine engine) {
        IModelBuilder modelBuilder = new CrewDeathModelBuilder();

        decades.forEach(decade -> {
            Dataset dead = engine.executeQuery(QueryFactory.buildQuery(ActorDeath, decade));
            Dataset alive = engine.executeQuery(QueryFactory.buildQuery(ActorNotDeath, decade));
            modelBuilder.buildModel(dead, alive, decade);
        });
    }

    private static void analyzeEffectFrequentActorPairs(QueryEngine engine) {
        IModelBuilder modelBuilder = new ActorPairModelBuilder(3);

        decades.forEach(decade -> {
            Dataset actorPairs = engine.executeQuery(QueryFactory.buildQuery(ActorPair, decade));
            Dataset individuals = engine.executeQuery(QueryFactory.buildQuery(ActorIndividual, decade));
            modelBuilder.buildModel(actorPairs, individuals, decade);
        });
    }

    private static void analyzePerformanceOfPrimaryGenre(QueryEngine engine, boolean useLocalDataset) {
        IModelBuilder modelBuilder = new PrimaryGenreModelBuilder(0.5, 0.7);
        if (useLocalDataset) {
            Dataset genres = DatasetBuilder.buildDataset("datasets/primaryGenre.tsv");
            modelBuilder.buildModel(genres, null, new TimeSpan(2000, 2010));
        } else {
            decades.forEach(decade -> {
                Dataset genres = engine.executeQuery(QueryFactory.buildQuery(PrimaryGenre, decades.get(4)));
                modelBuilder.buildModel(genres, null, decade);
            });
        }
    }

    private static void analyzePredictabilityOfAwards(QueryEngine queryEngine) {
        IModelBuilder modelBuilder = new AwardPredictionModelBuilder();
        TimeSpan timeSpan = new TimeSpan(1980, 2010);
        Dataset movies = queryEngine.executeQuery(QueryFactory.buildQuery(QueryType.Awards, timeSpan));

        System.out.println("Begin fetch");
        JSONEngine.fetchData(movies, "Awards");
        System.out.println("Fetch complete");

        modelBuilder.buildModel(movies, null, timeSpan);
    }
}
