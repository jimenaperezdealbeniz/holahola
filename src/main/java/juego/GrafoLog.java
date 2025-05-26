package juego;

import estructuras.MiLista;
import estructuras.MiListaEnlazada;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GrafoLog implements Serializable {
    private MiLista<NodoEntidad> entidades;
    private MiLista<RelacionLog> relaciones;

    public GrafoLog() {
        this.entidades = new MiListaEnlazada<>();
        this.relaciones = new MiListaEnlazada<>();
    }

    public void agregarRelacion(NodoEntidad origen, NodoEntidad destino, String accion, int turno) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Añadir nodos solo si no existen
        if (!entidades.contiene(origen)) {
            entidades.agregar(origen);
        }
        if (!entidades.contiene(destino)) {
            entidades.agregar(destino);
        }

        // Añadir la relación
        RelacionLog relacion = new RelacionLog(origen, destino, accion, turno, timestamp);
        relaciones.agregar(relacion);
    }

    public MiLista<NodoEntidad> getEntidades() {
        return entidades;
    }

    public MiLista<RelacionLog> getRelaciones() {
        return relaciones;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("--- GRAFO DEL LOG DE PARTIDA ---\n");
        for (int i = 0; i < relaciones.tamano(); i++) {
            sb.append(relaciones.obtener(i).toString()).append("\n");
        }
        sb.append("--------------------------------\n");
        return sb.toString();
    }
}
