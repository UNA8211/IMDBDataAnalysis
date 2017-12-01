package QueryEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dataset extends ArrayList<List<String>> {

    public Dataset() {

    }

    public Dataset(List<List<String>> data) {
        for(List<String> line : data) {
            List<String> adding = new ArrayList();
            for (String s : line) {
                adding.add(new String(s));
            }
            this.add(adding);
        }
    }

    public Dataset(Dataset dataset) {
        super(dataset);
    }

    public Dataset(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columns = new String[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columns[i - 1] = metaData.getColumnLabel(i);
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
