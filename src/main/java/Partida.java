public class Partida {
    private Tablero tablero;
    private Jugador jugador1;
    private Jugador jugador2;
    private boolean turnoJugador1;
    private int turnoActual;

    public Partida(int filas, int columnas, String nombreJugador1, String nombreJugador2) {
        this.tablero = new Tablero(filas, columnas);
        this.jugador1 = new Jugador(nombreJugador1);
        this.jugador2 = new Jugador(nombreJugador2);
        this.turnoJugador1 = true;
        this.turnoActual = 1;
    }

    public Jugador getJugadorActual() {
        return turnoJugador1 ? jugador1 : jugador2;
    }

    public Jugador getJugadorOponente() {
        return turnoJugador1 ? jugador2 : jugador1;
    }

    public void siguienteTurno() {
        turnoJugador1 = !turnoJugador1;
        turnoActual++;
        for (Unidad unidad : getJugadorActual().getUnidades()) {
            unidad.reiniciarMovimiento(); // Método que debes tener en Unidad
        }
    }

    public boolean estaTerminada() {
        return !jugador1.tieneUnidades() || !jugador2.tieneUnidades();
    }

    public String getGanador() {
        if (!jugador1.tieneUnidades()) return jugador2.getNombre();
        if (!jugador2.tieneUnidades()) return jugador1.getNombre();
        return null;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public Tablero getTablero() {
        return tablero;
    }
}
