package QueryEngine;

import Modeling.TimeSpan;

/**
 * QueryFactory maintains each of the MySQL scripts used in this project and provides a means of accessing time period
 * specific version of each by a strongly typed QueryType enum
 */
public class QueryFactory {

    public static String buildQuery(QueryType type, TimeSpan timeSpan) {
        switch (type) {
            case ActorDeath:
                return getActorDiedBeforeReleaseQuery(timeSpan.startYear, timeSpan.endYear);
            case ActorNotDeath:
                return getActorNotDiedBeforeReleaseQuery(timeSpan.startYear, timeSpan.endYear);
            case ActorPair:
                return getActorPairsQuery(timeSpan.startYear, timeSpan.endYear);
            case ActorIndividual:
                return getActorAverageRatingQuery(timeSpan.startYear, timeSpan.endYear);
            case Awards:
                return getAwardDataQuery(timeSpan.startYear, timeSpan.endYear);
            case MovieMonths:
                return getMovieMonthsQuery(timeSpan.startYear);
            case PrimaryGenre:
                return getActorsGenres(timeSpan.startYear, timeSpan.endYear);
            case GenreYear:
                return getSameYearGenreQuery(timeSpan.startYear, timeSpan.endYear, "Horror");
            default:
                throw new IllegalArgumentException("Invalid query type!");
        }
    }

    /**
     * Fetches the id, rating, and release year of films
     *
     * @param year, only fetch films released in year
     * @return SQL query as a string
     */
    private static String getMovieMonthsQuery(int year) {
        return "SELECT\n" +
                "  tConst,\n" +
                "  averageRating,\n" +
                "  startYear\n" +
                "FROM Production\n" +
                "  NATURAL JOIN Ratings\n" +
                "WHERE numVotes > 70\n" +
                "      AND titleType = 'movie'\n" +
                "      AND adult = 0\n" +
                "      AND startYear = " + year + "\n" +
                "      AND primaryTitle = originalTitle";
    }

    /**
     * Creates the SQL query selecting the names of crew members and the respective ratings of films they had a part in
     * during the supplied year range where this individual died prior to the film's release.
     *
     * @param startYear, lower bound of the date range
     * @param endYear,   upper bound of the date range
     * @return SQL query as a string
     */
    private static String getActorDiedBeforeReleaseQuery(int startYear, int endYear) {
        return "SELECT\n" +
                "  primaryTitle,\n" +
                "  startYear,\n" +
                "  deathYear,\n" +
                "  averageRating\n" +
                "FROM\n" +
                "  (SELECT DISTINCT\n" +
                "     nConst,\n" +
                "     deathYear,\n" +
                "     tConst,\n" +
                "     averageRating\n" +
                "   FROM\n" +
                "     Person\n" +
                "     NATURAL JOIN Acts_In\n" +
                "     NATURAL JOIN Directs\n" +
                "     NATURAL JOIN Writes\n" +
                "     NATURAL JOIN Ratings\n" +
                "   WHERE averageRating IS NOT NULL\n" +
                "         AND averageRating != 0\n" +
                "         AND numVotes > 20\n" +
                "         AND deathYear IS NOT NULL\n" +
                "         AND deathYear > 1900\n" +
                "  ) AS Part\n" +
                "  JOIN Production ON Part.tConst = Production.tConst\n" +
                "WHERE\n" +
                "  adult = 0\n" +
                "  AND startYear > " + startYear + "\n" +
                "  AND startYear < " + endYear + "\n" +
                "  AND startYear > deathYear;";
    }

    /**
     * Creates the SQL query selecting the names of films released during the suppied date window for which
     * non of the cast or crew died prior to the film's release.
     *
     * @param startYear, lower bound of the date range
     * @param endYear,   upper bound of the date range
     * @return SQL query as a string
     */
    private static String getActorNotDiedBeforeReleaseQuery(int startYear, int endYear) {
        return "SELECT\n" +
                "  primaryTitle,\n" +
                "  averageRating\n" +
                "FROM (\n" +
                "       SELECT DISTINCT\n" +
                "         nConst,\n" +
                "         deathYear,\n" +
                "         tConst,\n" +
                "         averageRating\n" +
                "       FROM\n" +
                "         Person\n" +
                "         NATURAL JOIN Acts_In\n" +
                "         NATURAL JOIN Directs\n" +
                "         NATURAL JOIN Writes\n" +
                "         NATURAL JOIN Ratings\n" +
                "       WHERE averageRating IS NOT NULL\n" +
                "             AND averageRating != 0\n" +
                "             AND numVotes > 20\n" +
                "             AND deathYear IS NOT NULL\n" +
                "     ) AS Part\n" +
                "  JOIN Production ON Part.tConst = Production.tConst\n" +
                "WHERE\n" +
                "  adult = 0\n" +
                "  AND startYear > " + startYear + "\n" +
                "  AND startYear < " + endYear + "\n" +
                "  AND startYear < deathYear;";
    }

    /**
     * Creates the SQL query for fetching pairs of actors who appeared together in films within the
     * provided date range
     *
     * @param startYear, lower bound of the date range
     * @param endYear,   upper bound of the date range
     * @return SQL query as a string
     */
    private static String getActorPairsQuery(int startYear, int endYear) {
        return "SELECT\n" +
                "  actor1.nConst AS nConst1,\n" +
                "  actor2.nConst AS nConst2,\n" +
                "  averageRating AS Rating\n" +
                "FROM (\n" +
                "    (SELECT\n" +
                "       nConst,\n" +
                "       tConst\n" +
                "     FROM Person\n" +
                "       NATURAL JOIN Acts_In\n" +
                "    ) AS actor1\n" +
                "    JOIN (SELECT\n" +
                "            nConst,\n" +
                "            tConst\n" +
                "          FROM Person\n" +
                "            NATURAL JOIN Acts_In\n" +
                "         ) AS actor2\n" +
                "      ON actor1.tConst = actor2.tConst AND actor1.nConst != actor2.nConst)\n" +
                "  LEFT JOIN Production\n" +
                "    ON startYear > " + startYear + "\n" +
                "       AND startYear < " + endYear + "\n" +
                "       AND adult = 0\n" +
                "       AND actor1.tConst = Production.tConst\n" +
                "  JOIN Ratings\n" +
                "    ON Production.tConst = Ratings.tConst\n" +
                "WHERE numVotes > 50\n" +
                "LIMIT 500000";
    }

    /**
     * Creates the SQL query for retrieving the average rating of each actor for films in the date range
     *
     * @param startYear, lower bound of the date range
     * @param endYear,   upper bound of the date range
     * @return SQL query as a string
     */
    private static String getActorAverageRatingQuery(int startYear, int endYear) {
        return "SELECT\n" +
                "  nConst,\n" +
                "  AVG(averageRating)\n" +
                "FROM (SELECT\n" +
                "        nConst,\n" +
                "        averageRating\n" +
                "      FROM Person\n" +
                "        NATURAL JOIN Acts_In\n" +
                "        JOIN Production ON (Acts_In.tConst = Production.tConst)\n" +
                "        JOIN Ratings ON (Production.tConst = Ratings.tConst AND Ratings.averageRating IS NOT NULL)\n" +
                "      WHERE adult = 0\n" +
                "            AND startYear > " + startYear + "\n" +
                "            AND startYear < " + endYear + "\n" +
                "            AND numVotes > 50\n" +
                "     ) AS ratings\n" +
                "GROUP BY nConst\n";
    }

    /**
     * Creates the SQL query for fetching the ratings and genres of movies within the supplied date range
     *
     * @param startYear, lower bound of the date range
     * @param endYear,   upper bound of the date range
     * @return SQL query as a string
     */
    private static String getActorsGenres(int startYear, int endYear) {
        return "SELECT\n" +
                "  primaryName,\n" +
                "  genre,\n" +
                "  averageRating," +
                "  startYear\n" +
                "FROM Person\n" +
                "  NATURAL JOIN Acts_In\n" +
                "  NATURAL JOIN Genre\n" +
                "  JOIN Ratings\n" +
                "    ON (Ratings.tConst = Acts_In.tConst\n" +
                "        AND Ratings.averageRating IS NOT NULL)\n" +
                "  JOIN Production\n" +
                "    ON (Acts_In.tConst = Production.tConst\n" +
                "        AND adult = 0\n" +
                "        AND titleType = 'movie'\n" +
                "        AND Production.startYear > " + startYear + "\n" +
                "        AND Production.startYear < " + endYear + ")\n" +
                "LIMIT 1000000";
    }

    /**
     * Creates the SQL query for fetching the financial and rating data for movies between the dates provided
     *
     * @param startYear, lower bound of the date range
     * @param endYear,   upper bound of the date range
     * @return SQL query as a string
     */
    private static String getAwardDataQuery(int startYear, int endYear) {
        return "SELECT \n" +
                "  tConst,\n" +
                "  startYear,\n" +
                "  runTime,\n" +
                "  budget,\n" +
                "  revenue,\n" +
                "  averageRating,\n" +
                "  numVotes\n" +
                "FROM Production\n" +
                "  NATURAL JOIN Finances\n" +
                "  NATURAL JOIN Ratings\n" +
                "WHERE runTime > 60\n" +
                "  AND startYear > " + startYear + "\n" +
                "  AND startYear < " + endYear + "\n" +
                "ORDER BY tConst " +
                "LIMIT 1000";
    }

    /**
     * Creates the SQL query for selecting movies from a particular genre from a given year
     *
     * @param startYear, the year in question
     * @param genre,     the specific genre to be retrieved
     * @return SQL query as string
     */
    private static String getSameYearGenreQuery(int startYear, int endYear, String genre) {
        return "SELECT p.tConst, p.primaryTitle, g.genre, r.numVotes, r.averageRating\n" +
                "FROM Production p, Genre g, Ratings r\n" +
                "WHERE g.genre LIKE '" + genre + "'\n" +
                "AND p.titleType like 'movie'\n" +
                "AND p.startYear = " + startYear + "\n" +
                "AND r.numVotes > 200\n" +
                "AND p.tConst = g.tConst\n" +
                "AND r.tConst = p.tConst";
    }
}