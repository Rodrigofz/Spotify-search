-- Load data
position_ranking = LOAD 'hdfs://cm:9000/uhadoop2020/3grupo/test_v2/positions/part-r-00000' USING PigStorage('\t') AS (key, position_ranking);
stream_ranking = LOAD 'hdfs://cm:9000/uhadoop2020/3grupo/test_v2/streams/part-r-00000' USING PigStorage('\t') AS (key, stream_ranking);
distance_ranking = LOAD 'hdfs://cm:9000/uhadoop2020/3grupo/test_v3/distances_ranking/part-r-00000' USING PigStorage('\t') AS (key, distance_ranking);

joined = JOIN position_ranking by key, stream_ranking by key, distance_ranking by key;

-- v2
-- ranking = FOREACH joined GENERATE position_ranking::key as key, 0.3*position_ranking::position_ranking+0.7*stream_ranking::stream_ranking as ranking;

-- v3
ranking = FOREACH joined GENERATE position_ranking::key as key, 0.1*position_ranking::position_ranking+0.1*stream_ranking::stream_ranking+0.8*distance_ranking::distance_ranking as ranking;

ordered_ranking = ORDER ranking BY ranking DESC;

STORE ordered_ranking INTO 'hdfs://cm:9000/uhadoop2020/3grupo/test_v3/total';