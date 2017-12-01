package QueryEngine;

import Utilities.Utils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONEngine {

    private static final String url = "http://www.omdbapi.com/?apikey=9ac195bd&i=";

    public static JSONObject readJsonFromUrl(String tConst) {
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
            List<String> awardData = new ArrayList<>();

            while (m.find()) {
                awardData.add(m.group(1));
            }
            return awardData;
        } catch (JSONException e) {
            return new ArrayList<>();
        }
    }

    public static void fetchData(Dataset movies, String attribute) {
        try {
            CompletableFuture.allOf(movies.parallelStream()
                    .map(movie -> CompletableFuture.supplyAsync(() ->
                            movie.addAll(Utils.pullAwardData(Objects.requireNonNull(readJsonFromUrl(movie.get(0))).getString(attribute)))))
                    .toArray(CompletableFuture[]::new))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
