public final class Queries {

    public static String sequels = "" +
            "SELECT p1.primaryTitle, p2.primaryTitle " +
            "FROM Production P1, Production P2 " +
            "WHERE p2.primaryTitle LIKE p1.primaryTitle + '%' " +
            "LIMIT 1000";

    public static String deadBeforeRelease = "" +
            "SELECT p.primaryName, p.deathYear, m.primaryTitle, m.startYear " +
            "FROM Person p, Acts_In pr, Production m " +
            "WHERE p.deathYear IS NOT NULL " +
            "AND p.deathYear > 2000 " +
            "AND p.nConst = pr.nConst " +
            "AND pr.tConst = m.tConst " +
            "AND m.startYear >= p.deathYear " +
            "AND m.titleType = 'movie' " +
            "ORDER BY primaryName ASC";

    public static String actorsPrimaryGenre = "" +
            "SELECT COUNT(primaryName) " +
            "FROM Person NATURAL JOIN Acts_In NATURAL JOIN Production NATURAL JOIN Genre " +
            "GROUP BY primaryName " +
            "LIMIT 100";

    public static String test = "" +
            "SELECT primaryName, genre " +
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
