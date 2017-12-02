package QueryEngine;

import Modeling.TimeSpan;

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
            case Sequels:
                return getSequelsQuery(timeSpan.startYear, timeSpan.endYear);
            case PrimaryGenre:
                return getActorsGenres(timeSpan.startYear, timeSpan.endYear);
            default:
                throw new IllegalArgumentException("Invalid query type!");
        }
    }

    private static String getSequelsQuery(int startYear, int endYear) {
        return "SELECT p1.primaryTitle, p2.primaryTitle " +
                "FROM Production P1, Production P2 " +
                "WHERE p2.primaryTitle LIKE p1.primaryTitle + '%' " +
                "LIMIT 1000";
    }

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
                "             AND deathYear IS NOT NULL\n" +
                "     ) AS Part\n" +
                "  JOIN Production ON Part.tConst = Production.tConst\n" +
                "WHERE\n" +
                "  adult = 0\n" +
                "  AND startYear > " + startYear + "\n" +
                "  AND startYear < " + endYear + "\n" +
                "  AND startYear < deathYear;";
    }

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
                "LIMIT 500000";
    }

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
                "     ) AS ratings\n" +
                "GROUP BY nConst\n";
    }

    private static String getActorsGenres(int startYear, int endYear) {
        return "SELECT\n" +
                "  primaryName,\n" +
                "  genre,\n" +
                "  averageRating\n" +
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
}
