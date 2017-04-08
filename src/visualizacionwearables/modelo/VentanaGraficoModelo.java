package visualizacionwearables.modelo;

import visualizacionwearables.modelo.utils.LectorCSV;
import visualizacionwearables.modelo.utils.DatosSensores;
import java.io.File;
import java.io.IOException;
import visualizacionwearables.modelo.utils.LectorCSV.DatosFormatException;

/**
 * Clase VentanaGraficoModelo, representa el modelo de una ventana de Grafico 
 * en el patron MVC, se emplea para almacenar los datos relativos a la vista
 * así como acceder a éstos
 * 
 * @author  Daniel González Alonso
 * @since   27/07/2016
 * @version 1.0
 */
public class VentanaGraficoModelo {
    
    /** El archivo CSV con los datos */
    private final File mArchivo;
    
    /** El dato seleccionado */
    private int mDatoSeleccionado;
    
    /** si el grafico superior ha sido ocultado por el usuario o no */
    private boolean mGraficoSuperiorOculto;
    
    /** El minimo tamaño de ventana para ocultar el panelInferior */
    private final int mLimitePanelSuperior = 320;
    
    
    /**
     * Crea un nuevo GraficoModelo a partir del fichero csv de la ruta dada
     * 
     * @param archivo el archivo donde se encuentra el fichero csv con los datos
     */
    public VentanaGraficoModelo(File archivo) {
        mArchivo = archivo;
        mDatoSeleccionado = -1;
        mGraficoSuperiorOculto = false;
    }
    
    /**
     * @return el titulo de la ventana del grafico
     */
    public String getTitulo() {
        return mArchivo.getName();
    }
    
    /**
     * Lee el archivo y retorna su contenido
     * 
     * @return el contenido del archivo
     * @throws java.io.IOException
     * @throws visualizacionwearables.modelo.utils.LectorCSV.DatosFormatException
     */
    public DatosSensores lee() throws IOException, DatosFormatException {
        LectorCSV lector = new LectorCSV();
        return lector.leeArchivo(mArchivo);
    }

    /**
     * @return the datoSeleccionado
     */
    public int getDatoSeleccionado() {
        return mDatoSeleccionado;
    }

    /**
     * @param datoSeleccionado the datoSeleccionado to set
     */
    public void setDatoSeleccionado(int datoSeleccionado) {
        mDatoSeleccionado = datoSeleccionado;
    }

    /**
     * @return the mGraficoSuperior
     */
    public boolean isGraficoSuperiorOculto() {
        return mGraficoSuperiorOculto;
    }

    /**
     * @param graficoSuperior the mGraficoSuperior to set
     */
    public void setGraficoSuperiorOculto(boolean graficoSuperior) {
        mGraficoSuperiorOculto = graficoSuperior;
    }

    /**
     * @return the mLimitePanelSuperior
     */
    public int getLimitePanelSuperior() {
        return mLimitePanelSuperior;
    }

}
