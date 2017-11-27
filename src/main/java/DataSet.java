import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataSet {
    private HashMap<String, ArrayList<String>> dataSet;
    public DataSet() {
        dataSet = new HashMap<String, ArrayList<String>>();
    }
    public DataSet(ResultSet r) {
        dataSet = new HashMap<String, ArrayList<String>>();
        convert(r);
    }

    private void convert(ResultSet r) {
        try {
            ResultSetMetaData metaData = r.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columns = new String[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columns[i - 1] = metaData.getColumnName(i);
                dataSet.put(metaData.getColumnName(i), new ArrayList<String>());
            }

            while (r.next()) {
                for (int i = 0; i < columns.length; i++) {
                    dataSet.get(columns[i]).add(r.getString(i + 1));
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println(dataSet.get("primaryName").get(0));
    }
}
