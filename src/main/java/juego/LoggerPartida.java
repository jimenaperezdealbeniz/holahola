package juego;

import estructuras.MiListaEnlazada;
import estructuras.MiLista;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerPartida implements Serializable {
    @Expose private MiLista<NodoLog> logLineal;      // Log tradicional de texto
    @Expose private GrafoLog grafoLog;               // Log estructurado en forma de grafo

    public LoggerPartida() {
        this.logLineal = new MiListaEnlazada<>();
        this.grafoLog = new GrafoLog();
    }

    /**
     * Registra una acción en el log de texto.
     */
    public void registrarAccion(Object entidad, String descripcion, int turnoGlobal) {
        String idEntidad = (entidad instanceof Unidad) ? ((Unidad) entidad).getNombre() : "Juego/Sistema";
        String marcaTiempo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String mensaje = marcaTiempo + " [Turno " + turnoGlobal + "] " + idEntidad + ": " + descripcion;
        logLineal.agregar(new NodoLog(mensaje));
    }

    /**
     * Registra una relación entre dos entidades en el log grafo.
     * Ej: unidad se mueve a casilla, o ataca a otra unidad.
     */
    public void registrarRelacion(Object origen, Object destino, String accion, int turnoGlobal) {
        NodoEntidad nodoOrigen = construirNodoEntidad(origen);
        NodoEntidad nodoDestino = construirNodoEntidad(destino);

        if (nodoOrigen != null && nodoDestino != null) {
            grafoLog.agregarRelacion(nodoOrigen, nodoDestino, accion, turnoGlobal);
        }
    }

    /**
     * Crea un NodoEntidad a partir de una unidad, casilla o cualquier otro objeto relevante.
     */
    private NodoEntidad construirNodoEntidad(Object obj) {
        if (obj instanceof Unidad) {
            return new NodoEntidad(((Unidad) obj).getNombre(), "Unidad");
        } else if (obj instanceof Casilla) {
            return new NodoEntidad(((Casilla) obj).getPosicion().toString(), "Casilla");
        } else if (obj instanceof Posicion) {
            return new NodoEntidad(((Posicion) obj).toString(), "Posicion");
        } else if (obj instanceof String) {
            return new NodoEntidad((String) obj, "Texto");
        }
        return null;
    }

    public MiLista<NodoLog> getLogLineal() {
        return logLineal;
    }

    public GrafoLog getGrafoLog() {
        return grafoLog;
    }

    /**
     * Muestra el log lineal en formato legible.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("--- LOG LINEAL DE PARTIDA ---\n");
        for (NodoLog entry : logLineal) {
            sb.append(entry.getMensaje()).append("\n");
        }
        sb.append("-----------------------------\n");
        return sb.toString();
    }

    /**
     * Muestra también el log en forma de grafo.
     */
    public String mostrarLogGrafo() {
        return grafoLog.toString();
    }
}
