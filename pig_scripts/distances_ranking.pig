-- Loads the files
raw_songs = LOAD 'hdfs://cm:9000/uhadoop2020/3grupo/data.csv' USING PigStorage(',') AS (position:double, track_name, artist, streams, url, date, region);
--raw_songs = LOAD 'data-test.csv' USING PigStorage(',') AS (position:double, track_name, artist, streams, url, date, region);
distances_scores=  LOAD 'hdfs://cm:9000/uhadoop2020/3grupo/distances_scores_v2' USING PigStorage('\t') AS (country, distance_score);

-- raw_songs = LOAD 'data-test.csv' USING PigStorage(',') AS (position:double, track_name, artist, streams, url, date, region);

-- Generate key 
full_songs = FOREACH raw_songs GENERATE CONCAT(artist,'##',track_name) as key, region;

-- Score the song by its region
score = JOIN full_songs BY region, distances_scores BY country;

songs_scored = FOREACH score GENERATE full_songs::key as key, distances_scores::distance_score as score; 

-- Group
grouped = GROUP songs_scored BY key;

-- v3
-- pre_ranking = FOREACH grouped GENERATE group as key, AVG(songs_scored.score) as score;

-- v4
pre_ranking = FOREACH grouped GENERATE group as key, MAX(songs_scored.score) as score;

-- exp
exp_ranking = FOREACH pre_ranking GENERATE key, EXP(-score/6818) as score;

-- Total ranking
data_grp = GROUP exp_ranking ALL;
result = FOREACH data_grp GENERATE SUM(exp_ranking.score) as total_scores;

-- Normalize rankings
ranking = FOREACH exp_ranking GENERATE key, score/result.total_scores as ranking;

ordered_ranking = ORDER ranking BY ranking DESC;

STORE ordered_ranking INTO 'hdfs://cm:9000/uhadoop2020/3grupo/test_v5/distances_ranking';
