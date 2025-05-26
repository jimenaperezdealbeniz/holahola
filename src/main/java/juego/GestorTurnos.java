package juego;

import java.util.LinkedList;
import java.util.Queue;

public class GestorTurnos {
    private Queue<Jugador> colaTurnos;

    public GestorTurnos(Jugador jugador1, Jugador jugador2) {
        colaTurnos = new LinkedList<>();
        colaTurnos.add(jugador1);
        colaTurnos.add(jugador2);
    }

    public Jugador obtenerJugadorActual() {
        return colaTurnos.peek();
    }

    public void pasarTurno() {
        Jugador actual = colaTurnos.poll();
        colaTurnos.add(actual);
    }
}
