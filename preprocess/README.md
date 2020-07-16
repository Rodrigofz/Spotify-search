# Preprocesamiento

## Map

Construye un mapa donde marca los países que se encuentran en el dataset.

> python3 [map.py](./map.py) {input_path} {output_path}

El output se encuentra en [map.html](./map.html)

![](map.png)

## Clean commas

Limpia las comas que no permiten procesar bien los datos. Algunas canciones poseían comas en los nombres o artistas, lo que no permitía trabajar los datos correctamente al ser un csv.

> python3 [clean_commas.py](./clean_commas.py) {input_path} {output_path}

## Delete globals

Borra las canciones pertenecientes al ranking global, ya que no nos interesaban para nuestros propósitos.

> python3 [delete_globals.py](./delete_globals.py) {input_path} {output_path}