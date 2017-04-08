package visualizacionwearables.vista;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import visualizacionwearables.modelo.utils.LectorCSV;
import visualizacionwearables.modelo.VentanaGraficoModelo;

/**
 * Clase VentanaPrincipalControlador, siguiendo el patron MVC, esta clase 
 * representa el controlador de la Ventana Principal.
 * 
 * Esta clase se encarga de validar los datos de la vista y con ellos actualizar
 * el modelo, y posiblemente actualizar la vista con estos nuevos datos
 * 
 * @author  Daniel González Alonso
 * @since   27/07/2016
 * @version 1.0
 */
public class VentanaPrincipalControlador {
    
    private final VentanaPrincipalVista miVista;
    
    /**
     * Constructor de VentanaPrincipalControlador, crea una nueva 
     * Ventana Principal
     */
    public VentanaPrincipalControlador(/* modelo */) {
        
        miVista = new VentanaPrincipalVista(this);
        miVista.setVisible(true);
    }
    
    public void onSelectedFile(final File file) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    VentanaGraficoModelo modelo = new VentanaGraficoModelo(file);
                    VentanaGraficoControlador controlador = new VentanaGraficoControlador(modelo);
                    
                    miVista.addInternalFrame(controlador.getVista());
                } catch (IOException | LectorCSV.DatosFormatException e) {
                    JOptionPane.showMessageDialog(
                        miVista,
                        "Error al abrir el archivo \"" + file.getName() + '\"'
                    );
                }
            }
        });
    }
    
    /** Funcion que implementa la division de las vertanas internas
     * horizontalmente */
    public void onSplit() {
        JInternalFrame[] frames = miVista.getInternalFrames();
        
        if (frames.length > 0) {
            int frameWidth  = miVista.getInternaleFramesPanelWidth(),
                frameHeight = miVista.getInternaleFramesPanelHeight() / frames.length;
            
            try {
                for (int i = 0; i < frames.length; i++) {    
                    if (frames[i].isMaximum()) { frames[i].setMaximum(false); }
                    frames[i].setBounds(0, frameHeight * i, frameWidth, frameHeight);
                }
            } catch (PropertyVetoException e) {
                JOptionPane.showMessageDialog(
                    miVista,
                    "Error al minimizar el tamaño de las ventanas"
                );
            }
        }
    }
}
