package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;
import Utilities.Utils;
import weka.classifiers.lazy.IBk;

public class AwardPredictionAnalysis extends ModelBuilderBase {

    private final String[] attributes = {"year", "DATE", "runtime",
            "NUMERIC", "budget", "NUMERIC", "revenue", "NUMERIC", "averagerating", "NUMERIC", "numvotes", "NUMERIC",
            "class", "NUMERIC", "nominations", "NUMERIC"};

    public AwardPredictionAnalysis() {

    }

    public void buildModel(Dataset dataset, Dataset ignored, TimeSpan timespan) {
        Utils.setFileOut("results/awardPrediction");
        this.crossValidate(dataset, new IBk(1), this.attributes);
        this.crossValidate(dataset, new IBk(3), this.attributes);
        this.crossValidate(dataset, new IBk(5), this.attributes);
        this.crossValidate(dataset, new IBk(7), this.attributes);
        Utils.setConsoleOut();
    }
}