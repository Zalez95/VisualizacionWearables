package visualizacionwearables.modelo.utils;

/**
 * Clase Punto, representa un punto con coordenadas en dos dimensiones: X, Y
 * 
 * @author  Daniel González Alonso
 * @since   27/07/2016
 * @version 1.0
 */
public class Punto {

    private double x;   /** La posicion del punto en el eje X */
    private double y;   /** La posicion del punto en el eje Y */

    /** Construye un nuevo Punto
     * 
     * @param x el valor de la coordenada x
     * @param y el valor de la coordenada y */
    public Punto(double x, double y) {

        this.x = x;
        this.y = y;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Ajusta el valor de X del punto al rango [newMinimo, newMaximo]
     * a partir de los maximos y minimos pasados
     * 
     * @param curMinimo el minimo valor actual en el eje X
     * @param curMaximo el maximo valor actual en el eje X
     * @param newMinimo el minimo valor del nuevo rango en el eje X
     * @param newMaximo el maximo valor del nuevo rango en el eje X
     */
    public void scaleX(double curMinimo, double curMaximo,
            double newMinimo, double newMaximo) {

        x = (x - curMinimo) / (curMaximo - curMinimo);
        x = (newMaximo - newMinimo) * x + newMinimo;
    }

    /**
     * Ajusta el valor de Y del punto al rango [newMinimo, newMaximo]
     * a partir de los maximos y minimos pasados
     * 
     * @param curMinimo el minimo valor actual en el eje Y
     * @param curMaximo el maximo valor actual en el eje Y
     * @param newMinimo el minimo valor del nuevo rango en el eje Y
     * @param newMaximo el maximo valor del nuevo rango en el eje Y
     */
    public void scaleY(double curMinimo, double curMaximo,
            double newMinimo, double newMaximo) {

        y = (y - curMinimo) / (curMaximo - curMinimo);
        y = (newMaximo - newMinimo) * y + newMinimo;
    }

}
