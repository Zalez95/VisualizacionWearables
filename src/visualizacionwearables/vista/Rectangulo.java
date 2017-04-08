package visualizacionwearables.vista;

import java.awt.Color;

/**
 * Rectangulo para dibujar en el panel GraficoPanel
 * 
 * @author  Daniel González Alonso
 * @since   25/08/2016
 * @version 1.0
 */
class Rectangulo {

    /** la posicion inicial del rectangulo a dibujar */
    private int mIniPosition;

    /** la posicion final del rectangulo a dibujar */
    private int mLongitud;

    /** El color del rectangulo a dibujar */
    private final Color mColor;

    /** Construye un nuevo Rectangulo */
    public Rectangulo() {
        mColor = Color.BLACK;
        mIniPosition = 0;
        mLongitud = -1;
    }

    /** Construye un nuevo Rectangulo
     * 
     * @param color el color del rectangulo */
    public Rectangulo(Color color) {
        mColor = color;
        mIniPosition = 0;
        mLongitud = -1;
    }

    /** @return la posicion inicial del rectangulo a dibujar */
    public int getIniPosition() {
        return mIniPosition;
    }

    /** Cambia la posicion inicial en el eje X del rectangulo a dibujar por la
     * posicion dada
     * 
     * @param iniPosition la nueva posicion inicial del cuadrado a dibujar */
    public void setIniPosition(int iniPosition) {
        mIniPosition = iniPosition;
    }

    /** @return la longitud del rectangulo a dibujar */
    public int getLongitud() {
        return mLongitud;
    }

    /** Cambia la posicion final en el eje X del rectangulo a dibujar por la
     * posicion dada
     * 
     * @param longitud la longitud del rectangulo a dibujar */
    public void setLongitud(int longitud) {
        mLongitud = longitud;
    }

    /** @return el color del rectangulo */
    public Color getColor() {
        return mColor;
    }

    /** Elimina los datos almacenados del rectangulo */
    public void reset() {
        mIniPosition = 0;
        mLongitud = -1;
    }
    
}
