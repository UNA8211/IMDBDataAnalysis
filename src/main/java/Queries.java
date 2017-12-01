@SuppressWarnings("WeakerAccess")
public final class Queries {

    public static String sequels = "" +
            "SELECT p1.primaryTitle, p2.primaryTitle " +
            "FROM Production P1, Production P2 " +
            "WHERE p2.primaryTitle LIKE p1.primaryTitle + '%' " +
            "LIMIT 1000";

    public static String getActorDiedBeforeReleaseQuery(int startYear, int endYear) {
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

    public static String getAwardDataQuery() {
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
                "ORDER BY tConst " +
                "LIMIT 300";
    }

    public static String getActorNotDiedBeforeReleaseQuery(int startYear, int endYear) {
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

    public static String getActorPairsQuery(int startYear, int endYear) {
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

    public static String getActorAverageRatingQuery(int startYear, int endYear) {
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

    public static String actorsPrimaryGenre = "" +
            "SELECT COUNT(primaryName) " +
            "FROM Person NATURAL JOIN Acts_In NATURAL JOIN Production NATURAL JOIN Genre " +
            "GROUP BY primaryName " +
            "LIMIT 100";

    public static String test = "" +
            "SELECT primaryName, genre, tConst " +
            "FROM Person NATURAL JOIN Acts_In NATURAL JOIN Production NATURAL JOIN Genre " +
            // "WHERE true " +
            "LIMIT 100";

    public static String actorPairs = "" +
            "SELECT DISTINCT\n" +
            "   Actor1.primaryName as name1,\n" +
            "   Actor1.nConst as id1,\n" +
            "   Actor2.primaryName as name2,\n" +
            "   Actor2.nConst as id2\n" +
            "FROM\n" +
            "   (SELECT\n" +
            "       primaryName,\n" +
            "       primaryTitle,\n" +
            "       nConst,\n" +
            "       tConst,\n" +
            "       titleType\n" +
            "   FROM Person\n" +
            "       NATURAL JOIN Acts_In\n" +
            "       NATURAL JOIN Production) AS Actor1,\n" +
            "   (SELECT\n" +
            "       primaryName,\n" +
            "       primaryTitle,\n" +
            "       nConst,\n" +
            "       tConst,\n" +
            "       titleType\n" +
            "   FROM Person\n" +
            "       NATURAL JOIN Acts_In\n" +
            "       NATURAL JOIN Production) AS Actor2\n" +
            "WHERE Actor1.nConst NOT LIKE Actor2.nConst\n" +
            "   AND Actor1.primaryTitle LIKE Actor2.primaryTitle\n" +
            "   AND Actor1.titleType LIKE 'movie'\n" +
            "LIMIT 50";
}