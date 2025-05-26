package juego;

import com.google.gson.annotations.Expose;
import estructuras.MiCola; // Necesitas tu implementación de MiCola
import estructuras.MiColaEnlazada; // Y su implementación
import estructuras.MiLista; // Necesitas tu implementación de MiLista
import estructuras.MiListaEnlazada; // Y su implementación

import java.io.Serializable;
import java.util.HashMap; // Para Map
import java.util.Map; // Para Map

public class Tablero implements Serializable {
    @Expose private int filas;
    @Expose private int columnas;
    @Expose private Casilla[][] casillas; // Asegúrate de que Casilla es Serializable y sus atributos tienen @Expose

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    // Constructor vacío para Gson
    public Tablero() {
    }

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

    public boolean estaDentroDeLimites(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    public Casilla getCasilla(int fila, int columna) {
        if (estaDentroDeLimites(fila, columna)) { // Usamos el método existente
            return casillas[fila][columna];
        }
        return null;
    }

    /**
     * Devuelve una lista de casillas adyacentes (vecinos) a la casilla dada.
     * Considera movimientos en cruz (arriba, abajo, izquierda, derecha).
     *
     * @param casilla La casilla de la que se quieren obtener los adyacentes.
     * @return Una MiLista de objetos Casilla que son adyacentes y están dentro de los límites del tablero.
     */
    public MiLista<Casilla> getAdyacentes(Casilla casilla) {
        MiLista<Casilla> adyacentes = new MiListaEnlazada<>();
        int fila = casilla.getFila();
        int columna = casilla.getColumna();

        // Posibles movimientos: arriba, abajo, izquierda, derecha
        int[] dFila = {-1, 1, 0, 0};
        int[] dColumna = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int nuevaFila = fila + dFila[i];
            int nuevaColumna = columna + dColumna[i];

            if (estaDentroDeLimites(nuevaFila, nuevaColumna)) {
                adyacentes.agregar(getCasilla(nuevaFila, nuevaColumna));
            }
        }
        return adyacentes;
    }

    /**
     * Realiza una Búsqueda en Anchura (BFS) para encontrar todas las casillas
     * alcanzables desde una casilla de inicio dentro de un rango de movimiento dado,
     * teniendo en cuenta el coste de movimiento de cada casilla.
     *
     * @param inicio          La casilla desde la que se inicia la búsqueda.
     * @param rangoMovimiento El rango máximo de movimiento de la unidad.
     * @return Un Map donde la clave es la Casilla alcanzable y el valor es el coste total para llegar a ella.
     */
    public Map<Casilla, Integer> getCasillasAlcanzablesBFS(Casilla inicio, int rangoMovimiento) {
        Map<Casilla, Integer> distancia = new HashMap<>(); // Almacena la distancia (coste) desde el inicio
        MiCola<Casilla> cola = new MiColaEnlazada<>();

        distancia.put(inicio, 0); // La distancia a la casilla de inicio es 0
        cola.encolar(inicio);

        while (!cola.estaVacia()) {
            Casilla actual = cola.desencolar();
            int costeActual = distancia.get(actual);

            // Si ya excedemos el rango de movimiento, no seguimos explorando desde esta casilla
            if (costeActual >= rangoMovimiento) {
                continue;
            }

            MiLista<Casilla> adyacentes = getAdyacentes(actual);
            for (Casilla vecino : adyacentes) {
                int nuevoCoste = costeActual + vecino.getCosteMovimiento(); // Suma el coste de la casilla vecina

                // Si el vecino no ha sido visitado o encontramos un camino más corto
                if (!distancia.containsKey(vecino) || nuevoCoste < distancia.get(vecino)) {
                    if (nuevoCoste <= rangoMovimiento) { // Solo si está dentro del rango
                        distancia.put(vecino, nuevoCoste);
                        cola.encolar(vecino);
                    }
                }
            }
        }
        // Excluir la casilla de inicio de las alcanzables si no queremos que aparezca como "movible" a sí misma
        // Puedes decidir si la incluyes o no dependiendo de la UX deseada.
        // distancia.remove(inicio);
        return distancia;
    }

    /**
     * Encuentra la ruta más corta desde una casilla de inicio a una casilla de destino
     * utilizando BFS. Esta versión es útil para la IA y considera la propia unidad.
     * Si el destino está ocupado por la unidad actual, se considera alcanzado.
     * Si está ocupado por otra unidad, la ruta llega hasta la casilla ANTES de la unidad.
     *
     * @param inicio  La casilla de inicio de la unidad.
     * @param destino La casilla objetivo (puede estar ocupada por un enemigo).
     * @param unidad  La unidad que intenta moverse (para comprobar si el destino está ocupado por ella misma).
     * @return Una MiLista de Casillas que representa la ruta más corta (excluyendo el inicio, incluyendo el destino final antes de un enemigo).
     * Retorna null si no hay ruta posible dentro del rango de movimiento de la unidad o si el destino es inaccesible.
     */
    public MiLista<Casilla> getRutaBFS(Casilla inicio, Casilla destino, Unidad unidad) {
        Map<Casilla, Casilla> predecesores = new HashMap<>(); // Para reconstruir el camino
        MiCola<Casilla> cola = new MiColaEnlazada<>();
        Map<Casilla, Integer> distancia = new HashMap<>(); // Distancia (coste) desde el inicio

        cola.encolar(inicio);
        distancia.put(inicio, 0);

        while (!cola.estaVacia()) {
            Casilla actual = cola.desencolar();

            // Si llegamos al destino, podemos intentar reconstruir la ruta
            // Ojo: Si el destino está ocupado por un enemigo, llegamos a la casilla ANTES del enemigo.
            if (actual.equals(destino)) {
                break; // Hemos encontrado el destino, reconstruimos la ruta
            }

            MiLista<Casilla> adyacentes = getAdyacentes(actual);
            for (Casilla vecino : adyacentes) {
                // No permitir pasar por casillas ocupadas por OTRAS unidades
                // A menos que la casilla destino sea el objetivo final y esté ocupada por un enemigo
                boolean esObstaculo = vecino.estaOcupada() && !vecino.getUnidadActual().equals(unidad);

                if (!distancia.containsKey(vecino) && !esObstaculo) { // Si no ha sido visitado y no es un obstáculo
                    distancia.put(vecino, distancia.get(actual) + 1); // Coste simple (número de pasos)
                    predecesores.put(vecino, actual);
                    cola.encolar(vecino);
                }
            }
        }

        // Reconstruir la ruta
        MiLista<Casilla> ruta = new MiListaEnlazada<>();
        Casilla temp = destino;

        // Si el destino es la unidad misma o un obstáculo, el BFS puede que no lo haya visitado
        // Necesitamos encontrar la casilla más cercana al objetivo que sí fue visitada
        // Esto es crucial para que la IA sepa a dónde moverse *antes* de atacar.
        Casilla casillaFinalRuta = destino; // Podría ser el propio destino, o la casilla antes del enemigo
        if (!predecesores.containsKey(destino) && !inicio.equals(destino)) {
            // Si el destino no fue alcanzado directamente por el BFS (ej. está ocupado por un enemigo)
            // Buscamos el predecesor del destino para acercarnos lo máximo posible
            // Esto implica que la ruta terminará *antes* del enemigo.
            // Para esto, podríamos necesitar una lógica más sofisticada, pero para empezar
            // podemos asumir que si el destino no está en predecesores, no hay ruta directa.
            // O, si está ocupado, el BFS no lo habrá añadido si es "esObstaculo".
            // Para la IA, queremos que llegue a la casilla adyacente a un enemigo.
            // Así que, si el destino está ocupado, la ruta debería ir a su predecesor
            // o a la casilla adyacente más cercana que *no* está ocupada.

            // Simplificamos: si no podemos llegar al destino, la ruta es nula
            // La IA deberá buscar el objetivo adyacente más cercano
            return null;
        }

        // Si el destino es la casilla de inicio, la ruta está vacía
        if (inicio.equals(destino)) {
            return new MiListaEnlazada<>();
        }

        // Reconstruir desde el destino hacia el inicio
        while (temp != null && !temp.equals(inicio)) {
            ruta.agregarAlPrincipio(temp); // Agrega al principio para mantener el orden
            temp = predecesores.get(temp);
        }

        // Si la ruta comienza con el inicio (y el inicio no es el destino), quítalo
        if (!ruta.estaVacia() && ruta.obtener(0).equals(inicio)) {
            ruta.eliminar(0); // Eliminar el inicio de la ruta
        }

        return ruta;
    }
}