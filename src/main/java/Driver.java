import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    public static void main(String[] args) {
        QueryEngine engine = new QueryEngine();
        Dataset results = engine.executeQuery(Queries.test);
        results.print();
        engine.closeConnection();

        List<JSONObject> movies = new ArrayList<>();
        for (List<String> line : results) {
            movies.add(JSONEngine.readJsonFromUrl(line.get(2)));
        }
        for (JSONObject j : movies) {
            System.out.println(j.toString());
        }
    }
}
