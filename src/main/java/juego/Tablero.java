package juego;

import estructuras.MiCola;
import estructuras.MiColaEnlazada;
import estructuras.MiLista;
import estructuras.MiListaEnlazada;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Tablero implements Serializable {
    @Expose private Casilla[][] casillas; // Asegurarse de que Casilla es serializable
    @Expose private int filas;
    @Expose private int columnas;

    // Constructor vacío para Gson
    public Tablero() {}

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.casillas = new Casilla[filas][columnas];
        inicializarCasillas();
    }

    private void inicializarCasillas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                casillas[i][j] = new Casilla(i, j);
            }
        }
    }

    public Casilla getCasilla(int fila, int columna) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            return casillas[fila][columna];
        }
        return null;
    }

    public Casilla getCasilla(Posicion posicion) { // Método sobrecargado para Posicion
        return getCasilla(posicion.getX(), posicion.getY());
    }

    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }

    /**
     * Retorna una lista de casillas adyacentes a la casilla dada (arriba, abajo, izquierda, derecha).
     * No filtra por casillas ocupadas, simplemente devuelve las casillas vecinas válidas.
     *
     * @param casilla La casilla de la que se quieren obtener los adyacentes.
     * @return Una MiLista de Casillas adyacentes.
     */
    public MiLista<Casilla> getAdyacentes(Casilla casilla) {
        MiLista<Casilla> adyacentes = new MiListaEnlazada<>();
        int[] dr = {-1, 1, 0, 0}; // Cambios en fila
        int[] dc = {0, 0, -1, 1}; // Cambios en columna

        for (int i = 0; i < 4; i++) {
            int nuevaFila = casilla.getFila() + dr[i];
            int nuevaColumna = casilla.getColumna() + dc[i];

            Casilla vecino = getCasilla(nuevaFila, nuevaColumna);
            if (vecino != null) {
                adyacentes.agregar(vecino);
            }
        }
        return adyacentes;
    }

    /**
     * Realiza un BFS para encontrar todas las casillas alcanzables desde una casilla de inicio
     * en un rango de movimiento dado.
     *
     * @param inicio La casilla de inicio.
     * @param rangoMovimiento El rango máximo de movimiento de la unidad.
     * @return Un mapa de Casilla a su costo de movimiento desde el inicio.
     */
    public Map<Casilla, Integer> getCasillasAlcanzablesBFS(Casilla inicio, int rangoMovimiento) {
        Map<Casilla, Integer> distancias = new HashMap<>(); // Almacena la distancia desde el inicio
        MiCola<Casilla> cola = new MiColaEnlazada<>(); // Cola para BFS

        distancias.put(inicio, 0);
        cola.encolar(inicio);

        while (!cola.estaVacia()) {
            Casilla actual = cola.desencolar();

            MiLista<Casilla> vecinos = getAdyacentes(actual); // Usar el nuevo método getAdyacentes

            for (int i = 0; i < vecinos.tamano(); i++) {
                Casilla vecino = vecinos.obtener(i);

                if (!vecino.estaOcupada()) { // Solo casillas vacías son transitables para el movimiento
                    int nuevaDistancia = distancias.get(actual) + vecino.getCosteMovimiento();

                    if (nuevaDistancia <= rangoMovimiento) {
                        // Si es una ruta más corta o no ha sido visitada aún
                        if (!distancias.containsKey(vecino) || nuevaDistancia < distancias.get(vecino)) {
                            distancias.put(vecino, nuevaDistancia);
                            cola.encolar(vecino);
                        }
                    }
                }
            }
        }
        // Retornar todas las casillas alcanzables incluyendo la de inicio
        return distancias;
    }

    /**
     * Encuentra la ruta más corta entre dos casillas usando BFS.
     *
     * @param inicio La casilla de inicio.
     * @param destino La casilla de destino.
     * @param unidad La unidad que se moverá (para determinar si el destino está ocupado por un aliado o enemigo).
     * @return Una MiLista de Casillas que representa la ruta, o null si no hay ruta o el destino está bloqueado.
     */
    public MiLista<Casilla> getRutaBFS(Casilla inicio, Casilla destino, Unidad unidad) {
        if (inicio == null || destino == null || inicio.equals(destino)) {
            return null; // O una lista vacía si el inicio es el destino
        }

        // Si el destino está ocupado por una unidad que no es la misma unidad que se mueve,
        // y no es un enemigo (si el objetivo es atacarlo), entonces no es una casilla transitable final.
        // Para fines de movimiento, una casilla ocupada por CUALQUIER OTRA unidad es un bloqueo.
        // La IA solo se moverá A UN ESPACIO ADYACENTE AL ENEMIGO, no sobre él.
        if (destino.estaOcupada() && !destino.getUnidadActual().equals(unidad)) {
            // Si la casilla destino está ocupada por otra unidad (aliado o enemigo)
            // no podemos movernos directamente a ella.
            // Esta lógica se podría ajustar si la IA puede "pasar" a través de aliados.
            return null;
        }

        Map<Casilla, Casilla> predecesores = new HashMap<>(); // Para reconstruir la ruta
        MiCola<Casilla> cola = new MiColaEnlazada<>();
        Map<Casilla, Integer> distancias = new HashMap<>(); // Distancia del camino más corto

        distancias.put(inicio, 0);
        cola.encolar(inicio);

        while (!cola.estaVacia()) {
            Casilla actual = cola.desencolar();

            if (actual.equals(destino)) {
                // Ruta encontrada, reconstruir y retornar
                return reconstruirRuta(predecesores, inicio, destino);
            }

            MiLista<Casilla> vecinos = getAdyacentes(actual); // Usar el nuevo método getAdyacentes

            for (int i = 0; i < vecinos.tamano(); i++) {
                Casilla vecino = vecinos.obtener(i);

                // Una casilla es válida si:
                // 1. Existe (ya está manejado por getAdyacentes).
                // 2. No está ocupada, O es la casilla de destino.
                //    La unidad no puede pasar por casillas ocupadas por OTRAS unidades.
                //    Si es el destino y está ocupada por un enemigo, la ruta se considera hasta ahí.
                boolean esTransitable = (!vecino.estaOcupada() || vecino.equals(destino));

                if (esTransitable && !distancias.containsKey(vecino)) {
                    distancias.put(vecino, distancias.get(actual) + vecino.getCosteMovimiento());
                    predecesores.put(vecino, actual);
                    cola.encolar(vecino);
                }
            }
        }
        return null; // No se encontró ruta
    }

    private MiLista<Casilla> reconstruirRuta(Map<Casilla, Casilla> predecesores, Casilla inicio, Casilla destino) {
        MiLista<Casilla> ruta = new MiListaEnlazada<>();
        Casilla actual = destino;

        while (actual != null && !actual.equals(inicio)) {
            ruta.agregarAlPrincipio(actual); // Añadir al principio para tener la ruta en orden
            actual = predecesores.get(actual);
        }
        // No se agrega la casilla de inicio a la ruta, ya que el movimiento es "hacia"
        // y la unidad ya está en la casilla de inicio.
        return ruta;
    }
}