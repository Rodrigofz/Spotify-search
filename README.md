# Proyecto Procesamiento Masivo de Datos (Otoño 2020)

- Roberto Aguilera
- Javiera D. Mizunuma
- Rodrigo Fuentes

## Que haremos

La idea del proyecto es descubrir cómo varían las canciones más populares a través del mundo, tomando en cuenta las posiciones en el TOP 200 de los rankings de Spotify, la cantidad de reproducciones totales de cada canción en todos los paises donde es escuchada y las distancia de los paises donde estas son reproducidas a uno de referencia.

Analizando esta información esperamos averiguar si existen correlaciones entre el lugar geográfico y la música que más se escucha o si la globalización ha avanzado tanto que lo más popular se ha independizado de la cultura de cada país.

Para esto, se usará los [siguientes datos](./data.zip). Estos están pre procesados para limpiarlos. Los datos sin procesar están [aquí](./raw_data.zip)

Se pretenden seguir lo siguientes pasos:

- Diseñar y calcular un ranking de importancia de las canciones, basado tanto en las posiciones alcanzadas por la canción, como en la cantidad de reproducciones que esta tuvo y las distancias a un pais de referencia.

- Utilizar dicho ranking para crear un índice sobre las canciones del dataset.

- Crear un buscador mediante elastic-search que utilice el indice para entregar resultados acorde al pais de referencia, de tal manera que las canciones más importantes aparezcan primero.
