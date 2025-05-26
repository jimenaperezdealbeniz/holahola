package juego;
import com.google.gson.annotations.Expose;
import estructuras.*;


public class GestorTurnos {
   @Expose
   private MiCola<Jugador> colaTurnos;

    public GestorTurnos(Jugador jugador1, Jugador jugador2) {
        this.colaTurnos = new MiColaEnlazada<>();
        colaTurnos.encolar(jugador1);
        colaTurnos.encolar(jugador2);
    }

    public Jugador obtenerJugadorActual() {
        return colaTurnos.frente();
    }

    public void pasarTurno() {
        Jugador actual = colaTurnos.desencolar();
        colaTurnos.encolar(actual);
    }
}
