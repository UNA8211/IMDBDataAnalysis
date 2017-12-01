import QueryEngine.Dataset;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatasetBuilder {

    public static Dataset buildDataset(String fileName) {
        List<String> dataLines = Objects.requireNonNull(getFileStream(new File(fileName))).collect(Collectors.toList());

        return dataLines.parallelStream()
                .map(line -> Arrays.asList(line.split("\t")))
                .collect(Collectors.toCollection(Dataset::new));
    }

    private static Stream<String> getFileStream(File file) {
        try {
            return new BufferedReader(new FileReader(file)).lines();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
