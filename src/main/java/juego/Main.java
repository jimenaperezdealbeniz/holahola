package juego; // Asegúrate de que el paquete sea el correcto para tu Main

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import estructuras.MiLista; // Si necesitas acceder a métodos de MiLista en Main
import estructuras.MiListaEnlazada;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String RUTA_GUARDADO = "partida.json"; // Ruta donde se guardará/cargará la partida

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting() // Para que el JSON sea legible
                .excludeFieldsWithoutExposeAnnotation() // Excluir campos sin @Expose
                .create();

        Partida partidaActual = null;

        System.out.println("--- ¡Bienvenido al Juego de Batalla de Ingenieros y Poetas! ---");
        System.out.println("1. Nueva Partida");
        System.out.println("2. Cargar Partida");
        System.out.print("Elige una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        if (opcion == 1) {
            System.out.print("Introduce nombre del Jugador 1: ");
            String nombreJ1 = scanner.nextLine();
            System.out.print("Introduce nombre del Jugador 2 (IA): ");
            String nombreJ2 = scanner.nextLine();
            System.out.print("Introduce el número de filas del tablero (ej. 10): ");
            int filas = scanner.nextInt();
            System.out.print("Introduce el número de columnas del tablero (ej. 10): ");
            int columnas = scanner.nextInt();
            System.out.print("¿Cada cuántos turnos se genera una nueva unidad? (ej. 5): ");
            int turnosNuevaUnidad = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            // ¡CORRECCIÓN! Se pasa el quinto argumento: turnosNuevaUnidad
            partidaActual = new Partida(filas, columnas, nombreJ1, nombreJ2, turnosNuevaUnidad);
            System.out.println("¡Nueva partida creada!");
        } else if (opcion == 2) {
            partidaActual = cargarPartida(gson);
            if (partidaActual != null) {
                System.out.println("Partida cargada exitosamente desde " + RUTA_GUARDADO);
            } else {
                System.out.println("No se pudo cargar la partida. Iniciando una nueva...");
                // Crear una partida predeterminada si no se puede cargar
                partidaActual = new Partida(10, 10, "Jugador Humano", "Jugador IA", 5);
            }
        } else {
            System.out.println("Opción no válida. Saliendo del juego.");
            scanner.close();
            return;
        }

        // --- Bucle principal del juego ---
        if (partidaActual != null) {
            simularJuego(partidaActual, scanner, gson);
        }

        scanner.close();
    }

    private static void simularJuego(Partida partida, Scanner scanner, Gson gson) {
        System.out.println("\n--- ¡Comienza el Juego! ---");

        while (!partida.estaTerminada()) {
            System.out.println("\n--- TURNO " + partida.getTurnoGlobal() + " ---");
            // ¡CORRECCIÓN! Usar gestorTurnos.obtenerJugadorActual()
            Jugador jugadorActual = partida.getGestorTurnos().obtenerJugadorActual();
            System.out.println("Es el turno de: " + jugadorActual.getNombre());

            // Mostrar estado del tablero (simple)
            imprimirTablero(partida.getTablero());
            System.out.println("Unidades de " + jugadorActual.getNombre() + ":");
            imprimirUnidadesJugador(jugadorActual);

            // Si es el jugador humano
            if (jugadorActual == partida.getJugador1()) { // Asumiendo J1 es el humano
                System.out.println("\n¿Qué quieres hacer, " + jugadorActual.getNombre() + "?");
                System.out.println("1. Mover una unidad");
                System.out.println("2. Atacar una unidad");
                System.out.println("3. Pasar turno");
                System.out.println("4. Guardar Partida");
                System.out.print("Elige una acción: ");
                int accion = scanner.nextInt();
                scanner.nextLine();

                switch (accion) {
                    case 1:
                        realizarMovimiento(partida, jugadorActual, scanner);
                        break;
                    case 2:
                        realizarAtaque(partida, jugadorActual, scanner);
                        break;
                    case 3:
                        System.out.println("Pasando turno...");
                        // ¡CORRECCIÓN! Usar avanzarTurno()
                        partida.avanzarTurno();
                        break;
                    case 4:
                        guardarPartida(partida, gson);
                        System.out.println("Partida guardada.");
                        break;
                    default:
                        System.out.println("Acción no válida. Se pasa el turno automáticamente.");
                        // ¡CORRECCIÓN! Usar avanzarTurno()
                        partida.avanzarTurno();
                        break;
                }
            } else {
                // La IA ya se maneja internamente en avanzarTurno()
                // Aquí solo imprimimos un mensaje antes de que la IA juegue su turno
                System.out.println("La IA está pensando su movimiento...");
                // Para que la IA juegue su turno y el juego avance
                partida.avanzarTurno();
            }

            // Opcional: Pausar para que el usuario vea el resultado antes de avanzar
            // Solo para el turno del humano, para que no avance muy rápido
            if (jugadorActual == partida.getJugador1() && !partida.estaTerminada()) {
                System.out.println("\nPresiona Enter para continuar...");
                scanner.nextLine();
            }
        }

        System.out.println("\n--- ¡PARTIDA TERMINADA! ---");
        Jugador ganador = partida.getGanador();
        if (ganador != null) {
            System.out.println("¡El ganador es: " + ganador.getNombre() + "!");
        } else {
            System.out.println("Ha sido un empate.");
        }
        System.out.println("Revisa el log para los detalles de la partida.");
    }

    private static void realizarMovimiento(Partida partida, Jugador jugadorActual, Scanner scanner) {
        if (jugadorActual.getUnidades().estaVacia()) {
            System.out.println("No tienes unidades para mover.");
            return;
        }

        System.out.println("Tus unidades disponibles:");
        for (int i = 0; i < jugadorActual.getUnidades().tamano(); i++) {
            Unidad u = jugadorActual.getUnidades().obtener(i);
            System.out.println(i + ". " + u.getNombre() + " en " + u.getPosicion().toString());
        }
        System.out.print("Elige el número de la unidad a mover: ");
        int unidadIndex = scanner.nextInt();
        scanner.nextLine();

        if (unidadIndex < 0 || unidadIndex >= jugadorActual.getUnidades().tamano()) {
            System.out.println("Unidad no válida.");
            return;
        }
        Unidad unidadAMover = jugadorActual.getUnidades().obtener(unidadIndex);

        System.out.print("Introduce la fila destino: ");
        int destFila = scanner.nextInt();
        System.out.print("Introduce la columna destino: ");
        int destColumna = scanner.nextInt();
        scanner.nextLine();
        Posicion nuevaPosicion = new Posicion(destFila, destColumna);

        if (partida.moverUnidad(unidadAMover, nuevaPosicion)) { // Se pasa el objeto Posicion
            System.out.println(unidadAMover.getNombre() + " movido a " + nuevaPosicion.toString());
        } else {
            System.out.println("No se pudo mover la unidad. Revisa el log.");
        }
    }

    private static void realizarAtaque(Partida partida, Jugador jugadorActual, Scanner scanner) {
        if (jugadorActual.getUnidades().estaVacia()) {
            System.out.println("No tienes unidades para atacar.");
            return;
        }

        System.out.println("Tus unidades disponibles para atacar:");
        for (int i = 0; i < jugadorActual.getUnidades().tamano(); i++) {
            Unidad u = jugadorActual.getUnidades().obtener(i);
            System.out.println(i + ". " + u.getNombre() + " en " + u.getPosicion().toString());
        }
        System.out.print("Elige el número de la unidad atacante: ");
        int atacanteIndex = scanner.nextInt();
        scanner.nextLine();

        if (atacanteIndex < 0 || atacanteIndex >= jugadorActual.getUnidades().tamano()) {
            System.out.println("Unidad atacante no válida.");
            return;
        }
        Unidad atacante = jugadorActual.getUnidades().obtener(atacanteIndex);

        Jugador oponente = (jugadorActual == partida.getJugador1()) ? partida.getJugador2() : partida.getJugador1();
        if (oponente.getUnidades().estaVacia()) {
            System.out.println("El oponente no tiene unidades para atacar.");
            return;
        }

        System.out.println("Unidades enemigas disponibles para ser atacadas:");
        for (int i = 0; i < oponente.getUnidades().tamano(); i++) {
            Unidad u = oponente.getUnidades().obtener(i);
            System.out.println(i + ". " + u.getNombre() + " en " + u.getPosicion().toString() + " (HP: " + u.getHp() + ")");
        }
        System.out.print("Elige el número de la unidad defensora: ");
        int defensorIndex = scanner.nextInt();
        scanner.nextLine();

        if (defensorIndex < 0 || defensorIndex >= oponente.getUnidades().tamano()) {
            System.out.println("Unidad defensora no válida.");
            return;
        }
        Unidad defensor = oponente.getUnidades().obtener(defensorIndex);

        int dano = partida.atacarUnidad(atacante, defensor);
        if (dano > 0) {
            System.out.println(atacante.getNombre() + " atacó a " + defensor.getNombre() + " causando " + dano + " de daño.");
            if (defensor.getHp() <= 0) {
                System.out.println(defensor.getNombre() + " ha sido eliminado!");
            }
        } else {
            System.out.println("El ataque no se pudo realizar (fuera de rango o error).");
        }
    }


    private static void imprimirTablero(Tablero tablero) {
        System.out.println("\n--- Estado del Tablero ---");
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Casilla casilla = tablero.getCasilla(i, j);
                if (casilla != null && casilla.estaOcupada()) {
                    Unidad u = casilla.getUnidadActual(); // Usar getUnidadActual()
                    // Si el nombre de la unidad incluye el nombre del jugador (ej. Ingeniero_J1_01)
                    // Podrías extraer el jugador para identificar de quién es la unidad.
                    // Para simplificar, asumimos que J1 y J2 en el nombre se refieren a los jugadores.
                    String simbolo = "U"; // Símbolo genérico por defecto
                    if (u.getNombre().contains("J1")) {
                        if (u instanceof Ingeniero) {
                            simbolo = "I1";
                        } else if (u instanceof Poeta) {
                            simbolo = "P1";
                        }
                    } else if (u.getNombre().contains("J2")) {
                        if (u instanceof Ingeniero) {
                            simbolo = "I2";
                        } else if (u instanceof Poeta) {
                            simbolo = "P2";
                        }
                    }
                    System.out.print("[" + simbolo + "]");
                } else {
                    System.out.print("[ ]"); // Casilla vacía
                }
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }

    private static void imprimirUnidadesJugador(Jugador jugador) {
        if (jugador.getUnidades().estaVacia()) {
            System.out.println(jugador.getNombre() + " no tiene unidades.");
            return;
        }
        for (int i = 0; i < jugador.getUnidades().tamano(); i++) {
            Unidad u = jugador.getUnidades().obtener(i);
            System.out.println("- " + u.getNombre() + " (HP: " + u.getHp() + ") en " + u.getPosicion().toString());
        }
    }

    // --- Métodos de Guardar/Cargar Partida ---
    private static void guardarPartida(Partida partida, Gson gson) {
        try (FileWriter writer = new FileWriter(RUTA_GUARDADO)) {
            gson.toJson(partida, writer);
            System.out.println("Partida guardada en: " + RUTA_GUARDADO);
        } catch (IOException e) {
            System.err.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    private static Partida cargarPartida(Gson gson) {
        try (FileReader reader = new FileReader(RUTA_GUARDADO)) {
            return gson.fromJson(reader, Partida.class);
        } catch (IOException e) {
            System.err.println("Error al cargar la partida: " + e.getMessage());
            return null;
        }
    }
}