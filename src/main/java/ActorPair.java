public class ActorPair {

    public final String actor1;
    public final String actor2;

    private int count;
    private float quality;

    public ActorPair(String nConst1, String nConst2) {
        this.actor1 = nConst1;
        this.actor2 = nConst2;
        this.count = 1;
        quality = 0;
    }

    public void addPair(float rating) {
        this.quality = ((this.quality * this.count++) + rating) / this.count;
    }

    public int getCount() {
        return this.count;
    }

    public float getQuality() {
        return this.quality;
    }

    @Override
    public String toString() {
        return this.actor1.concat(", ").concat(this.actor2) + this.count + ", " + this.quality;
    }


    @Override
    public boolean equals(Object pair) {
        if (pair instanceof ActorPair) {
            ActorPair comparison = (ActorPair) pair;
            return this.actor1.equals(comparison.actor1) && this.actor2.equals(comparison.actor2);
        }
        return false;
    }
}
