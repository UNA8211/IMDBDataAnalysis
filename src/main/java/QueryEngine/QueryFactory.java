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
                return null;
            case Sequels:
                return getSequelsQuery();
            case PrimaryGenre:
                return getActorsGenres(timeSpan.startYear, timeSpan.endYear);
            default:
                throw new IllegalArgumentException("Invalid query type!");
        }
    }

    private static String getSequelsQuery() {
        return "SELECT p1.primaryTitle, p2.primaryTitle " +
                "FROM Production P1, Production P2 " +
                "WHERE p2.primaryTitle LIKE p1.primaryTitle + '%' " +
                "LIMIT 1000";
    }

    private static String getActorDiedBeforeReleaseQuery(int startYear, int endYear) {
        return "SELECT\n" +
                "  primaryName,\n" +
                "  deathYear,\n" +
                "  primaryTitle,\n" +
                "  startYear,\n" +
                "  averageRating\n" +
                "FROM Person\n" +
                "  NATURAL JOIN Acts_In\n" +
                "  NATURAL JOIN Production\n" +
                "  LEFT JOIN Ratings ON Ratings.tConst = Production.tConst\n" +
                "WHERE\n" +
                "  deathYear IS NOT NULL\n" +
                "  AND averageRating IS NOT NULL\n" +
                "  AND startYear > deathYear\n" +
                "  AND startYear > " + startYear + "\n" +
                "  AND startYear < " + endYear + "\n" +
                "  AND titleType = 'movie'\n" +
                "  AND adult = 0\n" +
                "ORDER BY primaryName ASC";
    }

    private static String getActorNotDiedBeforeReleaseQuery(int startYear, int endYear) {
        return "SELECT\n" +
                "  primaryName,\n" +
                "  deathYear,\n" +
                "  primaryTitle,\n" +
                "  startYear,\n" +
                "  averageRating\n" +
                "FROM Person\n" +
                "  NATURAL JOIN Acts_In\n" +
                "  NATURAL JOIN Production\n" +
                "  LEFT JOIN Ratings ON Ratings.tConst = Production.tConst\n" +
                "WHERE\n" +
                "  averageRating IS NOT NULL\n" +
                "  AND (deathYear IS NULL OR startYear < deathYear)\n" +
                "  AND startYear > " + startYear + "\n" +
                "  AND startYear < " + endYear + "\n" +
                "  AND titleType = 'movie'\n" +
                "  AND adult = 0\n" +
                "ORDER BY primaryName ASC";
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
                "            AND titleType = 'movie'\n" +
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
                "        AND Production.adult = 0\n" +
                "        AND Production.startYear > " + startYear + "\n" +
                "        AND Production.startYear < " + endYear + ")";
    }
}
