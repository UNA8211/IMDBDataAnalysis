package Modeling.Builders;

import QueryEngine.Dataset;

public interface IModelBuilder {
    void buildModel(Dataset dataset1, Dataset dataset2, Integer startYear, Integer endYear);
}
