package QueryEngine;

import java.sql.*;

/**
 * MySQL database query engine provides an API for executing SQL queries against a remote database.
 * Results are converted to Dataset format and returned upon query completion
 */
public class QueryEngine {

    private Connection connection;

    // Database information
    public QueryEngine() {
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

    /**
     * Queries the Google Cloud Service DB as a Dataset
     *
     * @param query SQL query
     * @return a Dataset of the results
     */
    public Dataset executeQuery(String query) {
        System.out.print("\nBegin SQL query execution... ");
        long startTime = System.currentTimeMillis();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("complete, runtime: " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
            return new Dataset(rs);
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }

        return null;
    }

    /**
     * End the MySQL database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }
}
