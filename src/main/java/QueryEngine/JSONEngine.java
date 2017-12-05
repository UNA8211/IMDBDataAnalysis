package QueryEngine;

import Utilities.Utils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * JSONEngine provides an means of communicating with the OMDBAPI to fetch specific movie data. Data fetching is handled
 * asynchronously and returns after all api requests have been satisfied or responded in error. The requested data attributes
 * are then stripped from the returned JSON objects and appended to the provided dataset
 */
public class JSONEngine {

    private static final String url = "http://www.omdbapi.com/?apikey=9ac195bd&i=";
    private static int timeouts = 0;

    /**
     * Uses the tConsts from a given dataset to query the OMDb database. Extracts fields via the given set of attributes.
     * @param movies dataset for querying
     * @param attributes attributes to extract from JSON object
     */
    public static void fetchData(Dataset movies, String... attributes) {
        Long startTime = System.currentTimeMillis();
        System.out.println("Begin omdbapi data fetch... ");
        int index = 0;
        while (index < movies.size()) {
            List<CompletableFuture<Boolean>> futures = new ArrayList<>();
            // Break requests into chunks to avoid possible DDoS protections
            for (int cutoff = index + 50; index < cutoff; index++) {
                if (index >= movies.size()) {
                    break;
                }
                final int finalIndex = index;
                // Asynchronous requests
                futures.add(CompletableFuture.supplyAsync(() -> movies.get(finalIndex).addAll(Utils.parseJsonAttr(readJsonFromUrl(movies.get(finalIndex).get(0)), attributes))));
            }

            try {
                // Wait until all requests have received responses
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                timeouts++;
            }
            System.out.println((index / (double) movies.size()) * 100 + " %");
            Utils.sleep(1 + (int) Math.pow(timeouts, 2));
            timeouts = 0;
        }

        System.out.println("complete, runtime: " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
    }

    /**
     * Queries the OMDb database for the given tConst
     * @param tConst to query
     * @return the JSON object for the given tConst
     */
    private static JSONObject readJsonFromUrl(String tConst) {
        try {
            return new JSONObject(IOUtils.toString(new URL(url + tConst), Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
            timeouts++;
        }
        return null;
    }
}
