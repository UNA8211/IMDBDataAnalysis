import java.sql.ResultSet;

public class main {
    public static void main(String[] args) {
        QueryEngine engine = new QueryEngine();
        ResultSet test = engine.executeQuery("SELECT p.primaryName, p.deathYear, m.primaryTitle, m.startYear FROM Person p,\n" +
                "Acts_In pr, Production m\n" +
                "where p.deathYear IS NOT NULL\n" +
                "AND p.deathYear > 2000\n" +
                "AND p.nconst = pr.nconst\n" +
                "AND pr.tconst = m.tconst\n" +
                "AND m.startYear >= p.deathYear\n" +
                "AND m.titleType = 'movie'\n" +
                "ORDER BY primaryName ASC");

        DataSet set = new DataSet(test);
        set.print();
        engine.closeConnection();
    }
}
