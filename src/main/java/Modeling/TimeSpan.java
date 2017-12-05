package Modeling;

/**
 * A container object for a time window during which a query is valid,
 * typically used to represent a particular decade
 */
public class TimeSpan {

    public final int startYear;
    public final int endYear;

    public TimeSpan(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
    }

    @Override
    public String toString() {
        return "Period, (" + startYear + ", " + endYear + ")";
    }
}
