package QueryEngine;

import Utilities.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONEngine {

    private final String url = "http://www.omdbapi.com/?apikey=9ac195bd&i=";

    public JSONEngine() {

    }

    public JSONObject readJsonFromUrl(String tConst) {
        try {
            return new JSONObject(IOUtils.toString(new URL(url + tConst), Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> extractFromField(String pattern, String line) {
        try {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(line);
            List<String> awardData = new ArrayList();

            while (m.find()) {
                awardData.add(m.group(1));
            }


            return awardData;
        } catch (JSONException e) {
            return new ArrayList<String>();
        }
    }

    public static void fetchData(JSONEngine jsonEngine, Dataset movies, String attribute) {
        try {
            CompletableFuture.allOf(movies.stream()
                    .map(movie -> CompletableFuture.supplyAsync(() ->
                            movie.addAll(Utils.pullAwardData(jsonEngine.readJsonFromUrl(movie.get(0)).getString(attribute)))))
                    .toArray(CompletableFuture[]::new)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
