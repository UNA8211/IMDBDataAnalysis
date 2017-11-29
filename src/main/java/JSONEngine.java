import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

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
}
