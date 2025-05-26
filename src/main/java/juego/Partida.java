package juego;

import com.google.gson.annotations.Expose; // ¡Importante para la serialización!
import estructuras.MiLista; // Asegúrate de que tus estructuras estén correctamente importadas
import estructuras.MiListaEnlazada;

import java.io.Serializable; // ¡Importante para la serialización!
import java.util.Map; // Para los Map de BFS
import java.util.Random;

public class Partida implements Serializable {
    @Expose private Tablero tablero;
    @Expose private Jugador jugador1;
    @Expose private Jugador jugador2;
    @Expose private GestorTurnos gestorTurnos;
    @Expose private int turnosParaNuevaUnidad; // Renombrado para consistencia
    @Expose private int contadorTurnosParaNuevaUnidad;
    @Expose private LoggerPartida logger;
    @Expose private ControladorIA controladorIA; // Consistencia en el nombre
    @Expose private int turnoGlobal; // Nuevo atributo para el turno global de la partida

    private Random random; // Atributo para la generación de números aleatorios

    // Constructor vacío para Gson. ¡Es esencial para la deserialización!
    public Partida() {
        this.logger = new LoggerPartida(); // Se inicializa para evitar NullPointer si se carga una partida vieja sin logger
        this.random = new Random(); // Se inicializa siempre, para que funcione incluso si no se serializa
    }

    // Constructor principal de la Partida
    public Partida(int filas, int columnas, String nombreJugador1, String nombreJugador2, int turnosParaNuevaUnidad) {
        this.tablero = new Tablero(filas, columnas);
        this.jugador1 = new Jugador(nombreJugador1);
        this.jugador2 = new Jugador(nombreJugador2);
        this.gestorTurnos = new GestorTurnos(jugador1, jugador2);
        this.turnosParaNuevaUnidad = turnosParaNuevaUnidad; // Asigna el parámetro
        this.contadorTurnosParaNuevaUnidad = 0; // Se inicializa el contador de turnos para nuevas unidades
        this.logger = new LoggerPartida(); // Se inicializa el logger
        this.controladorIA = new ControladorIA(this, this.jugador2); // Se inicializa el controlador de la IA para el jugador 2 (asumiendo que J2 es la IA)
        this.turnoGlobal = 1; // El juego siempre empieza en el turno 1

        inicializarUnidades(); // Llamamos al método para colocar las unidades iniciales
    }

    // --- Getters de la Partida ---
    public Tablero getTablero() { return tablero; }
    public Jugador getJugador1() { return jugador1; }
    public Jugador getJugador2() { return jugador2; }
    public GestorTurnos getGestorTurnos() { return gestorTurnos; }
    public LoggerPartida getLogger() { return logger; }
    public int getTurnosParaNuevaUnidad() { return turnosParaNuevaUnidad; }
    public int getTurnoGlobal() { return turnoGlobal; } // Getter para el turno global

    // --- Métodos de Lógica del Juego ---

    /**
     * Encapsula la acción de registrar un evento tanto en el log de texto como en el grafo de relaciones.
     * @param origen El objeto que inicia la acción (Unidad, Jugador, etc.).
     * @param destino El objeto o posición de destino de la acción. Puede ser una Posicion, una Unidad, o un String.
     * @param accionVerbo La descripción verbal de la acción (e.g., "se mueve a", "ataca a", "se posiciona en").
     * @param turno El turno global en el que ocurre la acción.
     */
    public void registrarTodo(Object origen, Object destino, String accionVerbo, int turno) {
        // En algunos casos, el destino no es un objeto con nombre, sino una posición, etc.
        String accionTexto = accionVerbo;
        if (destino instanceof Posicion) {
            accionTexto += " " + ((Posicion) destino).toString();
        } else if (destino instanceof Unidad) {
            accionTexto += " " + ((Unidad) destino).getNombre();
        } else if (destino instanceof String) {
            accionTexto += " " + (String) destino;
        } else {
            accionTexto += " " + destino.getClass().getSimpleName(); // Fallback genérico
        }

        // Si el origen es una unidad, el texto de la acción debería incluir su nombre
        if (origen instanceof Unidad) {
            logger.registrarAccion((Unidad) origen, accionTexto, turno);
        } else if (origen instanceof Jugador) {
            logger.registrarAccion((Jugador) origen, accionTexto, turno);
        } else {
            logger.registrarAccion(origen, accionTexto, turno); // Para otros objetos (e.g., null para eventos del sistema)
        }

        logger.registrarRelacion(origen, destino, accionVerbo, turno);
    }

    private void inicializarUnidades() {
        // Jugador 1 (esquina superior izquierda)
        Unidad ing1 = new Ingeniero("Ingeniero_J1_01");
        Posicion posIng1 = new Posicion(0, 0);
        tablero.getCasilla(posIng1.getX(), posIng1.getY()).setUnidadActual(ing1);
        ing1.setPosicion(posIng1);
        jugador1.agregarUnidad(ing1);
        registrarTodo(ing1, posIng1, "se posiciona en", this.turnoGlobal);

        Unidad poet1 = new Poeta("Poeta_J1_01");
        Posicion posPoet1 = new Posicion(0, 1);
        tablero.getCasilla(posPoet1.getX(), posPoet1.getY()).setUnidadActual(poet1);
        poet1.setPosicion(posPoet1);
        jugador1.agregarUnidad(poet1);
        registrarTodo(poet1, posPoet1, "se posiciona en", this.turnoGlobal);

        // Jugador 2 (esquina inferior derecha)
        Unidad ing2 = new Ingeniero("Ingeniero_J2_01");
        Posicion posIng2 = new Posicion(tablero.getFilas() - 1, tablero.getColumnas() - 1);
        tablero.getCasilla(posIng2.getX(), posIng2.getY()).setUnidadActual(ing2);
        ing2.setPosicion(posIng2);
        jugador2.agregarUnidad(ing2);
        registrarTodo(ing2, posIng2, "se posiciona en", this.turnoGlobal);

        Unidad poet2 = new Poeta("Poeta_J2_01");
        Posicion posPoet2 = new Posicion(tablero.getFilas() - 1, tablero.getColumnas() - 2);
        tablero.getCasilla(posPoet2.getX(), posPoet2.getY()).setUnidadActual(poet2);
        poet2.setPosicion(posPoet2);
        jugador2.agregarUnidad(poet2);
        registrarTodo(poet2, posPoet2, "se posiciona en", this.turnoGlobal);
    }

    /**
     * Mueve una unidad de su posición actual a una nueva posición en el tablero.
     * Utiliza el algoritmo BFS del tablero para determinar si la nueva posición es alcanzable.
     *
     * @param unidad         La unidad a mover.
     * @param nuevaPosicion  La Posicion destino a la que se desea mover la unidad.
     * @return true si el movimiento es válido y se realiza, false en caso contrario.
     */
    public boolean moverUnidad(Unidad unidad, Posicion nuevaPosicion) {
        // Asegúrate de que la unidad pertenece al jugador actual para evitar movimientos no autorizados
        if (!gestorTurnos.obtenerJugadorActual().getUnidades().contiene(unidad)) {
            logger.registrarAccion(unidad, "Intento de movimiento no autorizado por jugador", this.turnoGlobal);
            return false;
        }

        Casilla casillaActual = tablero.getCasilla(unidad.getX(), unidad.getY());
        Casilla casillaDestino = tablero.getCasilla(nuevaPosicion.getX(), nuevaPosicion.getY());

        if (casillaDestino == null || casillaDestino.estaOcupada()) {
            logger.registrarAccion(unidad, "Intento de mover a casilla inválida o ocupada: " + nuevaPosicion.toString(), this.turnoGlobal);
            return false;
        }

        // Obtener casillas alcanzables usando el BFS del Tablero
        Map<Casilla, Integer> casillasAlcanzables = tablero.getCasillasAlcanzablesBFS(casillaActual, unidad.getRangoMovimiento());

        // Verificar si la casilla destino está entre las alcanzables y el coste es válido
        if (casillasAlcanzables.containsKey(casillaDestino)) {
            // Realizar el movimiento
            casillaActual.setUnidadActual(null); // Desocupar la casilla actual
            casillaDestino.setUnidadActual(unidad); // Ocupar la casilla destino
            unidad.setPosicion(nuevaPosicion); // Actualizar la posición de la unidad

            // Registrar el movimiento con registrarTodo
            registrarTodo(unidad, nuevaPosicion, "se mueve a", this.turnoGlobal);
            return true;
        } else {
            logger.registrarAccion(unidad, "Intento de movimiento fuera de rango a: " + nuevaPosicion.toString(), this.turnoGlobal);
            return false;
        }
    }

    /**
     * Una unidad ataca a otra unidad enemiga.
     *
     * @param atacante La unidad que ataca.
     * @param defensor La unidad que recibe el ataque.
     * @return El daño infligido.
     */
    public int atacarUnidad(Unidad atacante, Unidad defensor) {
        // Asegúrate de que el atacante pertenece al jugador actual
        if (!gestorTurnos.obtenerJugadorActual().getUnidades().contiene(atacante)) {
            logger.registrarAccion(atacante, "Intento de ataque no autorizado por jugador", this.turnoGlobal);
            return 0;
        }

        // Verificar que el defensor es una unidad enemiga
        Jugador jugadorActual = gestorTurnos.obtenerJugadorActual();
        Jugador jugadorOponente = (jugadorActual == jugador1) ? jugador2 : jugador1;
        if (!jugadorOponente.getUnidades().contiene(defensor)) {
            logger.registrarAccion(atacante, "Intento de ataque a unidad no enemiga o inexistente: " + defensor.getNombre(), this.turnoGlobal);
            return 0;
        }

        // Comprobación de rango de ataque usando Posicion.distanciaA()
        if (atacante.getPosicion().distanciaA(defensor.getPosicion()) > atacante.getRangoAtaque()) {
            logger.registrarAccion(atacante, "Intento de ataque fuera de rango a " + defensor.getNombre(), this.turnoGlobal);
            return 0; // No se inflige daño si está fuera de rango
        }

        // Cálculo del daño (factor aleatorio añadido)
        int factor = random.nextInt(3); // Esto generará 0, 1 o 2

        int dano = (factor * atacante.getAtaque()) - (defensor.getDefensa() + tablero.getCasilla(defensor.getX(), defensor.getY()).getDefensaModificador());
        if (dano < 0) {
            dano = 0; // El daño no puede ser negativo
        }

        // Aplicar daño
        defensor.setHp(defensor.getHp() - dano);

        // Registrar el ataque
        registrarTodo(atacante, defensor, "ataca a", this.turnoGlobal);

        // Verificar si la unidad defensora ha sido eliminada
        if (defensor.getHp() <= 0) {
            Casilla casillaDefensor = tablero.getCasilla(defensor.getX(), defensor.getY());
            casillaDefensor.setUnidadActual(null); // Desocupar casilla

            // Eliminar la unidad del jugador correspondiente
            if (jugador1.getUnidades().contiene(defensor)) {
                jugador1.eliminarUnidad(defensor);
            } else if (jugador2.getUnidades().contiene(defensor)) {
                jugador2.eliminarUnidad(defensor);
            }
            // Registrar que el defensor es eliminado
            registrarTodo(defensor, "Juego/Sistema", "es eliminado", this.turnoGlobal);
        }
        return dano;
    }

    /**
     * Avanza el turno del juego. Incrementa el turno global, cambia al siguiente jugador,
     * comprueba si se debe generar una nueva unidad y si es el turno de la IA, la activa.
     */
    public void avanzarTurno() {
        // Antes de cambiar de turno, registrar el inicio de turno para el jugador actual
        logger.registrarAccion(gestorTurnos.obtenerJugadorActual(), "INICIO DE TURNO", this.turnoGlobal);

        gestorTurnos.pasarTurno(); // Cambia el jugador actual en la cola

        // Incrementar el turno global
        this.turnoGlobal++; // ¡Se incrementa aquí!

        // Después de que la IA juegue, pasa el turno para que el humano pueda jugar el siguiente
        // Esto solo aplica si la IA consume un turno completo. Si la IA solo hace una acción
        // y el turno sigue siendo suyo, no se debería pasar el turno aquí.
        // Asumiendo que la IA ejecuta su lógica y eso es su "turno" completo:
        if (gestorTurnos.obtenerJugadorActual() == jugador2) { // Asumimos que jugador2 es la IA
            System.out.println("Es turno de la IA: " + jugador2.getNombre());
            controladorIA.realizarTurnoIA(); // Activar la lógica de la IA
            gestorTurnos.pasarTurno(); // La IA ya jugó, pasa el turno al siguiente jugador (humano)
            this.turnoGlobal++; // Incrementa el turno de nuevo para el turno del jugador humano
        }

        logger.registrarAccion(gestorTurnos.obtenerJugadorActual(), "FIN DE TURNO", this.turnoGlobal); // Registrar fin de turno para el que acaba

        // Si se cumple la condición, genera una nueva unidad para el jugador actual
        contadorTurnosParaNuevaUnidad++;
        if (contadorTurnosParaNuevaUnidad >= turnosParaNuevaUnidad) {
            generarNuevaUnidadParaJugadorActual();
            contadorTurnosParaNuevaUnidad = 0; // Reinicia el contador
        }

        logger.registrarAccion(null, "AVANCE A TURNO " + this.turnoGlobal + " - JUGADOR: " + gestorTurnos.obtenerJugadorActual().getNombre(), this.turnoGlobal);

        // Comprobar si la partida ha terminado después de las acciones del turno
        if (estaTerminada()) {
            Jugador ganador = getGanador();
            if (ganador != null) {
                logger.registrarAccion(null, "PARTIDA TERMINADA. GANADOR: " + ganador.getNombre(), this.turnoGlobal);
            } else {
                logger.registrarAccion(null, "PARTIDA TERMINADA. EMPATE.", this.turnoGlobal);
            }
        }
    }

    /**
     * Genera una nueva unidad para el jugador actual y la posiciona en una casilla adyacente
     * a una unidad existente, o en su base si no hay unidades.
     */
    private void generarNuevaUnidadParaJugadorActual() {
        Jugador jugadorActual = gestorTurnos.obtenerJugadorActual();
        Unidad nuevaUnidad = random.nextBoolean() ? new Ingeniero("Ingeniero_" + jugadorActual.getNombre() + "_" + System.currentTimeMillis()) : new Poeta("Poeta_" + jugadorActual.getNombre() + "_" + System.currentTimeMillis());

        MiLista<Unidad> unidadesJugador = jugadorActual.getUnidades(); // Usando MiLista

        if (unidadesJugador.estaVacia()) {
            // Si el jugador no tiene unidades, intentar posicionar en su base
            int filaBase, colBase;
            if (jugadorActual == jugador1) {
                filaBase = 0; colBase = 0;
            } else {
                filaBase = tablero.getFilas() - 1; colBase = tablero.getColumnas() - 1;
            }
            Posicion posBase = new Posicion(filaBase, colBase);
            Casilla casillaBase = tablero.getCasilla(posBase.getX(), posBase.getY());

            if (casillaBase != null && !casillaBase.estaOcupada()) {
                casillaBase.setUnidadActual(nuevaUnidad);
                nuevaUnidad.setPosicion(posBase);
                jugadorActual.agregarUnidad(nuevaUnidad);
                // Registrar la generación en base
                registrarTodo(nuevaUnidad, posBase, "se genera en", this.turnoGlobal);
                return;
            }
        } else {
            // Intentar posicionar adyacente a una unidad existente
            // Recorrer todas las unidades del jugador para encontrar un espacio adyacente
            for (int i = 0; i < unidadesJugador.tamano(); i++) {
                Unidad unidadExistente = unidadesJugador.obtener(i);
                Casilla casillaExistente = tablero.getCasilla(unidadExistente.getX(), unidadExistente.getY());

                MiLista<Casilla> adyacentes = tablero.getAdyacentes(casillaExistente);
                for (int j = 0; j < adyacentes.tamano(); j++) {
                    Casilla adyacente = adyacentes.obtener(j);
                    if (adyacente != null && !adyacente.estaOcupada()) {
                        adyacente.setUnidadActual(nuevaUnidad);
                        nuevaUnidad.setPosicion(adyacente.getPosicion());
                        jugadorActual.agregarUnidad(nuevaUnidad);
                        // Registrar la generación adyacente
                        registrarTodo(nuevaUnidad, adyacente.getPosicion(), "se genera en", this.turnoGlobal);
                        return; // Unidad generada, salir del método
                    }
                }
            }
        }
        // Si no se pudo generar la unidad
        System.out.println("No se pudo generar una nueva unidad para " + jugadorActual.getNombre() + ". No hay espacio adyacente o base libre.");
        logger.registrarAccion(null, "No se pudo generar una nueva unidad para " + jugadorActual.getNombre() + " por falta de espacio.", this.turnoGlobal);
    }

    /**
     * Verifica si la partida ha terminado. La partida termina si uno de los jugadores
     * no tiene unidades restantes.
     * @return true si la partida ha terminado, false en caso contrario.
     */
    public boolean estaTerminada() {
        return jugador1.getUnidades().estaVacia() || jugador2.getUnidades().estaVacia();
    }

    /**
     * Devuelve el jugador ganador si la partida ha terminado.
     * @return El objeto Jugador ganador, o null si es un empate o la partida aún no termina.
     */
    public Jugador getGanador() {
        if (estaTerminada()) {
            if (jugador1.getUnidades().estaVacia() && jugador2.getUnidades().estaVacia()) {
                return null; // Ambos perdieron, empate o nadie gana
            } else if (jugador1.getUnidades().estaVacia()) {
                return jugador2; // Jugador 2 gana
            } else { // if (jugador2.getUnidades().estaVacia())
                return jugador1; // Jugador 1 gana
            }
        }
        return null; // La partida aún no ha terminado
    }
}