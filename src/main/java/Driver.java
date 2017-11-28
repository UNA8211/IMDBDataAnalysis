import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Driver {

    public static void main(String[] args) {
        QueryEngine engine = new QueryEngine();
        Dataset results = engine.executeQuery(Queries.actorDiedBeforeRelease);
        results.print();
        engine.closeConnection();

//        List<JSONObject> movies = new ArrayList<>();
//        for (List<String> line : results) {
//            movies.add(JSONEngine.readJsonFromUrl(line.get(2)));
//        }
//        for (JSONObject j : movies) {
//            System.out.println(j.toString());
//        }
    }
}
