# Proyecto Procesamiento Masivo de Datos (Otoño 2020)

- Roberto Aguilera
- Javiera D. Mizunuma
- Rodrigo Fuentes

## Que haremos

La idea del proyecto es hacer un buscador que permita visualizar el tiempo y los países que una canción se mantuvo en el top 200 de Spotify.

Para esto, se usará los [siguientes datos](./data.csv). Estos están pre procesados para limpiarlos. Los datos sin procesar están [aquí](./raw_data.zip)

Se pretenden seguir lo siguientes pasos:

- Diseñar y calcular un ranking de importancia de las canciones, basado tanto en las posiciones alcanzadas por la canción, como en la cantidad de reproducciones que esta tuvo.

- Utilizar dicho ranking para crear un índice sobre las canciones del dataset.

- Crear una aplicación externa que, consultando a dicho índice, muestre el periodo de tiempo y los países donde una canción buscada estuvo en el top 200.
