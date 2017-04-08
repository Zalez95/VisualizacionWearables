package visualizacionwearables.vista;

import java.util.List;
import java.io.IOException;
import visualizacionwearables.modelo.VentanaGraficoModelo;
import visualizacionwearables.modelo.GraficoModelo;
import visualizacionwearables.modelo.utils.DatosSensores;
import visualizacionwearables.modelo.utils.LectorCSV;
import visualizacionwearables.modelo.utils.Punto;

/**
 * Clase VentanaGraficoControlador, siguiendo el patron MVC, esta clase 
 * representa el controlador de la Ventana de Grafico.
 * 
 * Esta clase se encarga de validar los datos de la vista y actualizarla con
 * los datos del modelo
 * 
 * @author  Daniel González Alonso
 * @since   27/07/2016
 * @version 1.0
 */
public class VentanaGraficoControlador {
    
    private final VentanaGraficoVista   miVista;
    private final VentanaGraficoModelo  miModelo;
    private final GraficoModelo         modeloGraficoSuperior,
                                        modeloGraficoInferior;
    
    /** Crea un nuevo VentanaGraficoControlador
     * 
     * @param modelo el modelo del patron MVC
     * @throws java.io.IOException
     * @throws visualizacionwearables.modelo.utils.LectorCSV.DatosFormatException */
    public VentanaGraficoControlador(VentanaGraficoModelo modelo)
            throws IOException, LectorCSV.DatosFormatException {
        
        this.miModelo = modelo;        
        // Creamos los modelos de los paneles de graficos
        DatosSensores datos = miModelo.lee();
        modeloGraficoSuperior = new GraficoModelo(datos, 0.0, 1.0);
        modeloGraficoInferior = new GraficoModelo(datos, 0.0, 0.5);
        
        // Creamos la vista
        miVista = new VentanaGraficoVista(this);
        
        // Añadimos el titulo a la ventana de la vista
        miVista.setTitle(miModelo.getTitulo());
        
        // Añadimos las opciones al combobox de datos de la ventana de la vista
        for (int i = 0; i < datos.getNumDatos(); i++) {
            switch (i)
            {
            case 0:
                miVista.addOpcionSeleccionDato("X");
                break;
            case 1:
                miVista.addOpcionSeleccionDato("Y");
                break;
            case 2:
                miVista.addOpcionSeleccionDato("Z");
                break;
            }
        }
        
        // Actualizamos la vista
        miVista.repaint();
    }
    
    /** @return la vista del controlador */
    public VentanaGraficoVista getVista() {
        return miVista;
    }
    
    /** Metodo que se encarga de actualizar los grafico de la vista con los
     * datos del modelo */
    public void onPaint() {
        // Actualización de los gráficos
        updateGrafico(miVista.getGraficoSuperior(), modeloGraficoSuperior);
        updateGrafico(miVista.getGraficoInferior(), modeloGraficoInferior);
    }
    
    /** Metodo que implementa la funcionalidad del boton Ampliar de la vista
     * <br>El metodo amplia la zona seleccionada en el grafico inferior, si no
     * se ampliar el centro de la zona visible del grafico */
    public void onAmpliar() {
        // 1. Obtenemos los datos de la vista
        GraficoPanelInteraccion graficoInferior = miVista.getGraficoInferior();
        double  width = graficoInferior.getWidth(),
                rectIni = graficoInferior.getSeleccion().getIniPosition() / width,
                rectLen = graficoInferior.getSeleccion().getLongitud() / width;
        
        // 2. Actualizamos el modelo
        if (rectIni >= 0 && rectLen >= 0) {
            modeloGraficoInferior.ampliarSeleccionViewport(rectIni, rectLen);
        } else {
            modeloGraficoInferior.acercarCentroViewport();
        }
        
        // 3. Actualizamos la vista
        desplazaGrafico(modeloGraficoInferior.getOffset());
        escalaGrafico(modeloGraficoInferior.getZoom());
        desplazaGrafico(modeloGraficoInferior.getOffset());
        miVista.repaint();
    }
    
    /** Metodo que implementa la funcionalidad del boton Reducir de la vista */
    public void onReducir() {
        // 1. Actualizamos el modelo
        modeloGraficoInferior.alejarViewport();
        
        // 2. Actualizamos la vista
        desplazaGrafico(modeloGraficoInferior.getOffset());
        escalaGrafico(modeloGraficoInferior.getZoom());
        desplazaGrafico(modeloGraficoInferior.getOffset());
        miVista.repaint();
    }
    
    /** Metodo que implementa la funcionalidad de la barra de scroll, sirve
     * para desplazarnos por el gráfico */
    public void onScroll() {
        // 1. Obtenemos los datos de la vista
        double posicion = miVista.getValueScrollbar()
                        / (double)miVista.getMaxValueScrollBar();
        
        // 2. Actualizamos el modelo
        modeloGraficoInferior.desplazarViewport(posicion);

        // 3. Actualizamos la vista
        desplazaGrafico(modeloGraficoInferior.getOffset());
        escalaGrafico(modeloGraficoInferior.getZoom());
        desplazaGrafico(modeloGraficoInferior.getOffset());
        miVista.repaint();
    }

    /** Metodo que se encarga de activar o desactivar la visualizacion de los
     * puntos en el gráfico */
    public void onPuntosCheckbox() {
        if (miVista.isPuntosCheckboxSeleccionado()) {
            miVista.getGraficoInferior().showCirculos();
        } else {
            miVista.getGraficoInferior().hideCirculos();
        }
        
        // Actualizamos la vista
        miVista.repaint();
    }
    
    /** Metodo que se encarga de guardar en el modelo de la Ventana el dato
     * seleccionado en la vista */
    void onDatoSeleccionado() {
        // 1. Obtenemos los datos de la vista
        int datoSeleccionado = miVista.getSeleccionComboBox();
        
        // 2. Validamos los datos de la vista
        if ((datoSeleccionado >= 0)
            && (datoSeleccionado < modeloGraficoSuperior.getNumColumnasDatos())) {
            
            // 3. Actualizamos el modelo
            miModelo.setDatoSeleccionado(datoSeleccionado);
            
            // 4. Actualizamos la vista
            desplazaGrafico(modeloGraficoInferior.getOffset());
            escalaGrafico(modeloGraficoInferior.getZoom());
            desplazaGrafico(modeloGraficoInferior.getOffset());
            miVista.repaint();
        }
    }

    /** Metodo que se encarga de activar o desactivar la visualizacion del 
     * panel superior de la ventana */
    void onPanelSuperiorCheckbox() {
        // 1. Actualizamos los datos del modelo
        miModelo.setGraficoSuperiorOculto(!miModelo.isGraficoSuperiorOculto());
        
        // 2. Actualizamos la vista
        miVista.ocultarPanelSuperior(miModelo.isGraficoSuperiorOculto());
    }
    
    /** Metodo que se encarga de actualizar la vista cuando cambia de tamaño */
    void onResize() {
        // 1. Obtenemos los datos de la vista
        int height = miVista.getHeight();
        
        // 2. Actualizamos la vista con los datos del modelo
        if (height < miModelo.getLimitePanelSuperior()) {
            miVista.ocultarPanelSuperior(true);
        } else {
            miVista.ocultarPanelSuperior(miModelo.isGraficoSuperiorOculto());
        }
        miVista.repaint();
    }
    
// Funciones privadas
    /** Metodo que se encarga de actualizar los grafico de la vista con los
     * datos del modelo
     * 
     * @param panel el panel que queremos actualizar
     * @param modelo el modelo del panel que queremos actualizar */
    private void updateGrafico(GraficoPanel panel, GraficoModelo modelo) {
        // 1. Obtenemos los datos de la vista
        int minimoX = panel.getMinX(),
            maximoX = panel.getViewportWidth(),
            minimoY = panel.getMinY(),
            maximoY = panel.getViewportHeight();
        int datoSeleccionado = miModelo.getDatoSeleccionado();
        
        // 2. Validamos los datos
        boolean datosOk = false;
        
        if ((datoSeleccionado >= 0)
            && (datoSeleccionado < modelo.getNumColumnasDatos())) {
            datosOk = true;
        }
        
        if (datosOk) {
            // 3. Obtenemos los datos del modelo
            List<Punto> puntos = modelo.getPuntosNearestNeighbor(datoSeleccionado, minimoX, maximoX, minimoY, maximoY);
            
            // 4. Actualizamos los datos de la vista
            panel.setLineasVerticales(modelo.calculateGridX(minimoX, maximoX));
            panel.setLineasHorizontales(modelo.calculateGridY(datoSeleccionado, minimoY, maximoY));
            panel.setPuntos(puntos);
            panel.repaint();
        }
    }
    
    /** Cambia la posicion de la barra de scroll y del rectangulo del grafico 
     * superior de forma proporcional a su tamaño actual
     * 
     * @param nuevoTam el porcentaje de tamaño que ha de ocupar la barra de
     *      scroll */
    private void escalaGrafico(double nuevoTam) {
        
        miVista.escalaScrollbar(
            (int)(miVista.getMaxValueScrollBar() * nuevoTam)
        );
        
        miVista.getRectanguloMapa().setLongitud(
            (int)(nuevoTam * miVista.getGraficoSuperior().getWidth())
        );
        
        miVista.getGraficoInferior().getSeleccion().reset();
    }
    
    /** Cambia la posicion de la barra de scroll y del rectangulo del grafico 
     * superior de forma proporcional a su tamaño actual
     * 
     * @param nuevoDesp el porcentaje de la longitud donde se ha de colocar la 
     *      barra de scroll */
    private void desplazaGrafico(double nuevoDesp) {
        
        miVista.desplazaScrollbar(
            (int)(miVista.getMaxValueScrollBar() * nuevoDesp)
        );
        
        miVista.getRectanguloMapa().setIniPosition(
            (int)(nuevoDesp * miVista.getGraficoSuperior().getWidth())
        );
    }
    
}
