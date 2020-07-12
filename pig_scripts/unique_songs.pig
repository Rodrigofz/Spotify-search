-- Loads the files
raw_songs = LOAD 'hdfs://cm:9000/uhadoop2020/3grupo/data.csv' USING PigStorage(',') AS (position:double, track_name, artist, streams, url, date, region);
--raw_songs = LOAD 'data-test.csv' USING PigStorage(',') AS (position:double, track_name, artist, streams, url, date, region);

-- Songs
songs = FOREACH raw_songs GENERATE artist, track_name;

-- Unique
unique_songs = DISTINCT songs;

STORE unique_songs INTO 'hdfs://cm:9000/uhadoop2020/3grupo/unique_songs.tsv';