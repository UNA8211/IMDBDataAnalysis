SELECT
  primaryName,
  genre,
  averageRating
FROM Person
  NATURAL JOIN Acts_In
  NATURAL JOIN Genre
  JOIN Ratings
    ON (Ratings.tConst = Acts_In.tConst
        AND Ratings.averageRating IS NOT NULL)
  JOIN Production
    ON (Acts_In.tConst = Production.tConst
        AND Production.adult = 0
        AND Production.startYear > 1980)