import QueryEngine.Dataset;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatasetBuilder {

    /**
     * Read data from a TSV and convert it to a Dataset
     *
     * @param fileName file to read from
     * @return Dataset of the file data
     */
    public static Dataset buildDataset(String fileName) {
        Long startTime = System.currentTimeMillis();
        System.out.print("Begin file read... ");
        List<String> dataLines = Objects.requireNonNull(getFileStream(new File(fileName))).collect(Collectors.toList());

        // Split each line on the tab character and add it to the Dataset
        Dataset dataset = dataLines.parallelStream()
                .map(line -> Arrays.asList(line.split("\t")))
                .collect(Collectors.toCollection(Dataset::new));

        System.out.println("complete, runtime: " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
        return dataset;
    }

    /**
     * Gets the file stream for reading
     *
     * @param file file to read from
     * @return file stream
     */
    private static Stream<String> getFileStream(File file) {
        try {
            return new BufferedReader(new FileReader(file)).lines();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
