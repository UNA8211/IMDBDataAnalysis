import QueryEngine.Dataset;
import QueryEngine.JSONEngine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String AWARD_NUM_REGEX = "(\\d+)";

    public static List<String> pullAwardData(String awards) {
        awards = awards.substring(awards.indexOf("Another") == -1 ? 0 : awards.indexOf("Another"));
        List<String> awardData = JSONEngine.extractFromField(AWARD_NUM_REGEX, awards);
        if (awardData.size() < 1) {
            awardData.add("0");
            awardData.add("0");
        } else if (awardData.size() == 1) {
            awardData.add(0, "0");
        }

        return awardData;
    }
    public static List<String> noAwards() {
        List<String> noAwards = new ArrayList();
        noAwards.add("0");
        noAwards.add("0");
        return noAwards;
    }

    public static void exportToArff(Dataset s, String fileName, String relation, String... attrs) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter("src/main/java/data/" + fileName);
            bw = new BufferedWriter(fw);

            bw.write("@RELATION " + relation + "\n");
            for (int i = 0; i < attrs.length - 1; i+=2) {
                bw.write("@ATTRIBUTE " + attrs[i] + " " +
                        (attrs[i + 1].equalsIgnoreCase("date") ? "DATE \"yyyy\"" : attrs[i + 1]) + "\n");
            }

            bw.write("\n");

            bw.write("@DATA\n");
            for (List<String> row : s) {
                bw.write(row.toString().substring(12).replace("]", "") + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }

                if (fw != null) {
                    bw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
