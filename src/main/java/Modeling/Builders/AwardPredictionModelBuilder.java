package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;
import Utilities.Utils;
import weka.classifiers.lazy.IBk;

/**
 * Attempts to answer the question
 */
public class AwardPredictionModelBuilder extends ModelBuilderBase {

    private final String[] attributes = {"year", "DATE", "runtime",
            "NUMERIC", "budget", "NUMERIC", "revenue", "NUMERIC", "averagerating", "NUMERIC", "numvotes", "NUMERIC",
            "class", "NUMERIC", "nominations", "NUMERIC"};

    public AwardPredictionModelBuilder() {

    }

    public void buildModel(Dataset dataset, Dataset ignored, TimeSpan timespan) {
        System.out.println("Initial dataset size: " + dataset.size());
        dataset.removeIf(element -> (element.get(7).equals("0") && element.get(8).equals("0")));
        System.out.println("Size of set with awards or nominations: " + dataset.size());
        //Utils.setFileOut("results/awardPrediction");

        this.accuracyRequirement = 1.0;
        this.crossValidate(dataset, new IBk(1), this.attributes);
        this.crossValidate(dataset, new IBk(3), this.attributes);
        this.crossValidate(dataset, new IBk(5), this.attributes);
        this.crossValidate(dataset, new IBk(7), this.attributes);

        this.accuracyRequirement = 2.0;
        this.crossValidate(dataset, new IBk(1), this.attributes);
        this.crossValidate(dataset, new IBk(3), this.attributes);
        this.crossValidate(dataset, new IBk(5), this.attributes);
        this.crossValidate(dataset, new IBk(7), this.attributes);

        this.accuracyRequirement = 3.0;
        this.crossValidate(dataset, new IBk(1), this.attributes);
        this.crossValidate(dataset, new IBk(3), this.attributes);
        this.crossValidate(dataset, new IBk(5), this.attributes);
        this.crossValidate(dataset, new IBk(7), this.attributes);

        this.accuracyRequirement = 4.0;
        this.crossValidate(dataset, new IBk(1), this.attributes);
        this.crossValidate(dataset, new IBk(3), this.attributes);
        this.crossValidate(dataset, new IBk(5), this.attributes);
        this.crossValidate(dataset, new IBk(7), this.attributes);

        this.accuracyRequirement = 5.0;
        this.crossValidate(dataset, new IBk(1), this.attributes);
        this.crossValidate(dataset, new IBk(3), this.attributes);
        this.crossValidate(dataset, new IBk(5), this.attributes);
        this.crossValidate(dataset, new IBk(7), this.attributes);

        //Utils.setConsoleOut();
    }
}