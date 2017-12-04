package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;

import java.time.Month;
import java.util.*;

public class MonthToMonthRatingsModelBuilder extends ModelBuilderBase {

    public MonthToMonthRatingsModelBuilder() {

    }

    @Override
    public void buildModel(Dataset movies, Dataset ignored, TimeSpan timeSpan) {
        movies.forEach(System.out::println);
        //Utils.setConsoleOut();

        HashMap<Integer, Dataset> year = splitOnYear(movies);
        System.out.println("NEW DATASET");

        //todo: Run set on dataset
        List<List<Double>> allAverages = new ArrayList<>();
        for (Object o : year.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            allAverages.add(calcMonthsForYear(year.get(pair.getKey()), (Integer) pair.getKey()));
        }

        List<Double> finalAverages = getTotalMonthAverages(allAverages);
        int i = 1;
        for (Double average : finalAverages) {
            System.out.println("AVERAGE PERFORMANCE FOR " + Month.of(i).name());
            System.out.println(average);
            i++;
        }
    }

    private List<Double> getTotalMonthAverages(List<List<Double>> allAverages) {
        List<Double> averages = new ArrayList<>();
        for (Month month : Month.values()) {
            Double finalAverage = 0.0;
            for (List<Double> average : allAverages) {
                finalAverage += average.get(month.getValue() - 1);
            }
            averages.add(finalAverage / allAverages.size());
        }
        return averages;
    }

    private HashMap<Integer, Dataset> splitOnYear(Dataset s) {
        HashMap<Integer, Dataset> setsByYear = new HashMap<>();
        Dataset byYear = new Dataset();
        String year = s.get(0).get(2);

        for (List<String> current : s) {
            if (!current.get(2).equalsIgnoreCase(year)) {
                setsByYear.put(Integer.parseInt(year), byYear);
                byYear = new Dataset();
                year = current.get(2);
            }
            byYear.add(current);
        }
        setsByYear.put(Integer.parseInt(year), byYear);
        return setsByYear;
    }

    private List<Double> calcMonthsForYear(Dataset s, int year) {
        List<Double> monthPerformances = new ArrayList<>();
        for (Month month : Month.values()) {
            System.out.println("BEGINNING PERFORMANCE ANALYSIS FOR " + month.name() + ", " + year);
            Dataset rowsForMonth = new Dataset();
            for (List<String> row : s) {
                if (row.get(3).equalsIgnoreCase(month.name().substring(0, 3))) {
                    rowsForMonth.add(row);
                }
            }
            Double average = averageRatingForMonth(rowsForMonth);
            monthPerformances.add(average);

            System.out.println("AVERAGE PERFORMANCE FOR " + month.name() + ", " + year + ":");
            System.out.println(average);

            System.out.println();
        }
        return monthPerformances;
    }

    private Double averageRatingForMonth(Dataset dataset) {
        List<Double> moviePerformances = new ArrayList<>();

        for (List<String> movie : dataset) {
            Double performance = Double.parseDouble(movie.get(1));
            System.out.println("Performance for " + movie.get(0) + ": " + performance);
            moviePerformances.add(performance);
        }
        if (moviePerformances.size() == 0) {
            return 0.0;
        }
        return average(moviePerformances);
    }

    private Double average(List<Double> moviePerformances) {
        Double average = 0.0;
        for (Double perf : moviePerformances) {
            average += perf;
        }
        average /= moviePerformances.size();
        return average;
    }

    //todo: Add award and nominations metric
    private Double calcPerformance(Double i, Integer r) {
        return (i / 2) * Math.sqrt(r);
    }

}
