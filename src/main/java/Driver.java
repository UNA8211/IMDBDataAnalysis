public class Driver {

    public static void main(String[] args) {
        QueryEngine engine = new QueryEngine();
        JSONEngine jsonEngine = new JSONEngine();
        //performanceOfActorPairs(engine);
        String awards = jsonEngine.readJsonFromUrl("tt0848228").getString("Awards");
        System.out.println(awards);
        System.out.println(Utils.pullAwardData(awards).toString());
        engine.closeConnection();
    }

    private static void actorDiedBeforeMovieRelease(QueryEngine engine) {
        // 1960s
        Dataset sixtiesDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(1960, 1970));
        Dataset sixtiesAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(1960, 1970));
        Analysis.actorDeath(sixtiesDead, sixtiesAlive, 1960, 1970);

        // 1970s
        Dataset seventiesDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(1970, 1980));
        Dataset seventiesAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(1970, 1980));
        Analysis.actorDeath(seventiesDead, seventiesAlive, 1970, 1980);

        // 1980s
        Dataset eightiesDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(1980, 1990));
        Dataset eightiesAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(1980, 1990));
        Analysis.actorDeath(eightiesDead, eightiesAlive, 1980, 1990);

        // 1990s
        Dataset ninetiesDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(1990, 2000));
        Dataset ninetiesAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(1990, 2000));
        Analysis.actorDeath(ninetiesDead, ninetiesAlive, 1990, 2000);

        // 2000s
        Dataset zerosDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(2000, 2010));
        Dataset zerosAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(2000, 2010));
        Analysis.actorDeath(zerosDead, zerosAlive, 2000, 2010);

        // 2010s
        Dataset tensDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(2010, 2020));
        Dataset tensAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(2010, 2020));
        Analysis.actorDeath(tensDead, tensAlive, 2010, 2020);

        // All time
        Dataset allTimeDead = engine.executeQuery(Queries.getActorDiedBeforeReleaseQuery(0, 2020));
        Dataset allTimeAlive = engine.executeQuery(Queries.getActorNotDiedBeforeReleaseQuery(0, 2020));
        Analysis.actorDeath(allTimeDead, allTimeAlive, 0, 2020);
    }

    private static void performanceOfActorPairs(QueryEngine engine) {
        Dataset actorPairs = engine.executeQuery(Queries.getActorPairsQuery(2000, 2020));
        Analysis.actorPairs(actorPairs, 2000, 2020);
    }

    private static void awardsPrediction(QueryEngine engine) {

    }
}
