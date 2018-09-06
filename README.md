# visualizacion-wearables
Programa de visualización de datos para mi asignatura de Prácticas en Empresa en la Universidad de Valladolid.

## Ejemplo de uso
![ejemplo_visualizacion_wearables](https://user-images.githubusercontent.com/12982523/45187070-beda8880-b22f-11e8-86c8-19b8aeaf3945.gif)

## Formato de datos
Los datos que es capaz de leer este programa siguen el siguiente formato CSV separado por `;`:

| Timestamp | dato1    | dato2    | dato3    |
| --------- | -------- | -------- | -------- |
| 0.0       | 1.105957 | 0.170654 | 0.129395 |
| 100.0     | 0.883789 | 0.099121 | 0.048828 |
| 26.0      | 0.844482 | 0.18335  | 0.125977 |

> Nota: El tiempo viene almacenado de forma relativa al valor de la fila anterior. El número de columnas de datos debe ser entre 1 y 3.
