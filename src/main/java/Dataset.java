import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Dataset extends ArrayList<List<String>> {

    Dataset() {

    }

    Dataset(Dataset dataset) {
        super(dataset);
    }

    Dataset(ResultSet r) {
        convert(r);
    }

    private void convert(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columns = new String[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columns[i - 1] = metaData.getColumnName(i);
            }

            while (resultSet.next()) {
                List<String> values = new ArrayList<>(columnCount);
                for (String column : columns) {
                    values.add(resultSet.getString(column));
                }
                this.add(values);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCol(List<String> vals) {
        int i = 0;
        for (List<String> row : this) {
            row.add(vals.get(i));
            i++;
        }
    }

    public void print() {
        this.forEach(set -> System.out.println(set.toString()));
    }
}
