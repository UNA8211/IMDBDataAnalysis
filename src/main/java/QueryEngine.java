import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;

public class QueryEngine {

    private Connection connection;

    QueryEngine() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://35.197.99.197:3306/movie";
            connection = DriverManager.getConnection(url, "root", "potatoes");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private String readAll(Reader rd) {
        try {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject readJsonFromUrl(String url) {
        try {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Dataset executeQuery(String query) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery(query);

            return new Dataset(rs);
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    public void printData(ResultSet r) {
        try {
            ResultSetMetaData metaData = r.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "   ");
            }
            System.out.println();

            while (r.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(r.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
