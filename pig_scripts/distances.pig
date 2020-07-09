raw_data = LOAD 'distances.tsv' USING PigStorage('\t') AS (country1, country2, distance);

-- Filter by chosen country
filtered = FOREACH (FILTER raw_data BY country1 == 'cl') GENERATE country2 as country, distance as distance;

-- Maximum distance
group_all = GROUP filtered ALL;
maximum = FOREACH group_all GENERATE MAX(filtered.distance) as max_distance;

-- We do this so we can assign higher scores to closer countries
inverse = FOREACH filtered GENERATE country, maximum.max_distance-distance as inverted_dist;

-- Get the sum of all distances
data_grp = GROUP inverse ALL;
result = FOREACH data_grp GENERATE SUM(inverse.inverted_dist) as total_distance;

ranking = FOREACH inverse GENERATE country, inverted_dist/result.total_distance as score;

ordered_ranking = ORDER ranking BY score DESC;

STORE ordered_ranking INTO 'test_v2/distances_ranking';