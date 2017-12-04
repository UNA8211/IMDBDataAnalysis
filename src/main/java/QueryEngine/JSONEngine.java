package QueryEngine;

import Utilities.Utils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * JSONEngine provides an means of communicating with the OMDBAPI to fetch specific movie data. Data fetching is handled
 * asynchronously and returns after all api requests have been satisfied or responded in error. The requested data attributes
 * are then stripped from the returned JSON objects and appended to the provided dataset
 */
public class JSONEngine {

    private static final String url = "http://www.omdbapi.com/?apikey=9ac195bd&i=";

    public static void fetchData(Dataset movies, String... attributes) {
        Long startTime = System.currentTimeMillis();
        System.out.print("Begin omdbapi data fetch... ");
        try {
            CompletableFuture.allOf(movies.parallelStream()
                    .map(movie -> CompletableFuture.supplyAsync(() ->
                            movie.addAll(Utils.parseJsonAttr(readJsonFromUrl(movie.get(0)), attributes))))
                    .toArray(CompletableFuture[]::new))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("complete, runtime: " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
    }

    private static JSONObject readJsonFromUrl(String tConst) {
        try {
            return new JSONObject(IOUtils.toString(new URL(url + tConst), Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
