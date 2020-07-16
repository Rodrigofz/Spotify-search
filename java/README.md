# Elastic Search

Scripts utilizados para crear índices sobre canciones y realizar búsquedas. Basados en los scripts del lab 7 del curso. Funcionan al agregarlos en el código fuente de este lab.

## Construir índice

> java -jar mdp-lab07.jar BuildSpotifyIndexBulk -i {data_path} -o {index_name}

## Buscar sin ranking

> java -jar mdp-lab07.jar SearchSpotifyIndex -i {index_name}

## Buscar con ranking

> java -jar mdp-lab07.jar SearchSpotifyIndexWithRank -i {index_name}
