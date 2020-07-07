-- Loads the file
raw_songs = LOAD 'hdfs://cm:9000/uhadoop2020/3grupo/data.csv' USING PigStorage(',') AS (position:double, track_name, artist, streams, url, date, region);
-- raw_songs = LOAD 'data-test.csv' USING PigStorage(',') AS (position:double, track_name, artist, streams, url, date, region);

-- Generate key 
full_songs = FOREACH raw_songs GENERATE CONCAT(artist,'##',track_name) as key, streams;

-- Group songs by key
grouped = GROUP full_songs BY key;

-- Now for streams
stream_mean = FOREACH grouped GENERATE group, AVG(full_songs.streams) as mean_streams;

-- Total "means"
data_grp = GROUP stream_mean ALL;
result = FOREACH data_grp GENERATE SUM(stream_mean.mean_streams) as total_means;

-- Add total streams to mean
pre_stream_ranking = FOREACH stream_mean GENERATE $0, $1, result.total_means;

-- Ranking :D
ranking = FOREACH pre_stream_ranking GENERATE $0 as key, ($1/$2) as ranking;

-- Order
ordered_ranking = ORDER ranking BY ranking DESC;

data_grp = GROUP ordered_ranking ALL;
result = FOREACH data_grp GENERATE SUM(ordered_ranking.ranking) as total;

STORE ordered_ranking INTO 'hdfs://cm:9000/uhadoop2020/3grupo/test_v2/streams';