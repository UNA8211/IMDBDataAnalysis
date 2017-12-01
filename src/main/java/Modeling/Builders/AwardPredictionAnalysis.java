package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;
import weka.classifiers.lazy.IBk;

public class AwardPredictionAnalysis extends ModelBuilderBase {

    private final String[] attibutes = {"year", "DATE", "runtime",
            "NUMERIC", "budget", "NUMERIC", "revenue", "NUMERIC", "averagerating", "NUMERIC", "numvotes", "NUMERIC",
            "class", "NUMERIC", "nominations", "NUMERIC"};

    public AwardPredictionAnalysis() {

    }

    public void buildModel(Dataset dataset, Dataset ignored, TimeSpan timespan) {
        this.crossValidate(dataset, new IBk(3), this.attibutes);
    }
}