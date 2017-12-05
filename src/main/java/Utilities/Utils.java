package Utilities;

import QueryEngine.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class that contains several useful functions for field separation, file export, and parsing.
 */
public class Utils {

    private static final Pattern AWARD_NUM_REGEX = Pattern.compile("(\\d+)");

    /**
     * Uses Extracts the given attributes from a JSON object.
     * @param parse JSON object to extract from
     * @param attrs List of attributes to pull
     * @return a list of the attribute's values
     */
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


    /**
     * Splits the award string into two values for nominations and wins.
     * @param unParsed unparsed award string
     * @return both nominations and wins
     */
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

    /**
     * Generic extractor using a given regex pattern.
     * @param pattern pattern to match
     * @param line string to parse
     * @return List of all extracted values
     */
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

    /**
     * Converts a dataset to an arff file for use in Weka's classifier classes
     * @param s Dataset to convert
     * @param fileName file to write to
     * @param relation relation title
     * @param attrs in order list of attributes and their type. Each attribute should be given a equivalent type.
     */
    public static void exportToArff(Dataset s, String fileName, String relation, String... attrs) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter("src/main/java/Data/" + fileName);
            bw = new BufferedWriter(fw);

            bw.write("@RELATION " + relation + "\n\n");
            // Write all attributes and types
            for (int i = 0; i < attrs.length - 1; i += 2) {
                bw.write("@ATTRIBUTE " + attrs[i] + " " +
                        (attrs[i + 1].equalsIgnoreCase("date") ? "DATE \"yyyy\"" : attrs[i + 1]) + "\n");
            }

            bw.write("\n");

            bw.write("@DATA\n");
            // Add each data file
            for (List<String> row : s) {
                bw.write(row.toString().substring(12).replace("]", "") + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close readers/writers
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

    /**
     * Remove lines of a Dataset if a specified attribute contains no value.
     * @param s Dataset to prune
     * @param attrIndex Index of the attribute to check
     */
    public static void pruneAttribute(Dataset s, int attrIndex) {
        s.removeIf(row -> row.get(attrIndex).equalsIgnoreCase("n/a"));
    }

    /**
     * Remove USD format. Converts to straight integer
     * @param money dollar value in USD format
     * @return formatted string
     */
    private static String parseMoney(String money) {
        String s = money.replace("$", "");
        s = s.replace(",", "");

        return s;
    }

    /**
     * Extracts the month from a date.
     * @param date date in the format dd MMM yyyy
     * @return Month as MMM
     */
    public static String convertDate(String date) {
        try {
            DateFormat df = new SimpleDateFormat("dd MMM yyyy");
            Date formatted = df.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatted);
            return Month.of(cal.get(Calendar.MONTH) + 1).name();
        } catch (ParseException e) {
            return "N/A";
        }
    }

    /**
     * Wait a specified period of time
     * @param seconds seconds to wait
     */
    public static void sleep(int seconds) {
        double endSleep = (System.currentTimeMillis() / 1000.0 + seconds);
        while (true) {
            if ((System.currentTimeMillis() / 1000.0) > endSleep) {
                break;
            }
        }
    }

    /**
     * Set the console to output into a text file.
     * @param fileName file to write
     */
    public static void setFileOut(String fileName) {
        try {
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(fileName.concat(".txt"), true)), true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set console output back to the console.
     */
    public static void setConsoleOut() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
}
