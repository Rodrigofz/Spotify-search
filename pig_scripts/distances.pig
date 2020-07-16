raw_data = LOAD 'distances.tsv' USING PigStorage('\t') AS (country1, country2, distance);

-- Filter by chosen country
filtered = FOREACH (FILTER raw_data BY country1 == '$country') GENERATE country2 as country, distance as distance;

ordered_ranking = ORDER filtered BY distance DESC;

STORE ordered_ranking INTO 'distances_scores/$country';