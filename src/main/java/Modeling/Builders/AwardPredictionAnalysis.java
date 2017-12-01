package Modeling.Builders;

import Modeling.TimeSpan;
import Utilities.Utils;
import QueryEngine.Dataset;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AwardPredictionAnalysis extends ModelBuilderBase {
    public AwardPredictionAnalysis() {

    }

    public void buildModel(Dataset trainingData, Dataset classifyingData, TimeSpan timespan) {


        Utils.exportToArff(trainingData, "award_prediction_train.arff", "awards", "year", "DATE", "runtime",
                "NUMERIC", "budget", "NUMERIC", "revenue", "NUMERIC", "averagerating", "NUMERIC", "numvotes", "NUMERIC",
                "class", "NUMERIC", "nominations", "NUMERIC");

        Utils.exportToArff(classifyingData, "award_prediction_class.arff", "awards", "year", "DATE", "runtime",
                "NUMERIC", "budget", "NUMERIC", "revenue", "NUMERIC", "averagerating", "NUMERIC", "numvotes", "NUMERIC",
                "class", "NUMERIC", "nominations", "NUMERIC");

        try {
            ArffLoader loader = new ArffLoader();
            loader.setFile(new File("src/main/java/data/award_prediction_train.arff"));
            Instances structure = loader.getStructure();
            structure.setClassIndex(structure.numAttributes() - 1);

            IBk iBk = new IBk(3);
            iBk.buildClassifier(structure);
            Instance current;
            while ((current = loader.getNextInstance(structure)) != null) {
                iBk.updateClassifier(current);
            }

            System.out.println(iBk);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}