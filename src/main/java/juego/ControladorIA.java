package juego;

import estructuras.MiLista;
import com.google.gson.annotations.Expose; // Podría no ser necesario si no se serializa, pero es buena práctica para campos

import java.io.Serializable;
import java.util.Map; // Para el Map de BFS

public class ControladorIA implements Serializable { // La IA podría no necesitar ser serializable
    @Expose private Partida partida; // Referencia a la partida para operar sobre ella
    @Expose private Jugador jugadorIA; // Referencia al jugador que controla la IA

    // Constructor vacío para Gson (si se serializa)
    public ControladorIA() {}

    public ControladorIA(Partida partida, Jugador jugadorIA) {
        this.partida = partida;
        this.jugadorIA = jugadorIA;
    }

    public void realizarTurnoIA() {
        System.out.println("IA (" + jugadorIA.getNombre() + ") está realizando su turno...");

        MiLista<Unidad> misUnidades = jugadorIA.getUnidades();
        for (Unidad unidadIA : misUnidades) { // Usando el for-each con Iterable
            if (!unidadIA.estaViva()) { // Asegurarse de que la unidad esté viva
                continue;
            }
            // Lógica de la IA: Intentar atacar primero, si no, moverse hacia un enemigo
            if (!intentarAtacarEnemigoCercano(unidadIA)) {
                moverHaciaEnemigoCercano(unidadIA);
            }
        }
        System.out.println("IA (" + jugadorIA.getNombre() + ") ha terminado su turno.");
    }

    private boolean intentarAtacarEnemigoCercano(Unidad unidadIA) {
        Jugador oponente = (partida.getJugador1() == jugadorIA) ? partida.getJugador2() : partida.getJugador1();
        MiLista<Unidad> unidadesOponente = oponente.getUnidades();

        if (unidadesOponente.estaVacia()) {
            return false;
        }

        Unidad enemigoObjetivo = null;
        int distanciaMinima = Integer.MAX_VALUE;

        for (Unidad enemigo : unidadesOponente) {
            if (!enemigo.estaViva()) { // Solo considerar enemigos vivos
                continue;
            }
            int distancia = unidadIA.getPosicion().distanciaA(enemigo.getPosicion());
            if (distancia <= unidadIA.getRangoAtaque() && distancia < distanciaMinima) {
                distanciaMinima = distancia;
                enemigoObjetivo = enemigo;
            }
        }

        if (enemigoObjetivo != null) {
            partida.atacarUnidad(unidadIA, enemigoObjetivo);
            return true;
        }
        return false;
    }


    private void moverHaciaEnemigoCercano(Unidad unidadIA) {
        Jugador oponente = (partida.getJugador1() == jugadorIA) ? partida.getJugador2() : partida.getJugador1();
        MiLista<Unidad> unidadesOponente = oponente.getUnidades();

        if (unidadesOponente.estaVacia()) {
            return;
        }

        Unidad enemigoObjetivo = null;
        int distanciaMinima = Integer.MAX_VALUE;

        // Encontrar el enemigo más cercano en términos de distancia en el tablero (coste de ruta BFS)
        for (Unidad enemigo : unidadesOponente) {
            if (!enemigo.estaViva()) {
                continue;
            }
            // Usar Posicion para obtener la casilla de inicio y destino
            Casilla inicio = partida.getTablero().getCasilla(unidadIA.getPosicion());
            Casilla destinoEnemigo = partida.getTablero().getCasilla(enemigo.getPosicion());

            MiLista<Casilla> ruta = partida.getTablero().getRutaBFS(inicio, destinoEnemigo, unidadIA);

            if (ruta != null && !ruta.estaVacia() && ruta.tamano() < distanciaMinima) {
                distanciaMinima = ruta.tamano();
                enemigoObjetivo = enemigo;
            }
        }

        if (enemigoObjetivo != null) {
            Casilla inicio = partida.getTablero().getCasilla(unidadIA.getPosicion());
            Casilla destinoEnemigo = partida.getTablero().getCasilla(enemigoObjetivo.getPosicion());
            MiLista<Casilla> ruta = partida.getTablero().getRutaBFS(inicio, destinoEnemigo, unidadIA);

            if (ruta != null && !ruta.estaVacia()) {
                int movimientosRestantes = unidadIA.getRangoMovimiento();
                Casilla casillaFinalDeMovimiento = null;

                for(int i = 0; i < ruta.tamano(); i++) { // Iterar para encontrar la última casilla alcanzable
                    Casilla casillaRuta = ruta.obtener(i);

                    // Si la casilla está ocupada por una unidad aliada, o la propia unidad (ya está allí),
                    // no podemos movernos a ella.
                    // La lógica de getRutaBFS ya debería haber manejado obstáculos.
                    // Este bucle es para limitar el movimiento al rango.
                    // Si la casillaRuta es el destino y está ocupada por un enemigo, podemos considerarla
                    // como un punto hasta el que la IA se quiere acercar.
                    if (casillaRuta.getCosteMovimiento() <= movimientosRestantes && !casillaRuta.estaOcupada()) {
                        movimientosRestantes -= casillaRuta.getCosteMovimiento();
                        casillaFinalDeMovimiento = casillaRuta;
                    } else {
                        // Si la casilla está ocupada (y no es el objetivo de ataque) o no hay suficiente movimiento
                        break;
                    }
                }

                if (casillaFinalDeMovimiento != null && !casillaFinalDeMovimiento.equals(inicio)) { // Asegurarse de que realmente se va a mover
                    // ¡CORRECCIÓN! Usar objeto Posicion
                    partida.moverUnidad(unidadIA, casillaFinalDeMovimiento.getPosicion());
                } else {
                    System.out.println("IA: " + unidadIA.getNombre() + " no pudo moverse más cerca del enemigo " + enemigoObjetivo.getNombre() + " o ya estaba adyacente.");
                }
            } else {
                System.out.println("IA: No se encontró una ruta válida para " + unidadIA.getNombre() + " hacia " + enemigoObjetivo.getNombre());
            }
        } else {
            System.out.println("IA: " + unidadIA.getNombre() + " no encontró un enemigo cercano para moverse.");
        }
    }
}