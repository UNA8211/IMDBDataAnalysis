import java.sql.*;

public class QueryEngine {
    Connection conn;
    public QueryEngine() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://35.197.99.197:3306/movie";
            conn = DriverManager.getConnection(url,"root","potatoes");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery(query);

            return rs;
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
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
