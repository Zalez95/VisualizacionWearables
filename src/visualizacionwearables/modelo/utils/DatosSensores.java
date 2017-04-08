package visualizacionwearables.modelo.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase DatosSensores, se emplea unicamente para almacenar los datos obtenidos
 * del fichero csv en forma de tabla
 * 
 * @author Daniel González Alonso
 * @since   28-07-2016
 * @version 1.0
 */
public class DatosSensores {

    /** nombre de las mediciones */
    private final String name;
    
    /** la columna de la tabla con los tiempos */
    private List<Double> tiempo;
    
    /** Las componentes de los datos de una medicion */
    private List<double[]> datos;

    /** Numero de componentes necesarias para una medicion */
    private final int numDatos;

    /** Construye un nuevo objeto Datos
     * 
     * @param name el nombre de los datos
     * @param numDatos la longitud de los datos */
    public DatosSensores(String name, int numDatos) {

        this.name = name;
        this.numDatos = numDatos;
        tiempo = new ArrayList<>();
        datos = new ArrayList<>();
    }
    
    /** @return el numero de componentes que tiene una medicion
     *      (sin incluir el tiempo)  */
    public int getNumDatos() {
        return numDatos;
    }
    
    
    /** @return el numero de filas que tiene la tabla  */
    public int getNumRows() {
        return tiempo.size();
    }
    
    /** @return the name */
    public String getName() {
        return name;
    }

    /** Introduce una nueva fila de datos en la tabla Datos Sensores
     * 
     * @param tiempo los datos de tipo tiempo que queremos introducir
     * @param datos los datos de que queremos introducir como datos en la
     *      tabla */
    public void addDatos(double tiempo, double[] datos) {
     
        assert datos.length == numDatos :
            "la longitud de los datos no coinciden con la de los datos de la tabla";
        
        this.tiempo.add(tiempo);
        this.datos.add(datos);
    }    
    
    /** Devuelve el tiempo de la fila dada
     * 
     * @param fila la posicion de la fila en la tabla
     * @return el tiempo de la tabla de la fila dada  */
    public Double getTiempo(int fila) {
        
        assert fila >= 0 && fila < tiempo.size() :
            "No se puede acceder a la fila '" + fila + '\'';
        
        return tiempo.get(fila);
    }
    
    /**  Almacena el tiempo dado en la posicion del indice
     * 
     * @param fila el indice de la fila donde queremos cambiar el tiempo
     * @param tiempo el nuevo tiempo a guardar */
    public void setTiempo(int fila, Double tiempo) {
        assert fila >= 0 && fila < this.tiempo.size() :
            "No se puede acceder a la fila '" + fila + '\'';
        
        this.tiempo.set(fila, tiempo);
    }

    /** Devuelve el valor del dato situado en la columna y fila dada de la tabla
     * 
     * @param columna el indice de la columna del datos que queremos
     * @param fila el indice de la fila del datos que queremos
     * @return el dato situados en la columna y fila dada */
    public Double getDatos(int columna, int fila) {
        
        assert columna >= 0 && columna < numDatos :
            "No se pueden obtener los datos de la columna '" + columna + '\'';
        assert fila >= 0 && fila < datos.size() :
            "No se pueden obtener los datos de la fila '" + fila + '\'';
        
        return datos.get(fila)[columna];
    }
    
}
