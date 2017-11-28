SELECT
  Actor1.nConst AS id1,
  Actor2.nConst AS id2
FROM
  (SELECT
     nConst,
     primaryName,
     primaryTitle,
     tConst
   FROM Person
     NATURAL JOIN Acts_In
     NATURAL JOIN Production
   WHERE titleType = 'movie'
         AND startYear > 1980) AS Actor1
  INNER JOIN
  (SELECT
     nConst,
     primaryName,
     primaryTitle,
     tConst
   FROM Person
     NATURAL JOIN Acts_In
     NATURAL JOIN Production
   WHERE titleType = 'movie'
         AND startYear > 1980) AS Actor2
    ON Actor1.tConst = Actor2.tConst
WHERE Actor1.nConst != Actor2.nConst
      AND Actor1.tConst = Actor2.tConst;