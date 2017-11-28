public final class Queries {

    public static String sequels = "SELECT p1.primaryTitle, p2.primaryTitle " +
            "FROM Production P1, Production P2 " +
            "WHERE p2.primaryTitle LIKE p1.primaryTitle + '%' " +
            "LIMIT 1000";

    public static String deadBeforeRelease = "SELECT p.primaryName, p.deathYear, m.primaryTitle, m.startYear " +
            "FROM Person p, Acts_In pr, Production m " +
            "WHERE p.deathYear IS NOT NULL " +
            "AND p.deathYear > 2000 " +
            "AND p.nconst = pr.nconst " +
            "AND pr.tconst = m.tconst " +
            "AND m.startYear >= p.deathYear " +
            "AND m.titleType = 'movie' " +
            "ORDER BY primaryName ASC";
}
