-- Movies where an actor died prior to release
SELECT
  primaryTitle,
  averageRating
FROM
  (SELECT DISTINCT
     nConst,
     deathYear,
     tConst,
     averageRating
   FROM
     Person
     NATURAL JOIN Acts_In
     NATURAL JOIN Directs
     NATURAL JOIN Writes
     NATURAL JOIN Ratings
   WHERE averageRating IS NOT NULL
         AND averageRating != 0
         AND deathYear IS NOT NULL
         AND deathYear > 1900
  ) AS Part
  JOIN Production ON Part.tConst = Production.tConst
WHERE
  adult = 0
  AND startYear > 1900
  AND startYear > deathYear;

-- Movies where no actors died prior to release
SELECT
  primaryTitle,
  averageRating
FROM (
       SELECT DISTINCT
         nConst,
         deathYear,
         tConst,
         averageRating
       FROM
         Person
         NATURAL JOIN Acts_In
         NATURAL JOIN Directs
         NATURAL JOIN Writes
         NATURAL JOIN Ratings
       WHERE averageRating IS NOT NULL
             AND averageRating != 0
             AND deathYear IS NOT NULL
     ) AS Part
  JOIN Production ON Part.tConst = Production.tConst
WHERE
  adult = 0
  AND startYear > 1900
  AND startYear < deathYear;
