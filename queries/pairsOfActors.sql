-- Find all pairs of actors
SELECT
  actor1.nConst,
  actor2.nConst,
  primaryTitle,
  averageRating
FROM ((SELECT
         nConst,
         tConst
       FROM Person
         NATURAL JOIN Acts_In) AS actor1
  JOIN (SELECT
          nConst,
          tConst
        FROM Person
          NATURAL JOIN Acts_In) AS actor2
    ON actor1.tConst = actor2.tConst AND actor1.nConst != actor2.nConst)
  LEFT JOIN Production
    ON Production.startYear > 1980
       AND Production.startYear < 2020
       AND actor1.tConst = Production.tConst
  JOIN Ratings ON Production.tConst = Ratings.tConst;