-- Find all pairs of actors
SELECT
  actor1.nConst AS nConst1,
  actor2.nConst AS nConst2,
  averageRating AS Rating
FROM (
    (SELECT
       nConst,
       tConst
     FROM Person
       NATURAL JOIN Acts_In
    ) AS actor1
    JOIN (SELECT
            nConst,
            tConst
          FROM Person
            NATURAL JOIN Acts_In
         ) AS actor2
      ON actor1.tConst = actor2.tConst AND actor1.nConst != actor2.nConst)
  LEFT JOIN Production
    ON startYear > 1980
       AND startYear < 2020
       AND actor1.tConst = Production.tConst
  JOIN Ratings
    ON Production.tConst = Ratings.tConst
LIMIT 100000;

-- Average rating for each actor
SELECT
  nConst,
  AVG(averageRating)
FROM (SELECT
        nConst,
        averageRating
      FROM Person
        NATURAL JOIN Acts_In
        JOIN Production ON (Acts_In.tConst = Production.tConst)
        JOIN Ratings ON (Production.tConst = Ratings.tConst AND Ratings.averageRating IS NOT NULL)
      WHERE adult = 0
            AND startYear > 1990
            AND startYear < 2020
            AND titleType = 'movie'
     ) AS ratings
GROUP BY nConst
LIMIT 10000;