package juego;

public class Partida {
    private Tablero tablero;
    private Jugador jugador1, jugador2;
    private boolean turnoJugador1 = true;

    public Partida(int filas, int columnas, String nombre1, String nombre2) {
        this.tablero = new Tablero(filas, columnas);
        this.jugador1 = new Jugador(nombre1);
        this.jugador2 = new Jugador(nombre2);
        inicializarUnidades();
    }

    private void inicializarUnidades() {
        Unidad unidad1 = new Ingeniero();
        Unidad unidad2 = new Poeta();


        unidad1.setPosicion(0, 0);
        unidad2.setPosicion(tablero.getFilas() - 1, tablero.getColumnas() - 1);

        tablero.getCasilla(0, 0).colocarUnidad(unidad1);
        tablero.getCasilla(9, 9).colocarUnidad(unidad2);

        jugador1.agregarUnidad(unidad1);
        jugador2.agregarUnidad(unidad2);
    }

    public void siguienteTurno() {
        turnoJugador1 = !turnoJugador1;
    }

    public Jugador getJugadorActual() {
        return turnoJugador1 ? jugador1 : jugador2;
    }

    public Jugador getJugadorOponente() {
        return turnoJugador1 ? jugador2 : jugador1;
    }

    public boolean estaTerminada() {
        return jugador1.getUnidades().isEmpty() || jugador2.getUnidades().isEmpty();
    }

    public String getGanador() {
        if (jugador1.getUnidades().isEmpty()) return jugador2.getNombre();
        if (jugador2.getUnidades().isEmpty()) return jugador1.getNombre();
        return null;
    }

    public Tablero getTablero() {
        return tablero;
    }
}
