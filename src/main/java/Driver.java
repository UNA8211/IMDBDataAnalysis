public class Driver {

    public static void main(String[] args) {
        QueryEngine engine = new QueryEngine();
        actorDiedBeforeMovieRelease(engine);
//        Dataset results = engine.executeQuery(Queries.test);
//        results.print();
        engine.closeConnection();

//        List<JSONObject> movies = new ArrayList<>();
//        for (List<String> line : results) {
//            movies.add(JSONEngine.readJsonFromUrl(line.get(2)));
//        }
//        for (JSONObject j : movies) {
//            System.out.println(j.toString());
//        }
    }

    private static void actorDiedBeforeMovieRelease(QueryEngine engine) {
        Dataset died = engine.executeQuery(Queries.actorDiedBeforeRelease);
        Dataset notDied = engine.executeQuery(Queries.actorNotDiedBeforeRelease);
        Analysis.actorDeath(died, notDied);
    }
}
