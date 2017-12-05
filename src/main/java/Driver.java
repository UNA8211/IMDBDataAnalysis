import Modeling.Builders.*;
import Modeling.TimeSpan;
import QueryEngine.*;
import Utilities.Utils;

import java.util.ArrayList;
import java.util.List;

import static QueryEngine.QueryType.*;

/**
 * Driver serves and program entry point and allows for selection of which types of queries and analysis
 * should be preformed. Datasets for modeling and analysis may be retried from an SQL database using the QueryEngine
 * or from local .tsv files using the DatasetBuilder class.
 */
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

    /**
     * Run all analyses
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        QueryEngine queryEngine = new QueryEngine();

        analyzeBestMonths(queryEngine, false);
        analyzeEffectFrequentActorPairs(queryEngine);
        analyzeEffectOfCrewDeath(queryEngine);
        analyzePerformanceOfPrimaryGenre(queryEngine, false);
        analyzePredictabilityOfAwards(queryEngine);

        queryEngine.closeConnection();
    }

    private static void analyzeEffectOfCrewDeath(QueryEngine engine) {
        IModelBuilder modelBuilder = new CrewDeathModelBuilder();

        System.out.println("BEGINNING ANALYSIS OF CREW MEMBER DEATH");
        decades.forEach(decade -> {
            Dataset dead = engine.executeQuery(QueryFactory.buildQuery(ActorDeath, decade));
            Dataset alive = engine.executeQuery(QueryFactory.buildQuery(ActorNotDeath, decade));
            modelBuilder.buildModel(dead, alive, decade);
        });
        System.out.println("COMPLETED ANALYSIS OF CREW MEMBER DEATH\n\n\n");
    }

    private static void analyzeEffectFrequentActorPairs(QueryEngine engine) {
        IModelBuilder modelBuilder = new ActorPairModelBuilder(3);
        System.out.println("BEGINNING ANALYSIS OF FREQUENT ACTOR PAIRS");

        decades.forEach(decade -> {
            Dataset actorPairs = engine.executeQuery(QueryFactory.buildQuery(ActorPair, decade));
            Dataset individuals = engine.executeQuery(QueryFactory.buildQuery(ActorIndividual, decade));
            modelBuilder.buildModel(actorPairs, individuals, decade);
        });

        System.out.println("COMPLETED ANALYSIS OF FREQUENT ACTOR PAIRS\n\n\n");
    }

    private static void analyzePerformanceOfPrimaryGenre(QueryEngine engine, boolean useLocalDataset) {
        IModelBuilder modelBuilder = new PrimaryGenreModelBuilder(0.8, 0.99);

        System.out.println("BEGINNING ANALYSIS OF ACTOR PRIMARY GENRE");

        if (useLocalDataset) {
            Dataset genres = DatasetBuilder.buildDataset("datasets/primaryGenre.tsv");
            decades.forEach(decade -> modelBuilder.buildModel(genres, null, decade));
        } else {
            decades.forEach(decade -> {
                Dataset genres = engine.executeQuery(QueryFactory.buildQuery(PrimaryGenre, decade));
                modelBuilder.buildModel(genres, null, decade);
            });
        }

        System.out.println("COMPLETED ANALYSIS OF ACTOR PRIMARY GENRE\n\n\n");
    }

    private static void analyzePredictabilityOfAwards(QueryEngine queryEngine) {
        IModelBuilder modelBuilder = new AwardPredictionModelBuilder();

        System.out.println("BEGINNING ANALYSIS OF PREDICTABILITY OF AWARDS");

        TimeSpan timeSpan = new TimeSpan(1980, 2010);
        Dataset movies = queryEngine.executeQuery(QueryFactory.buildQuery(QueryType.Awards, timeSpan));

        JSONEngine.fetchData(movies, "Awards");

        modelBuilder.buildModel(movies, null, timeSpan);

        System.out.println("COMPLETED ANALYSIS OF PREDICTABILITY OF AWARDS\n\n\n");
    }

    private static void analyzeBestMonths(QueryEngine queryEngine, boolean useLocalDataset) {
        IModelBuilder modelBuilder = new MonthToMonthRatingsModelBuilder();

        System.out.println("BEGINNING ANALYSIS OF BEST PERFORMING MONTHS");

        Dataset movies;
        if (useLocalDataset) {
            movies = DatasetBuilder.buildDataset("src/main/java/Data/months.tsv");
        } else {
            movies = new Dataset();
            for (int i = 2007; i < 2016; i++) {
                Dataset year = queryEngine.executeQuery(QueryFactory.buildQuery(QueryType.MovieMonths, new TimeSpan(i, i)));
                year.shuffle();
                movies.addAll(year.subList(0, 100));
            }

            JSONEngine.fetchData(movies, "BoxOffice", "Released");
        }

        // Prune out movies without box office
        Utils.pruneAttribute(movies, 3);

        //Utils.setFileOut("src/main/java/Data/bestMonths2");

        modelBuilder.buildModel(movies, null, null);

        System.out.println("COMPLETED ANALYSIS OF BEST PERFORMING MONTHS\n\n\n");
    }
}
