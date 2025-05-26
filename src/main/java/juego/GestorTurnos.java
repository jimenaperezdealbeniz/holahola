package juego;
import com.google.gson.annotations.Expose;
import estructuras.*;

import java.io.Serializable;


public class GestorTurnos implements Serializable {
   @Expose private MiCola<Jugador> colaTurnos;

   public GestorTurnos() {}

    public GestorTurnos(Jugador jugador1, Jugador jugador2) {
        this.colaTurnos = new MiColaEnlazada<>();
        colaTurnos.encolar(jugador1);
        colaTurnos.encolar(jugador2);
    }

    public Jugador obtenerJugadorActual() {
        return colaTurnos.frente();
    }

    public MiCola<Jugador> getColaTurnos() { // Getter necesario para Gson o para depuración
        return colaTurnos;
    }

    public void pasarTurno() {
        Jugador actual = colaTurnos.desencolar();
        colaTurnos.encolar(actual);
    }
}
