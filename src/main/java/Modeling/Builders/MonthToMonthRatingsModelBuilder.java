package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;
import Utilities.Utils;

import java.time.Month;
import java.util.*;
import java.util.regex.Pattern;

public class MonthToMonthRatingsModelBuilder extends ModelBuilderBase {

    private Pattern validTitle = Pattern.compile("[a-zA-Z\\s]+");
    private Pattern possibleSequel1 = Pattern.compile("(.)+[:,;]");
    private Pattern possibleSequel2 = Pattern.compile("(.)+[Two]+");
    private Pattern possibleSequel3 = Pattern.compile("(.)+[2]+");

    public MonthToMonthRatingsModelBuilder() {

    }

    @Override
    public void buildModel(Dataset movies, Dataset ignored, TimeSpan timeSpan) {
        movies.forEach(System.out::println);
        //Utils.setConsoleOut();

        HashMap<Integer, Dataset> year = splitOnYear(movies);
        System.out.println("NEW DATASET");

        //todo: Run set on dataset
        //todo: Logging and output
        Iterator it = year.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            year.get(pair.getKey());
        }

    }

    private HashMap<Integer, Dataset> splitOnYear(Dataset s) {
        HashMap<Integer, Dataset> setsByYear = new HashMap<>();
        Dataset byYear = new Dataset();
        String year = s.get(0).get(2);
        Iterator<List<String>> iterator = s.iterator();

        while (iterator.hasNext()) {
            List<String> current = iterator.next();
            if (!current.get(2).equalsIgnoreCase(year)) {
                setsByYear.put(Integer.parseInt(year), byYear);
                byYear = new Dataset();
                year = current.get(2);
            }
            byYear.add(current);
        }
        setsByYear.put(Integer.parseInt(year),byYear);
        return setsByYear;
    }

    private List<List<Double>> calcMonthsForYear(Dataset s) {
        List<List<Double>> monthPerformances = new ArrayList<>();
        for (Month month : Month.values()) {
            Dataset rowsForMonth = new Dataset();
            for (List<String> row : s) {
                if (row.get(4).equalsIgnoreCase(month.name().substring(0, 3))) {
                    rowsForMonth.add(row);
                }
            }
            monthPerformances.add(averageRatingForMonth(rowsForMonth));
        }
        return monthPerformances;
    }

    private List<Double> averageRatingForMonth(Dataset s) {
        List<Double> moviePerformances = new ArrayList<>();
        for (List<String> movie : s) {
            moviePerformances.add(calcPerformance(Double.parseDouble(movie.get(1)), Integer.parseInt(movie.get(3))));
        }
        return moviePerformances;
    }

    private Double calcPerformance(Double i, Integer r) {
        return (i / 2) * Math.sqrt(r);
    }


}
