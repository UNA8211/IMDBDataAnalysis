package Modeling.Builders;

import Modeling.ActorPair;
import Modeling.TimeSpan;
import QueryEngine.Dataset;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ModelBuilderBase implements IModelBuilder {

    public ModelBuilderBase() {

    }

    @Override
    public void buildModel(Dataset dataset1, Dataset dataset2, TimeSpan timeSpan) {
        // NOP
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
