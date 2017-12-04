package Utilities;

import QueryEngine.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern AWARD_NUM_REGEX = Pattern.compile("(\\d+)");

    public static List<String> parseJsonAttr(JSONObject parse, String... attrs) {
        List<String> attrsForObject = new ArrayList<>();
        for (String attr : attrs) {
            switch (attr) {
                case "Awards":
                    attrsForObject.addAll(pullAwardsData(parse.getString(attr)));
                    break;
                case "Ratings":
                    attrsForObject.add(Objects.requireNonNull(parse.getJSONArray(attr).getJSONObject(0).getString("Value").substring(0, 3)));
                    break;
                case "BoxOffice":
                    attrsForObject.add(Objects.requireNonNull(parseMoney(parse.getString(attr))));
                    break;
                case "Released":
                    attrsForObject.add(Objects.requireNonNull(convertDate(parse.getString(attr))));
                    break;
                default:
                    attrsForObject.add(Objects.requireNonNull(parse.getString(attr)));
            }
        }
        return attrsForObject;
    }


    private static List<String> pullAwardsData(String unParsed) {
        unParsed = unParsed.substring(!unParsed.contains("Another") ? 0 : unParsed.indexOf("Another"));
        List<String> awardData = extractFromField(AWARD_NUM_REGEX, unParsed);
        if (awardData.size() < 1) {
            awardData.add("0");
            awardData.add("0");
        } else if (awardData.size() == 1) {
            awardData.add(0, "0");
        }

        return awardData;
    }

    private static List<String> extractFromField(Pattern pattern, String line) {
        try {
            Matcher matcher = pattern.matcher(line);
            List<String> awardData = new ArrayList<>();

            while (matcher.find()) {
                awardData.add(matcher.group(1));
            }
            return awardData;
        } catch (JSONException e) {
            return new ArrayList<>();
        }
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

    public static void pruneAttribute(Dataset s, int attrIndex) {
        s.removeIf(row -> row.get(attrIndex).equalsIgnoreCase("n/a"));
    }

    private static String parseMoney(String money) {
        String s = money.replace('$', '\0');
        s = s.replace(',', '\0');

        return s;
    }

    private static String convertDate(String date) {
        if (date.isEmpty() || date.equals("N/A")) {
            return "N/A";
        }
        return date.replaceAll("[0-9]+", "").trim();
    }

    public static void sleep(int seconds) {
        double endSleep = (System.currentTimeMillis() / 1000.0 + seconds);
        while (true) {
            if ((System.currentTimeMillis() / 1000.0) > endSleep) {
                break;
            }
        }
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
