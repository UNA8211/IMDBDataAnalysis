SELECT *
FROM (SELECT
        movie1.primaryTitle AS title1,
        movie1.startYear    AS releaseDate1,
        movie2.primaryTitle AS title2,
        movie2.startYear    AS releaseDate2
      FROM
        (SELECT
           tConst,
           primaryTitle,
           startYear
         FROM Production
         WHERE titleType = 'movie'
               AND adult = 0
               AND Production.startYear > 1980) AS movie1
        , (SELECT
             tConst,
             primaryTitle,
             startYear
           FROM Production
           WHERE titleType = 'movie'
                 AND adult = 0
                 AND Production.startYear > 1980) AS movie2
      -- WHERE movie2.startYear > movie1.startYear
      -- AND movie2.primaryTitle LIKE (movie1.primaryTitle + '_')
      LIMIT 1000000) AS pairs
WHERE title2 LIKE ('_' + title1 + '_');