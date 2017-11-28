public class Driver {

    public static void main(String[] args) {
        QueryEngine engine = new QueryEngine();
        Dataset results = engine.executeQuery(Queries.actorPairs);
        results.print();
        engine.closeConnection();
    }
}
