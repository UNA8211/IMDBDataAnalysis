package Modeling;

/**
 * ActorPair provides functionality for storing a pair of actors and their respective ratings,
 * used to collect and supply data with respect to the quality of the individuals and the pair as a whole.
 */
public class ActorPair implements Comparable<ActorPair> {

    public final String actor1;
    public final String actor2;

    private int count;
    private float combinedActorQuality;
    private float avgIndividualActorQuality;

    public ActorPair(String nConst1, String nConst2, float combinedActorQuality) {
        this.actor1 = nConst1;
        this.actor2 = nConst2;
        this.count = 1;
        this.combinedActorQuality = combinedActorQuality;
    }

    // Factor the supplied rating into the average rating by weighting the current rating by the
    // number of examples
    public void addPair(float rating) {
        this.combinedActorQuality = ((this.combinedActorQuality * this.count++) + rating) / this.count;
    }

    public int getCount() {
        return this.count;
    }

    public float getCombinedActorQuality() {
        return this.combinedActorQuality;
    }

    public void setAvgIndividualActorQuality(float quality) {
        this.avgIndividualActorQuality = quality;
    }

    public float getAvgIndividualActorQuality() {
        return this.avgIndividualActorQuality;
    }

    @Override
    public String toString() {
        return "(" + this.actor1 + ", " + this.actor2 + "), Frequency: " + this.count + ", AvgRating: " + this.avgIndividualActorQuality + ", CombinedRating: " + this.combinedActorQuality;
    }

    @Override
    public int hashCode() {
        return this.actor1.hashCode() + this.actor2.hashCode();
    }

    @Override
    public boolean equals(Object pair) {
        if (pair instanceof ActorPair) {
            ActorPair comparison = (ActorPair) pair;
            return this.actor1.equals(comparison.actor1) && this.actor2.equals(comparison.actor2);
        }
        return false;
    }

    @Override
    public int compareTo(ActorPair pair) {
        return Float.compare(this.combinedActorQuality, pair.combinedActorQuality);
    }
}
