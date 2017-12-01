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
            ArffLoader trainingLoader = new ArffLoader();
            trainingLoader.setFile(new File("src/main/java/data/award_prediction_train.arff"));
            Instances structure = trainingLoader.getStructure();
            structure.setClassIndex(structure.numAttributes() - 2);

            IBk iBk = new IBk(3);
            iBk.buildClassifier(structure);
            Instance current;
            while ((current = trainingLoader.getNextInstance(structure)) != null) {
                iBk.updateClassifier(current);
            }

            System.out.println(iBk);

            ArffLoader classLoader = new ArffLoader();
            classLoader.setFile(new File("src/main/java/data/award_prediction_class.arff"));
            Instances structure2 = classLoader.getStructure();
            structure2.setClassIndex(structure2.numAttributes() - 2);

            current = null;
            int correct = 0;
            int total = 0;
            while((current = classLoader.getNextInstance(structure2)) != null) {
                double classValue = iBk.classifyInstance(current);
                if (Math.abs(classValue - current.classValue()) <= 3.0) {
                    correct++;
                }
                total++;
            }

            System.out.println("CORRECT CLASSIFICATIONS: " + correct);
            System.out.println("TOTAL CLASSIFICATIONS:   " + total);
            System.out.println("CLASSIFICATION ACCURACY: " + (correct / (double) total) * 100 + "%");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}