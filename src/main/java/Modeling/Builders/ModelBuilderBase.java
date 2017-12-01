package Modeling.Builders;

import Modeling.ActorPair;
import Modeling.TimeSpan;
import QueryEngine.Dataset;
import Utilities.Utils;
import weka.classifiers.Classifier;
import weka.classifiers.UpdateableClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ModelBuilderBase implements IModelBuilder {

    public ModelBuilderBase() {

    }

    @Override
    public void buildModel(Dataset dataset1, Dataset dataset2, TimeSpan timeSpan) {
        // NOP
    }

    protected void crossValidate(Dataset dataset, Classifier classifier, String... attributes) {
        List<Double> accuracies = new ArrayList<>(10);
        for (int i = 0; i < 5; i++) {
            dataset.shuffle();
            Dataset trainingSet = dataset.getTrainingSet();
            Dataset testSet = dataset.getTestSet();

            // Fold One
            Utils.exportToArff(trainingSet, "training.arff", "awards", attributes);
            Utils.exportToArff(testSet, "testing.arff", "awards", attributes);
            accuracies.add(computeFold(classifier));

            // Fold Two, Flip Datasets
            Utils.exportToArff(testSet, "training.arff", "awards", attributes);
            Utils.exportToArff(trainingSet, "testing.arff", "awards", attributes);
            accuracies.add(computeFold(classifier));
        }

        double avgAccuracy = 0.0;
        for (Double accuracy : accuracies) {
            avgAccuracy += accuracy;
        }
        avgAccuracy /= accuracies.size();

        double stdDev = 0.0;
        for (Double accuracy : accuracies) {
            stdDev += Math.pow(accuracy - avgAccuracy, 2);
        }
        stdDev = Math.sqrt(stdDev / accuracies.size());

        System.out.println(classifier.toString());
        System.out.println("------------------------------------------------");
        System.out.println("Average accuracy: " + avgAccuracy + ", StdDev: " + stdDev);
        System.out.println("\n\n");
    }

    private double computeFold(Classifier classifier) {
        try {
            ArffLoader trainingLoader = new ArffLoader();
            trainingLoader.setFile(new File("src/main/java/data/training.arff"));
            Instances structure = trainingLoader.getStructure();
            structure.setClassIndex(structure.numAttributes() - 2);
            classifier.buildClassifier(structure);

            Instance current;
            while ((current = trainingLoader.getNextInstance(structure)) != null) {
                ((UpdateableClassifier) classifier).updateClassifier(current);
            }

            ArffLoader classLoader = new ArffLoader();
            classLoader.setFile(new File("src/main/java/data/testing.arff"));
            Instances structure2 = classLoader.getStructure();
            structure2.setClassIndex(structure2.numAttributes() - 2);

            int correct = 0;
            int total = 0;
            while ((current = classLoader.getNextInstance(structure2)) != null) {
                double classValue = classifier.classifyInstance(current);
                if (Math.abs(classValue - current.classValue()) <= 2.0) {
                    correct++;
                }
                total++;
            }

            double accuracy = (correct / (double) total);
//            System.out.println("CORRECT CLASSIFICATIONS: " + correct);
//            System.out.println("TOTAL CLASSIFICATIONS:   " + total);
//            System.out.println("CLASSIFICATION ACCURACY: " + (accuracy * 100) + "%");

            return accuracy;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    protected void computeStats(List<ActorPair> pairs) {
        // Sample means
        double combinedMean = 0.0;
        double individualMean = 0.0;

        for (ActorPair pair : pairs) {
            combinedMean += pair.getCombinedActorQuality();
            individualMean += pair.getAvgIndividualActorQuality();
        }

        combinedMean /= pairs.size();
        individualMean /= pairs.size();

        // Sample standard deviations
        double combinedStd = 0.0;
        double individualStd = 0.0;

        for (ActorPair pair : pairs) {
            combinedStd += Math.pow(pair.getCombinedActorQuality() - combinedMean, 2);
            individualStd += Math.pow(pair.getAvgIndividualActorQuality() - individualMean, 2);
        }

        combinedStd = Math.sqrt(combinedStd / pairs.size());
        individualStd = Math.sqrt(individualStd / pairs.size());

        System.out.println("Combined: " + combinedMean + ", (" + combinedStd + ")");
        System.out.println("Individual: " + individualMean + ", (" + individualStd + ")");
    }
}
