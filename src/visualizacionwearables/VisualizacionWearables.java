package visualizacionwearables;

import visualizacionwearables.vista.VentanaPrincipalControlador;

/**
 *
 * @author  Daniel González Alonso
 * @since   27/07/2016
 * @version 1.0
 */
public class VisualizacionWearables {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Create and display the form 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaPrincipalControlador c = new VentanaPrincipalControlador();
                } catch(Exception e) { e.printStackTrace(); }
            }
        });
    }
    
}
