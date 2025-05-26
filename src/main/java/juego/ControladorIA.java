package juego;

import java.util.List;
import java.util.Random;

public class ControladorIA {
    private Tablero tablero;
    private LoggerPartida logger;
    private Random random = new Random();

    public ControladorIA(Tablero tablero, LoggerPartida logger) {
        this.tablero = tablero;
        this.logger = logger;
    }

    public void jugarTurnoIA(Jugador jugadorIA, List<Jugador> jugadoresEnemigos) {
        for (Unidad unidad : jugadorIA.getUnidades()) {
            moverUnidadAleatoriamente(unidad);
            atacarSiPuede(unidad, jugadoresEnemigos);
        }
    }

    private void moverUnidadAleatoriamente(Unidad unidad) {
        int rango = unidad.getRangoMovimiento();
        int origenX = unidad.getX();
        int origenY = unidad.getY();

        for (int intentos = 0; intentos < 10; intentos++) {
            int dx = random.nextInt(2 * rango + 1) - rango;
            int dy = random.nextInt(2 * rango + 1) - rango;
            int destinoX = origenX + dx;
            int destinoY = origenY + dy;

            if (tablero.estaDentroDeLimites(destinoX, destinoY)) {
                Casilla destino = tablero.getCasilla(destinoX, destinoY);
                if (!destino.estaOcupada()) {
                    tablero.getCasilla(origenX, origenY).removerUnidad();
                    destino.colocarUnidad(unidad);
                    unidad.setPosicion(destinoX, destinoY);

                    logger.log(unidad.getNombre() + " se mueve a (" + destinoX + "," + destinoY + ")");
                    break;
                }
            }
        }
    }

    private void atacarSiPuede(Unidad unidad, List<Jugador> jugadoresEnemigos) {
        int x = unidad.getX();
        int y = unidad.getY();
        int rango = unidad.getRangoAtaque();

        for (int i = x - rango; i <= x + rango; i++) {
            for (int j = y - rango; j <= y + rango; j++) {
                if (tablero.estaDentroDeLimites(i, j)) {
                    Casilla casilla = tablero.getCasilla(i, j);
                    if (casilla.estaOcupada()) {
                        Unidad objetivo = casilla.getUnidad();

                        if (esUnidadDeEnemigo(objetivo, jugadoresEnemigos)) {
                            int danio = Math.max(0, unidad.getAtaque() - objetivo.getDefensa());
                            objetivo.setHp(objetivo.getHp() - danio);

                            logger.log(unidad.getNombre() + " ataca a " + objetivo.getNombre() + " causando " + danio + " de daño");

                            if (objetivo.getHp() <= 0) {
                                casilla.removerUnidad();
                                eliminarUnidadDeJugador(objetivo, jugadoresEnemigos);
                                logger.log(objetivo.getNombre() + " ha muerto.");
                            }
                            return; // Solo un ataque por turno
                        }
                    }
                }
            }
        }
    }

    private boolean esUnidadDeEnemigo(Unidad unidad, List<Jugador> jugadoresEnemigos) {
        for (Jugador enemigo : jugadoresEnemigos) {
            if (enemigo.getUnidades().contains(unidad)) {
                return true;
            }
        }
        return false;
    }

    private void eliminarUnidadDeJugador(Unidad unidad, List<Jugador> jugadoresEnemigos) {
        for (Jugador enemigo : jugadoresEnemigos) {
            if (enemigo.getUnidades().remove(unidad)) {
                break;
            }
        }
    }
}
