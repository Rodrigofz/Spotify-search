-- Loads the file
raw_songs = LOAD 'hdfs://cm:9000/uhadoop2020/3grupo/data.csv' USING PigStorage(',') AS (position:double, track_name, artist, streams, url, date, region);

-- Generate key 
full_songs = FOREACH raw_songs GENERATE CONCAT(artist,'##',track_name) as key, position;

-- Group songs by key
grouped = GROUP full_songs BY key;

--flat = FOREACH grouped GENERATE FLATTEN(full_songs);

-- Sum
mean = FOREACH grouped GENERATE group, AVG(full_songs.position) AS total_positions;

-- Calculate total "means"
data_grp = GROUP mean ALL;
result = FOREACH data_grp GENERATE SUM(mean.total_positions) as total_means;

-- Add total means to mean
pre_ranking = FOREACH mean GENERATE $0, $1, result.total_means;

-- Ranking :D
ranking = FOREACH pre_ranking GENERATE $0 as key, ((200-$1)/$2) as ranking;
-- Order
ordered_ranking = ORDER ranking BY ranking DESC;

STORE ordered_ranking INTO 'hdfs://cm:9000/uhadoop2020/3grupo/test_proyecto';
