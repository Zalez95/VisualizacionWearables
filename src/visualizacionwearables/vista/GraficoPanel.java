package visualizacionwearables.vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import visualizacionwearables.modelo.utils.Punto;
import java.util.Map.Entry;

/**
 * JPanel Modificado para dibujar el grafico de lineas que queremos además de
 * rectangulos en caso de que queramos destacar cierta parte del grafico
 * 
 * @author  Daniel González Alonso
 * @since   27/07/2016
 * @version 1.0
 */
public class GraficoPanel extends JPanel {
    
    private Graphics2D g2d;
    
    /** los puntos a dibujar en el grafico */
    private List<Punto> mPuntos;
    
    /** Si debemos dibujar los puntos del grafico o no */
    private boolean mCirculos;

    /** El tamaño de los circulos a dibujar */
    private final int tamCirculo = 6;
    
    /** Los rectangulos a dibujar sobre el gráfico */
    private List<Rectangulo> mRectangulos;
    
    /** Si debemos mostrar la rejilla o no */
    private boolean mRejilla;
    
    /** Si debemos mostrar las unidades o no */
    private boolean mUnidades;
    
    /** Margenes para las unidades */
    private final int mMargenX = 0;
    private final int mMargenY = 0;
    
    /** Las posiciones de las lineas verticales en el eje X junto al valor a
     * mostrar */
    private List< Entry<Integer, String> > mLineasVerticales;
    
    /** Las posiciones de las lineas horizontal en el eje Y junto al valor a
     * mostrar */
    private List< Entry<Integer, String> > mLineasHorizontales;
    
    
    /** Construye un nuevo grafico por defecto */
    public GraficoPanel() {
        initComponents();
        mPuntos = new ArrayList<>();
        mCirculos = false;
        mRectangulos = new ArrayList<>();
        mRejilla = false;
        mUnidades = false;
        mLineasVerticales = new ArrayList<>();
        mLineasHorizontales = new ArrayList<>();
    }
    
    /** Construye un nuevo grafico
     * 
     * @param circulos si queremos que se muestren los circulos o no
     * @param rejilla si queremos que se muestren lo rejilla o no
     * @param unidades si queremos que se muestren las unidades o no */
    public GraficoPanel(boolean circulos, boolean rejilla, boolean unidades) {
        initComponents();
        mPuntos = new ArrayList<>();
        mCirculos = circulos;
        mRectangulos = new ArrayList<>();
        mRejilla = rejilla;
        mUnidades = unidades;
        mLineasVerticales = new ArrayList<>();
        mLineasHorizontales = new ArrayList<>();
    }
    
    /** @return el ancho del viewport */
    public int getViewportWidth() {
        return getWidth() - mMargenX;
    }
    
    /** @return la altura del viewport */
    public int getViewportHeight() {
        return getHeight() - mMargenY;
    }
    
    /** @return el minimo valor donde se puede dibujar en el viewport en el
     * eje Y */
    public int getMinY() {
        return 0;
    }
    
    /** @return el minimo valor donde se puede dibujar en el viewport en el 
     * eje X */
    public int getMinX() {
        return mMargenX;
    }
    
    /** Cambia los puntos del grafico a dibujar por @param puntos
     * 
     * @param puntos los nuevos puntos del grafico a dibujar */
    public void setPuntos(List<Punto> puntos) {
        mPuntos = puntos;
    }
    
    /** Añade un rectangulo con el color dado a la lista de rectangulos a 
     * dibujar
     * 
     * @param color el color del Rectangulo que queremos dibujar
     * @return el indice del rectangulo creado */
    public int addRectangulo(Color color) {
        mRectangulos.add(new Rectangulo(color));
        return mRectangulos.size() - 1;
    }
    
    /** Devuelve el rectangulo situado en index
     * 
     * @param index el indice del rectangulo que queremos obtener
     * @return el rectangulo situado en index */
    public Rectangulo getRectangulo(int index) {
        assert index >= 0 && index < mRectangulos.size() :
                "No se puede acceder al rectangulo '" + index + '\'';
        
        return mRectangulos.get(index);
    }
    
    /** Muestra los circulos del grafico la proxima vez que se dibuje
     * el panel */
    public void showCirculos() { this.mCirculos = true; }
    
    /** Oculta los circulos del grafico la proxima vez que se dibuje el panel */
    public void hideCirculos() { this.mCirculos = false; }
    
    /** Muestra los circulos del grafico la proxima vez que se dibuje
     * el panel */
    public void showRejilla() { this.mRejilla = true; }
    
    /** Oculta los circulos del grafico la proxima vez que se dibuje el panel */
    public void hideRejilla() { this.mRejilla = false; }
    
    /** Muestra los circulos del grafico la proxima vez que se dibuje
     * el panel */
    public void showUnidades() { this.mUnidades = true; }
    
    /** Oculta los circulos del grafico la proxima vez que se dibuje el panel */
    public void hideUnidades() { this.mUnidades = false; }
    
    /** Cambia los valores de la rejilla por los dados
     * 
     * @param marcas las posiciones de las lineas verticales en el eje X junto
     *      al valor a mostrar */
    public void setLineasVerticales(List< Entry<Integer, String> > marcas) {
        mLineasVerticales = marcas;
    }
    
    /** Cambia los valores de la rejilla por los dados
     * 
     * @param marcas las posiciones de las lineas horizontales en el eje Y junto
     *      al valor a mostrar */
    public void setLineasHorizontales(List< Entry<Integer, String> > marcas) {
        mLineasHorizontales = marcas;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (mRejilla) { drawRejilla(); }
        if (mUnidades) { drawUnidades(); }
        if (mCirculos) { drawGraphWithDots(mPuntos); }
        else { drawGraph(mPuntos); }
        
        for (Rectangulo r : mRectangulos) {
            drawRectangulo(r.getIniPosition(), r.getLongitud(), r.getColor());
        }
    }
    
    /** Dada un array de puntos, dibuja un grafico de lineas en dos dimensiones
     * con las coordenadas de los mismos.
     * 
     * @param puntos los puntos que queremos dibujar en el grafico
     *      (con coordenadas en el rango del eje X [0, mWidth] y del eje Y
     *      [0, mHeight]) */
    private void drawGraph(List<Punto> puntos){
        if (puntos.size() > 1) {
            
            Punto p1 = puntos.get(0), p2 = null;
            for (int i = 1; i < puntos.size(); i++) {
                p2 = puntos.get(i);
                
                if (p2 != null) {
                    g2d.drawLine(
                        (int)Math.round(p1.getX()),
                        (int)Math.round(p1.getY()),
                        (int)Math.round(p2.getX()),
                        (int)Math.round(p2.getY())
                    );
                    p1 = p2;
                }
            }
        }
    }
    
    /** Dada un array de puntos, dibuja un grafico de lineas en dos dimensiones
     * con las coordenadas de los mismos.
     * 
     * @param puntos los puntos que queremos dibujar en el grafico
     *      (con coordenadas en el rango del eje X [0, mWidth] y del eje Y
     *      [0, mHeight]) */
    private void drawGraphWithDots(List<Punto> puntos){
        if (puntos.size() > 1) {
            
            Punto p1 = puntos.get(0), p2 = null;
            for (int i = 1; i < puntos.size(); i++) {
                p2 = puntos.get(i);
                
                if (p2 != null) {
                    g2d.drawLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
                    g2d.drawOval((int)p1.getX() - tamCirculo/2, (int)p1.getY() - tamCirculo/2, tamCirculo, tamCirculo);
                    p1 = p2;
                }
            }
        }
    }

    /** Muestra un rectangulo desde arriba a abajo del panel con la posicion
     * inicial y final en el ejeX especificadas
     * 
     * @param iniPosition la posicion inicial actual en el eje X del grafico
     * @param longitud la posicion final actual en el eje X del grafico
     * @param color el color del rectangulo */
    private void drawRectangulo(int iniPosition, int longitud, Color color) {
        if ((iniPosition >= 0) && (longitud >= 0)) {
            
            g2d.setColor(color);
            g2d.fill(
                new Rectangle2D.Float(iniPosition, 0, longitud, getViewportHeight())
            );
            g2d.setColor(Color.BLACK);
        }
    }
    
    /** Dibuja la rejilla del grafico (grid) a partir de las marcas de las
     * lineas almacenadas */
    private void drawRejilla() {
        Stroke oldStroke = g2d.getStroke();
        g2d.setColor(Color.GRAY);
        g2d.setStroke(
            new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                            new float[] { 4, 4 }, 0)
        );
        
        for (Entry<Integer, String> e : mLineasVerticales) {
            g2d.drawLine(e.getKey(), 0, e.getKey(), getViewportHeight());
        }
        
        for (Entry<Integer, String> e : mLineasHorizontales) {
            g2d.drawLine(mMargenX, e.getKey(), getViewportWidth(), e.getKey());
        }
                
        g2d.setStroke(oldStroke);
        g2d.setColor(Color.BLACK);
    }
    
    /** Dibuja las unidadesde del grafico a partir de las marcas de las lineas 
     * almacenadas */
    private void drawUnidades() {
        for (Entry<Integer, String> e : mLineasVerticales) {
            char[] caracteres = e.getValue().toCharArray();
            g2d.drawChars(caracteres, 0, caracteres.length, e.getKey(), getViewportHeight());
        }
        
        for (Entry<Integer, String> e : mLineasHorizontales) {
            char[] caracteres = e.getValue().toCharArray();
            g2d.drawChars(caracteres, 0, caracteres.length, 0, e.getKey());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
