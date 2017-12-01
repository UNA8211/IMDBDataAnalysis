import QueryEngine.JSONEngine;

import java.io.*;
import java.util.List;

public class Utils {

    public static final String AWARD_NUM_REGEX = "(\\d+)";

    public static List<String> pullAwardData(String awards) {
        awards = awards.substring(!awards.contains("Another") ? 0 : awards.indexOf("Another"));
        List<String> awardData = JSONEngine.extractFromField(AWARD_NUM_REGEX, awards);
        if (awardData.size() < 1) {
            awardData.add("0");
            awardData.add("0");
        } else if (awardData.size() == 1) {
            awardData.add(0, "0");
        }

        return awardData;
    }

    public static void setFileOut(String fileName) {
        try {
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(fileName.concat(".txt"), true)), true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setConsoleOut() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
}
