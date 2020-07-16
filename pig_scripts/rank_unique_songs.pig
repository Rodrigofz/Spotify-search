-- Unique songs
unique_songs = LOAD 'hdfs://cm:9000/uhadoop2020/3grupo/unique_songs.tsv' USING PigStorage('\t') AS (artist, song);
-- Ranking
rankings = LOAD 'hdfs://cm:9000/uhadoop2020/3grupo/proyecto_final/total/$country/part-r-00000' USING PigStorage('\t') AS (key, ranking:double);

-- Generate keys for unique songs
unique_keys = FOREACH unique_songs GENERATE CONCAT(artist,'##',song) as key, artist, song;

-- Join songs and rankings
joined = FOREACH (JOIN unique_keys BY key, rankings BY key) GENERATE unique_keys::artist as artist, unique_keys::song as song, rankings::ranking as ranking;

-- Order
ordered = ORDER joined BY ranking DESC;

-- Save
STORE ordered INTO 'hdfs://cm:9000/uhadoop2020/3grupo/proyecto_final/rank_unique_songs/$country';



