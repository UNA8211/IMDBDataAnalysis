package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;
import Utilities.Utils;

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
        Utils.setConsoleOut();
    }
}
