SELECT
  actor1.nConst as nConst1,
  actor2.nConst as nConst2,
  averageRating as Rating
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
LIMIT 10000;