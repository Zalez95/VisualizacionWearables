package visualizacionwearables.modelo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase LectorCSV
 * Se emplea unicamente para cargar y traducir el archivo csv indicado
 * 
 * @author  Daniel González Alonso
 * @since   25-07-2016
 * @version 1.0
 */
public class LectorCSV {
    
    /** Excepcion por si hay un fallo en el formato de el archivo indicado */
    public class DatosFormatException extends Exception {
        
        public DatosFormatException() { super(); }
        public DatosFormatException(String message) { super(message); }
        public DatosFormatException(String message, Throwable cause) { super(message, cause); }
        public DatosFormatException(Throwable cause) { super(cause); }
    }
    
    /** Dado la ruta del archivo csv con los datos que queremos leer, devuelve
     * los datos del fichero en forma de objeto de tipo DatosSensores
     * 
     * @param archivo el archivo CSV que queremos leer
     * @return los datos del fichero traducidos a objeto DatosSensores */
    public DatosSensores leeArchivo(File archivo)
            throws IOException, DatosFormatException {
        
        // Obtenemos las lineas del archivo
        ArrayList<String> lineas = getLineas(archivo);
        
        // Traducimos las lineas a objetos de tipo Datos
        DatosSensores datosSens = parseDatos(lineas, archivo.getName());
        
        // Calculamos el tiempo absoluto
        setTiempoAbsoluto(datosSens);
        
        return datosSens;
    }
    
    /** Dado un archivo, devuelve una lista con las lineas del mismo
     *
     * @param archivo el archivo a leer 
     * @return una lista con las lineas del archivo */
    private ArrayList<String> getLineas(File archivo) throws IOException {
        
        ArrayList<String> lineas = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
               lineas.add(linea);
            }
        }
        
        return lineas;
    }
    
    /**
     * Transforma el tiempo relativo almacenado en tiemposList (la diferencia de 
     * tiempo entre dos mediciones  consecutivas, a excepcion del primer dato)
     * por el tiempo absoluto (diferencia de tiempo con el primer dato)
     * 
     * @param tiemposList la lista con los datos uyas fechas queremos transformar
     */
    private void setTiempoAbsoluto(DatosSensores datosSens) {
        
        // Solo si tenemos mas de un dato transformaremos los tiempos ya que el
        // tiempo del primer dato ya es absoluto
        if (datosSens.getNumRows() > 1) {
            
            // El primer dato no ha de perder la diferencia absoluta
            double lastTiempo = datosSens.getTiempo(0);

            for (int i = 1; i < datosSens.getNumRows(); i++) {
                double curTiempo = datosSens.getTiempo(i) + lastTiempo;
                datosSens.setTiempo(i, curTiempo);
                lastTiempo = curTiempo;
            }
        }
    }
    
    /**
     * Traduce la lista pasada con las filas de la tabla a un objeto de tipo
     * DatosSensores
     * 
     * @note los datos de las filas han de estar separadas por ';'
     * @param lineas la lista con las filas de la tabla que queremos traducir
     * @param filename el nombre del archivo que cuyos datos queremos traducir
     * @return los Datos traducidos
     */
    private DatosSensores parseDatos(ArrayList<String> lineas, String filename)
            throws DatosFormatException {
        
        if (lineas.size() < 1) { throw(new DatosFormatException()); }
        
        // Obtenemos el numero de columnas necesarias para datos
        int lengthData = lineas.get(1).split(";").length - 1;
        DatosSensores ret = new DatosSensores(filename, lengthData);
        
        for (int i = 1 ; i < lineas.size(); i++) {  // Saltamos la cabecera
            // Dividimos los datos
            String[] datosString = lineas.get(i).split(";");
            
            // Traducimos y añadimos los datos
            switch (datosString.length) {
                case 2:
                    try {
                        ret.addDatos(
                            Double.parseDouble(datosString[0]),
                            new double[]{
                                Double.parseDouble(datosString[1])
                            } );
                    } catch (NumberFormatException e) {
                        throw new DatosFormatException();
                    }
                    break;
                case 4:
                    try {
                        ret.addDatos(
                            Double.parseDouble(datosString[0]),
                            new double[]{
                                Double.parseDouble(datosString[1]),
                                Double.parseDouble(datosString[2]),
                                Double.parseDouble(datosString[3])
                            } );
                    } catch (NumberFormatException e) {
                        throw new DatosFormatException();
                    }
                    break;
                default:
                    throw new DatosFormatException();
            }

        }
        
        return ret;
    }
    
}
