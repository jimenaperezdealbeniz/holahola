package juego;

import estructuras.MiListaEnlazada;
import estructuras.MiLista;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.time.LocalDateTime; // Necesitarás Java 8+ para esto
import java.time.format.DateTimeFormatter;


public class LoggerPartida implements Serializable {
    @Expose private MiLista<NodoLog> logEntries; // Cambiado a MiLista

    public LoggerPartida() {
        this.logEntries = new MiListaEnlazada<>(); // Usando MiListaEnlazada
    }

    public void registrarAccion(Object entidad, String descripcion, int turnoGlobal) {
        String idEntidad = (entidad instanceof Unidad) ? ((Unidad) entidad).getNombre() : "Juego/Sistema";
        String marcaTiempo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String mensaje = marcaTiempo + " [Turno " + turnoGlobal + "] " + idEntidad + ": " + descripcion;
        logEntries.agregar(new NodoLog(mensaje));
    }

    public MiLista<NodoLog> getLogEntries() {
        return logEntries;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("--- LOG DE PARTIDA ---\n");
        for (NodoLog entry : logEntries) {
            sb.append(entry.getMensaje()).append("\n");
        }
        sb.append("----------------------");
        return sb.toString();
    }
}