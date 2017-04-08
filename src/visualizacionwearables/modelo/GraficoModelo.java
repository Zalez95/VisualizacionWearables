package visualizacionwearables.modelo;

import visualizacionwearables.modelo.utils.Punto;
import visualizacionwearables.modelo.utils.DatosSensores;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Clase GraficoModelo, representa el modelo de un Panel de Grafico en el patron
 * MVC, se emplea para obtener y tratar los puntos a dibujar en el grafico
 * 
 * @author  Daniel González Alonso
 * @since   19/08/2016
 * @version 1.0
 */
public class GraficoModelo {
    
    /** Los datos del Modelo */
    private final DatosSensores mDatosSensores;
    
    /** Porcentaje de longitud en el eje X que nos saltaremos */
    private double mOffset;
    
    /** Porcentaje de longitud en el eje X que dibujaremos empezando en offset
     * (cuanto mas pequeño sea, menos datos veremos, es decir, se aumentara en
     * el grafico) */
    private double mZoom;
    
    /** El maximo zoom que se puede hacer en el grafico */
    private final double ZOOM = 0.05;
    
    
    /** Crea un nuevo GraficoModelo a partir del fichero csv de la ruta dada
     * 
     * @param datosSensores los datos a mostrar en el grafico
     * @param offset porcentaje de la longitud del punto más lejano en el eje X
     *      que nos saltaremos queremos ver en el viewport
     * @param zoom porcentaje de la longitud del punto más lejano en el eje X
     *      que queremos ver en el viewport */
    public GraficoModelo(DatosSensores datosSensores, double offset, double zoom) {
        mDatosSensores = datosSensores;
        mZoom = zoom;
        mOffset = offset;
    }
    
    /** @return el numero de columnas necesarias para las componentes de los
     *      datos de las mediciones */
    public int getNumColumnasDatos() {
        return mDatosSensores.getNumDatos();
    }
    
    /** @return el zoom actual (longitud que queremos visualizar en el viewport)
     * en el rango [0.0, 1.0] */
    public double getZoom() {
        return mZoom;
    }
    
    /** @return el offset desde el cual se empiezan a dibujar los puntos
     *      en el rango [0.0, 1.0] */
    public double getOffset() {
        return mOffset;
    }
    
    /** Amplia el zoom del grafico para obtener así menos puntos la proxima vez
     * que llamemos a getPuntos y así ver una porcion mas pequeña del grafico */
    public void acercarCentroViewport() {
        if (mZoom > 2 * ZOOM) {
            mZoom    -= ZOOM;
            mOffset  += ZOOM / 2.0;
        }
    }
    
    /** Amplia el zoom del viewport en la zona seleccionada del eje X para 
     * obtener así menos puntos la proxima vez que llamemos a getPuntos y así 
     * ver una porcion mas pequeña del grafico
     * 
     * @param posInicial la posicion inicial en el eje X del viewport que
     *      queremos ampliar en el rango [0, 1]
     * @param longitud la longitud en el eje X de la zona que queremos ampliar
     *      del viewport en el rango [0, 1] */
    public void ampliarSeleccionViewport(double posInicial, double longitud) {
        assert  posInicial >= 0 && posInicial <= 1
                && longitud >= 0 && longitud <= 1 :
                "parametros exceden los limites [0, 1]";
        
        if (mZoom * longitud > ZOOM / 100) {
            mOffset += mZoom * posInicial;
            mZoom = mZoom * longitud;
        }
    }
    
    /** Reduce el zoom del viewport para obtener así mas puntos la proxima vez
     * que llamemos a getPuntos y así ver una porcion mas grande del grafico */
    public void alejarViewport() {
        if (mZoom < 1) {
            mZoom    += ZOOM;
            mOffset  -= ZOOM / 2.0;
            
            if (mOffset + mZoom > 1) {
                mOffset -= mOffset + mZoom - 1;
            }
            if (mOffset < 0) {
                mZoom += (-mOffset);
                mOffset = 0;
            }
            if (mZoom > 1) {
                if (mOffset - (mZoom - 1) >= 0) {
                    mOffset -= mZoom - 1;
                    mZoom = 1;
                } else {
                    mZoom = 1;
                    mOffset = 0;
                }
            }
        }
    }
    
    /** Desplaza el viewport hacia la derecha o la izquierda
     * 
     * @param desplazamiento el porcentaje de la longitud del grafico a la que 
     *      queremos desplazarnos */
    public void desplazarViewport(double desplazamiento) {
        if (desplazamiento >= 0.0 && desplazamiento <= 1.0) {
            mOffset = desplazamiento;
        }
    }
    
    /** Calcula las marcas y los valores a mostrar en la rejilla del grafico en
     * el eje X (lineas verticales)
     * <br>Nota: El origen de coordenadas en el viewport se encuentra arriba a
     * la izquierda
     * 
     * @param minX el punto minimo donde se puede dibujar en el viewport
     * @param maxX el punto maximo donde se puede dibujar en el viewport
     * @return una lista de pares con el punto donde dibujar la linea vertical
     *      y el valor correspondiente en el grafico */
    public List< Entry<Integer, String> > calculateGridX(int minX, int maxX) {
        List<Punto> puntos  = getPuntos(0);
        List< Entry<Integer, String> > ret = new ArrayList<>();
        
        double  longitudR   = getMaxX(puntos) - getMinX(puntos),
                longitudV   = longitudR * mZoom,
                msToPx      = (maxX - minX) / longitudV,
                pasoMs      = calculaPaso(longitudV, 12),
                offsetIni   = longitudR * mOffset + puntos.get(0).getX(),
                offsetIt    = offsetIni + pasoMs - offsetIni % pasoMs;
        
        while (offsetIt < offsetIni + longitudR * mZoom) {
            Entry<Integer, String> nuevo = new java.util.AbstractMap.SimpleEntry<>(
                minX + (int)Math.round((offsetIt - offsetIni) * msToPx),
                "" + (int)offsetIt
            );
            ret.add(nuevo);
            
            offsetIt += pasoMs;
        }
        
        return ret;
    }
    
    /** Calcula las marcas y los valores a mostrar en la rejilla del grafico en
     * el eje Y (lineas horizontales).
     * <br>Nota: El origen de coordenadas en el viewport se encuentra arriba a
     * la izquierda
     * 
     * @param dato el valor en el eje Y que queremos poner en las marcas
     * @param minY el punto minimo donde se puede dibujar en el viewport
     * @param maxY el punto maximo donde se puede dibujar en el viewport
     * @return una lista de pares con el punto donde dibujar la linea horizontal
     *      y el valor correspondiente en el grafico */
    public List< Entry<Integer, String> > calculateGridY(
            int dato, int minY, int maxY
    ) {
        assert dato >= 0 && dato < mDatosSensores.getNumDatos() :
            " No se puede obtener los datos de la columna '" + dato + '\'';
        
        List<Punto> puntos = getPuntos(dato);
        List< Entry<Integer, String> > ret = new ArrayList<>();
        
        double  curMaxY     = getMaxY(puntos),
                longitud    = curMaxY - getMinY(puntos),
                yToPx       = (maxY - minY) / longitud,
                pasoY       = calculaPaso(longitud, 8),
                offsetY     = curMaxY % pasoY;
        
        while (offsetY < longitud) {
            double valor = curMaxY - offsetY;
            String marca = (valor > 5 || valor < -5)?
                "" + Math.round(valor) : String.format("%1.2f", valor);
            
            Entry<Integer, String> nuevo = new java.util.AbstractMap.SimpleEntry<>(
                minY + (int)Math.round(offsetY * yToPx), marca
            );
            ret.add(nuevo);
            
            offsetY += pasoY;
        }
        
        return ret;
    }
    
    /** Retorna los puntos con los datos del tiempo en el eje X y el datos
     * solicitados en el eje Y, creados a partir de la interpolacion de
     * los datos de los sensores mediante el algoritmo del Vecino mas Cercano.
     * <br>Nota: El origen de coordenadas en el viewport se encuentra arriba a
     * la izquierda
     * 
     * @param dato el dato que queremos almacenar en el eje Y
     * @param minX el minimo valor en el eje X en las coordenadas del grafico
     * @param maxX el minimo valor en el eje X en las coordenadas del grafico
     * @param minY el minimo valor en el eje Y en las coordenadas del grafico
     * @param maxY el minimo valor en el eje Y en las coordenadas del grafico
     * @return una lista con los puntos solicitados */
    public List<Punto> getPuntosNearestNeighbor(
            int dato, int minX, int maxX, int minY, int maxY
    ) {
        List<Punto> ret = new ArrayList<>(),
                    puntos = getPuntos(dato);

        double  longitudX       = puntos.get(puntos.size() - 1).getX()
                                - puntos.get(0).getX(),
                longitudY       = maxY - minY,
                limiteInferior  = longitudX * mOffset + puntos.get(0).getX(),
                limiteSuperior  = limiteInferior + longitudX * mZoom;
        
        // Ajustamos los datos al tamaño del grafico
        scaleY(puntos, minY, maxY);
        puntos = recortaPuntos(puntos, limiteInferior, limiteSuperior);
        scaleX(puntos, minX, maxX);
        
        for (int i = 0; i < maxX; i++) {
            // Obtenemos los puntos dentro del rango [i +- 0.5]
            List<Punto> puntosCercanos = new ArrayList<>();
            for (Punto p : puntos) {
                if (p.getX() < i + 0.5)
                if ((p.getX() > i - 0.5) && (p.getX() < i + 0.5)) {
                    puntosCercanos.add(p);
                }
            }

            // Obtenemos el punto más cercano en el eje X al pixel actual de
            // entre los puntos anteriores
            Punto puntoMasCercano = getPuntoMasCercano(puntosCercanos, i);

            // Si existe, añadimos el punto mas cercano invirtiendo su Y ya que
            // el origen de coordenadas esta arriba a la izquierda
            if (puntoMasCercano != null) {
                ret.add( new Punto( puntoMasCercano.getX(),
                                    longitudY - puntoMasCercano.getY() ) );
            }
        }
        
        if (puntos.size() > 1) {
            ret.add(
                new Punto(  puntos.get(puntos.size()-1).getX(),
                            longitudY - puntos.get(puntos.size()-1).getY() )
            );
        }
        
        return ret;
    }
    
// Funciones privadas: 
    /** @return el paso entre cada linea que hemos de poner en el viewport
     * @param double longitud la longitud entre los valores 
     * @param maxPasos el objetivo de numero de pasos que queremos */
    private double calculaPaso(double longitud, int maxPasos) {
        double  exponente   = Math.round(Math.log10(longitud / maxPasos)),
                magnitud    = Math.pow(10, exponente),
                resto       = longitud / magnitud,
                paso        = magnitud;

        if ((resto > 5) && (longitud / (paso * 10) >= maxPasos / 2)) {
            paso *= 10;
        } else if ((resto > 2) && (longitud / (paso * 5) >= maxPasos / 2)) {
            paso *= 5;
        } else if ((resto > 1) && (longitud / (paso * 2) >= maxPasos /2)) {
            paso *= 2;
        }
        
        return paso;
    }
    
    /** Retorna una Lista con los puntos creados a partir de los datos de los
     * sensores, con los datos del tiempo en el eje X y el datos solicitados en
     * el eje Y
     * 
     * @param dato el dato que queremos almacenar en el eje Y
     * @return un lista con los puntos solicitados */
    private List<Punto> getPuntos(int dato) {
        assert dato >= 0 && dato < mDatosSensores.getNumDatos() :
            " No se puede obtener los datos de la columna '" + dato + '\'';
        
        List<Punto> puntos = new ArrayList<>(mDatosSensores.getNumRows());
        
        for (int i = 0; i < mDatosSensores.getNumRows(); i++) {
            puntos.add(i, new Punto(mDatosSensores.getTiempo(i),
                                    mDatosSensores.getDatos(dato, i)));
        }
        
        return puntos;
    }
    
    /** Devuelve una lista con aquellos puntos de los dados que no excedan los
     * limites en el eje X determinados por el offset y el zoom
     * (los puntos de los extremos si se incluyen)
     * 
     * @param puntos los puntos que queremos recortar
     * @param limiteInferior el valor minimo en el eje X que han de tener los
     *      puntos de la lista de puntos despues de recortarlos
     * @param limiteSuperior el valor maximo en el eje X que han de tener los
     *      puntos de la lista de puntos despues de recortarlos
     * @return la lista de puntos dada sin aquellos puntos que excedan los
     *      limites almacenados en el modelo */
    private List<Punto> recortaPuntos(
            List<Punto> puntos, double limiteInferior, double limiteSuperior
    ) {
        List<Punto> ret = new ArrayList<>();
                
        // Introducimos todos los puntos en la zona incluido el posterior y el
        // anterior
        for (Punto p : puntos) {
            if (p.getX() < limiteInferior) {
                if (ret.isEmpty()) {
                    ret.add(p);
                } else {
                    ret.set(0, p);
                }
            } else {
                ret.add(p);
                if (p.getX() >= limiteSuperior) {
                    break;
                }
            }
        }
        
        // Recortamos los puntos de los extremos
        if (ret.size() > 1) {
            Punto   pIni1 = ret.get(0), 
                    pIni2 = ret.get(1),
                    pInicial = interpolaPunto(limiteInferior, pIni1, pIni2);
            ret.set(0, pInicial);
            
            Punto   pFin1 = ret.get(ret.size() - 2),
                    pFin2 = ret.get(ret.size() - 1),
                    pFinal = interpolaPunto(limiteSuperior, pFin1, pFin2);
            ret.set(ret.size() - 1, pFinal);
        }
        
        return ret;
    }
    
    /** Interpola un punto dado el valor en el eje X del mismo y los puntos que
     * componen la recta donde está el punto
     * 
     * @param valorX la posicion del punto en el eje X
     * @param pAnterior el punto anterior en el eje X al que queremos calcular
     * @param pSiguiente el punto siguiente en el eje X al que queremos calcular */
    private Punto interpolaPunto(double valorX, Punto pAnterior, Punto pSiguiente) {
        double m = (pSiguiente.getY() - pAnterior.getY())
                    / (pSiguiente.getX() - pAnterior.getX());
        
        return new Punto(valorX, m * (valorX - pAnterior.getX()) + pAnterior.getY());
    }
    
    /** De los puntos dados, calcula aquel que se encuentra más cerca en el eje
     * X al valor dado
     * 
     * @param puntos la lista con los puntos que queremos evaluar
     * @param x el valor en el eje X que queremos aproximar
     * @return el punto más cercano, null si la lista estaba vacía */
    private Punto getPuntoMasCercano(List<Punto> puntos, double x) {
        
        Punto puntoMasCercano = null;
        double masCercanoDif = Double.MAX_VALUE;
        
        for (Punto p : puntos) {
            double curDif = Math.abs(x - p.getX());

            if (puntoMasCercano == null) {
                puntoMasCercano = p;
                masCercanoDif = curDif;
            } else {
                if (curDif < masCercanoDif) {
                    masCercanoDif = curDif;
                    puntoMasCercano = p;
                }
            }
        }
        
        return puntoMasCercano;
    }
    
    /** Transforma las coordenadas de los puntos de la lista dada en el rango
     * [minX, maxX]
     * 
     * @param puntos los puntos que queremos transformar
     * @param minX el minimo valor en el eje X
     * @param maxX el minimo valor en el eje X */
    private void scaleX(List<Punto> puntos, double minX, double maxX) {
        double  curMinX = getMinX(puntos),
                curMaxX = getMaxX(puntos);
        
        for (Punto punto : puntos) {
            if (punto != null) {
                punto.scaleX(curMinX, curMaxX, minX, maxX);
            }
        }
    }
    
    /** Transforma las coordenadas de los puntos de la lista dada en el rango
     * [minY, maxY]
     * 
     * @param puntos los puntos que queremos transformar
     * @param minY el minimo valor en el eje Y
     * @param maxY el minimo valor en el eje Y */
    private void scaleY(List<Punto> puntos, double minY, double maxY) {
        double  curMinY = getMinY(puntos),
                curMaxY = getMaxY(puntos);
        
        for (Punto punto : puntos) {
            if (punto != null) {
                punto.scaleY(curMinY, curMaxY, minY, maxY);
            }
        }
    }
    
    /** @param puntos lista con los puntos
     * @return el minimo valor existente en la dimension X */
    private double getMinX(List<Punto> puntos) {
        double min = Double.MAX_VALUE;
            
        for (Punto punto : puntos) {
            double curX = punto.getX();
            if (curX < min) { min = curX; }
        }
        
        return min;
    }
    
    /** @param puntos lista con los puntos
     * @return el maximo valor existente en la dimension X */
    private double getMaxX(List<Punto> puntos) {
        double max = Double.MIN_VALUE;
            
        for (Punto punto : puntos) {
            double curX = punto.getX();
            if (curX > max) { max = curX; }
        }
        
        return max;
    }
    
    /** @param puntos lista con los puntos
     * @return el minimo valor existente en la dimension Y */
    private double getMinY(List<Punto> puntos) {
        double min = Double.MAX_VALUE;
            
        for (Punto punto : puntos) {
            double curY = punto.getY();
            if (curY < min) { min = curY; }
        }
        
        return min;
    }
    
    /** @param puntos lista con los puntos
     * @return el maximo valor existente en la dimension Y */
    private double getMaxY(List<Punto> puntos) {
        double max = Double.MIN_VALUE;
            
        for (Punto punto : puntos) {
            double curY = punto.getY();
            if (curY > max) { max = curY; }
        }
        
        return max;
    }
    
}
