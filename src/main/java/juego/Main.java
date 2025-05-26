package juego;

public class Main {
    public static void main(String[] args) {
        Partida partida = new Partida(10, 10, "Jugador 1", "Jugador 2");

        System.out.println("Empieza el juego:");
        System.out.println("Turno de: " + partida.getJugadorActual().getNombre());

        partida.siguienteTurno();
        System.out.println("Ahora es turno de: " + partida.getJugadorActual().getNombre());
    }
}

