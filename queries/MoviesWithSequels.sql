SELECT *
FROM
  (SELECT
     movie1.tConst       AS id1,
     movie1.primaryTitle AS title1,
     movie1.startYear    AS releaseDate1,
     movie2.tConst       AS id2,
     movie2.primaryTitle AS title2,
     movie2.startYear    AS releaseDate2
   FROM Production movie1, Production movie2
   WHERE movie1.tConst != movie2.tConst
         AND movie1.adult = 0
         AND movie2.adult = 0
         AND movie1.titleType = 'movie'
         AND movie2.titleType = 'movie'
         AND movie1.startYear > 1990
         AND movie2.startYear > movie1.startYear
  ) AS movies
WHERE title2 LIKE CONCAT(title1, '_')
LIMIT 1000;
