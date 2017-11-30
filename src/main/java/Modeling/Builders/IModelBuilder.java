package Modeling.Builders;

import Modeling.TimeSpan;
import QueryEngine.Dataset;

public interface IModelBuilder {
    void buildModel(Dataset dataset1, Dataset dataset2, TimeSpan timeSpan);
}
