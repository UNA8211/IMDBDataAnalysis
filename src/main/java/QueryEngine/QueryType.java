package QueryEngine;

public enum QueryType {
    ActorDeath("Effect of an actors death prior to film release"),
    ActorNotDeath("Not"),
    ActorPair("Combined effect of actors who often work together"),
    ActorIndividual("Individual average rating"),
    Sequels("Predictive power of a films attributes on the chance of a sequel"),
    PrimaryGenre("Relative performance of an individual outside their primary genre"),
    Awards("Predictive power of a films attributes on the number of awards it receives");

    private String description;

    QueryType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
