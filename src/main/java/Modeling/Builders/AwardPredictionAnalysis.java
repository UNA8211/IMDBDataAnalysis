package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;
import weka.classifiers.lazy.IBk;

public class AwardPredictionAnalysis extends ModelBuilderBase {
    public AwardPredictionAnalysis() {

    }

    public void buildModel(Dataset awardData, Dataset unused, TimeSpan timespan) {
        IBk classifier = new IBk();
    }
}
