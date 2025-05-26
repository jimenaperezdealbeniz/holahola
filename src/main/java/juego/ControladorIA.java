package juego;

import estructuras.MiLista;
import estructuras.MiListaEnlazada;

import java.util.Random;

public class ControladorIA {
    private Partida partida;
    private Jugador jugadorIA;
    private Random random;

    public ControladorIA(Partida partida, Jugador jugadorIA) {
        this.partida = partida;
        this.jugadorIA = jugadorIA;
        this.random = new Random();
    }

    public void realizarTurnoIA() {
        System.out.println("Turno de la IA: " + jugadorIA.getNombre());
        MiLista<Unidad> unidadesIA = jugadorIA.getUnidades();

        // Si la IA no tiene unidades, no puede hacer nada
        if (unidadesIA.estaVacia()) {
            System.out.println("La IA no tiene unidades para mover.");
            return;
        }

        // Recorrer las unidades de la IA
        for (Unidad unidadIA : unidadesIA) {
            // Verificar si la unidad todavía existe (no fue eliminada por otro ataque en este mismo turno de la IA)
            if (!partida.getTablero().getCasilla(unidadIA.getX(), unidadIA.getY()).estaOcupada() ||
                    !partida.getTablero().getCasilla(unidadIA.getX(), unidadIA.getY()).getUnidadActual().equals(unidadIA)) {
                continue; // La unidad ya no está en el tablero o fue reemplazada
            }


            // 1. Intentar atacar primero
            Unidad enemigoCercano = buscarEnemigoCercanoEnRango(unidadIA);
            if (enemigoCercano != null) {
                partida.atacarUnidad(unidadIA, enemigoCercano);
                System.out.println(unidadIA.getNombre() + " (IA) atacó a " + enemigoCercano.getNombre());
                // Después de atacar, la unidad no puede moverse en este turno (regla simple)
                continue;
            }

            // 2. Si no puede atacar, intentar moverse hacia el enemigo más cercano
            moverHaciaEnemigoCercano(unidadIA);
            System.out.println(unidadIA.getNombre() + " (IA) se movió o intentó moverse.");
        }
    }

    private Unidad buscarEnemigoCercanoEnRango(Unidad unidadIA) {
        Jugador oponente = (partida.getJugador1() == jugadorIA) ? partida.getJugador2() : partida.getJugador1();
        MiLista<Unidad> unidadesOponente = oponente.getUnidades();
        Unidad enemigoMasCercanoEnRango = null;
        int distanciaMinima = Integer.MAX_VALUE;

        for (Unidad enemigo : unidadesOponente) {
            // Distancia de Manhattan (no es exactamente la del grafo, pero sirve para rango de ataque)
            int distancia = Math.abs(unidadIA.getX() - enemigo.getX()) + Math.abs(unidadIA.getY() - enemigo.getY());
            if (distancia <= unidadIA.getRangoAtaque() && distancia < distanciaMinima) {
                enemigoMasCercanoEnRango = enemigo;
                distanciaMinima = distancia;
            }
        }
        return enemigoMasCercanoEnRango;
    }

    private void moverHaciaEnemigoCercano(Unidad unidadIA) {
        Jugador oponente = (partida.getJugador1() == jugadorIA) ? partida.getJugador2() : partida.getJugador1();
        MiLista<Unidad> unidadesOponente = oponente.getUnidades();

        if (unidadesOponente.estaVacia()) {
            return; // No hay enemigos a los que acercarse
        }

        Unidad enemigoObjetivo = null;
        int distanciaMinima = Integer.MAX_VALUE;

        // Encontrar el enemigo más cercano en términos de distancia en el tablero (no solo Manhattan)
        for (Unidad enemigo : unidadesOponente) {
            // Usar BFS para encontrar la ruta más corta al enemigo
            Casilla inicio = partida.getTablero().getCasilla(unidadIA.getX(), unidadIA.getY());
            Casilla destinoEnemigo = partida.getTablero().getCasilla(enemigo.getX(), enemigo.getY());
            MiLista<Casilla> ruta = partida.getTablero().getRutaBFS(inicio, destinoEnemigo, unidadIA);

            if (ruta != null && ruta.tamano() < distanciaMinima) {
                distanciaMinima = ruta.tamano();
                enemigoObjetivo = enemigo;
            }
        }

        if (enemigoObjetivo != null) {
            Casilla inicio = partida.getTablero().getCasilla(unidadIA.getX(), unidadIA.getY());
            Casilla destinoEnemigo = partida.getTablero().getCasilla(enemigoObjetivo.getX(), enemigoObjetivo.getY());
            MiLista<Casilla> ruta = partida.getTablero().getRutaBFS(inicio, destinoEnemigo, unidadIA);

            if (ruta != null && !ruta.estaVacia()) {
                // Mover la unidad el máximo de casillas posible dentro de su rango de movimiento
                // y hacia el enemigo.
                // La ruta incluye el destino. El primer elemento de la ruta es la casilla a la que se moverá primero.
                int movimientosRestantes = unidadIA.getRangoMovimiento();
                Casilla siguienteCasilla = null;

                for(int i = 0; i < ruta.tamano(); i++) {
                    Casilla casillaRuta = ruta.obtener(i);
                    // No podemos movernos a una casilla ocupada, a menos que sea el destino final y el enemigo se mueva.
                    // Para simplificar la IA, la unidad solo se mueve si la casilla destino está desocupada.
                    if (casillaRuta.estaOcupada()) {
                        // Si la casilla está ocupada por una unidad que no es el objetivo, no podemos pasar.
                        // Si es el objetivo, no podemos terminar el movimiento ahí.
                        // Si está ocupada por el objetivo, la IA no se mueve, intenta atacar.
                        // Si está ocupada por un aliado, tampoco se mueve.
                        System.out.println("IA: Ruta bloqueada por una unidad en " + casillaRuta);
                        break;
                    }

                    if (casillaRuta.getCosteMovimiento() <= movimientosRestantes) {
                        movimientosRestantes -= casillaRuta.getCosteMovimiento();
                        siguienteCasilla = casillaRuta; // Esta es la última casilla válida a la que se puede mover
                    } else {
                        break; // No hay suficiente movimiento para la siguiente casilla de la ruta
                    }
                }

                if (siguienteCasilla != null) {
                    partida.moverUnidad(unidadIA, siguienteCasilla.getFila(), siguienteCasilla.getColumna());
                } else {
                    System.out.println("IA: No se pudo encontrar un movimiento válido para " + unidadIA.getNombre() + " hacia " + enemigoObjetivo.getNombre());
                }
            } else {
                System.out.println("IA: No se encontró una ruta válida para " + unidadIA.getNombre() + " hacia " + enemigoObjetivo.getNombre());
            }
        }
    }
}