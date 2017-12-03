package Utilities;

import QueryEngine.Dataset;
import QueryEngine.JSONEngine;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utils {

    public static final Pattern AWARD_NUM_REGEX = Pattern.compile("(\\d+)");

    public static List<String> parseJsonAttr(JSONObject parse, String... attrs) {
        List<String> attrsForObject = new ArrayList<>();
        for (String attr : attrs) {
            switch (attr) {
                case "Awards":
                    attrsForObject.addAll(pullAwardsData(parse.getString(attr)));
                default:
                    attrsForObject.add(Objects.requireNonNull(parse.getString(attr)));
            }
        }
        return attrsForObject;
    }


    public static List<String> pullAwardsData(String unParsed) {
        unParsed = unParsed.substring(!unParsed.contains("Another") ? 0 : unParsed.indexOf("Another"));
        List<String> awardData = JSONEngine.extractFromField(AWARD_NUM_REGEX, unParsed);
        if (awardData.size() < 1) {
            awardData.add("0");
            awardData.add("0");
        } else if (awardData.size() == 1) {
            awardData.add(0, "0");
        }

        return awardData;
    }

    public static List<String> noAwards() {
        List<String> noAwards = new ArrayList<>();
        noAwards.add("0");
        noAwards.add("0");
        return noAwards;
    }

    public static void exportToArff(Dataset s, String fileName, String relation, String... attrs) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter("src/main/java/Data/" + fileName);
            bw = new BufferedWriter(fw);

            bw.write("@RELATION " + relation + "\n\n");
            for (int i = 0; i < attrs.length - 1; i += 2) {
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
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public Date convertToDate(String date) {
//
//    }

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
