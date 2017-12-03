package QueryEngine;

import java.sql.*;
import java.util.*;

/**
 * Dataset Class serves as a collection of entries retrieved from an SQL query, or built from a file.
 * Serves as a wrapper of a list of rows where each row is a list of the column values in the result table.
 */
public class Dataset extends ArrayList<List<String>> {

    public Dataset() {

    }

    public Dataset(List<List<String>> data) {
        for (List<String> line : data) {
            List<String> adding = new ArrayList<>(line);
            this.add(adding);
        }
    }

    public Dataset(Dataset dataset) {
        super(dataset);
    }

    // Convert an SQL result set into dataset format
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

    public void shuffle() {
        Collections.shuffle(this);
    }

    public Dataset getTrainingSet() {
        return new Dataset(this.subList(0, this.size() / 2));
    }

    public Dataset getTestSet() {
        return new Dataset(this.subList(this.size() / 2, this.size()));
    }

    public void print() {
        this.forEach(set -> System.out.println(set.toString()));
    }
}
