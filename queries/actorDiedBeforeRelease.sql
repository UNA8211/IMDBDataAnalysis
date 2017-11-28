SELECT
  primaryName,
  deathYear,
  primaryTitle,
  startYear,
  averageRating
FROM Person
  NATURAL JOIN Acts_In
  NATURAL JOIN Production
  LEFT JOIN Ratings ON Ratings.tConst = Production.tConst
WHERE
  deathYear IS NOT NULL
  AND averageRating IS NOT NULL
  AND deathYear > 1980
  AND startYear > deathYear
  AND titleType = 'movie'
  AND adult = 0
ORDER BY primaryName ASC;

SELECT
  primaryName,
  deathYear,
  primaryTitle,
  startYear,
  averageRating
FROM Person
  NATURAL JOIN Acts_In
  NATURAL JOIN Production
  LEFT JOIN Ratings ON Ratings.tConst = Production.tConst
WHERE
  averageRating IS NOT NULL
  AND (deathYear IS NULL OR startYear < deathYear)
  AND startYear > 1980
  AND titleType = 'movie'
  AND adult = 0
ORDER BY primaryName ASC;
