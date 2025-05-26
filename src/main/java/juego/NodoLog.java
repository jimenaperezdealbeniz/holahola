package juego;

import java.util.ArrayList;
import java.util.List;

public class NodoLog {
    private String accion;
    private List<NodoLog> siguientes; // acciones que siguen a esta

    public NodoLog(String accion) {
        this.accion = accion;
        this.siguientes = new ArrayList<>();
    }

    public String getAccion() {
        return accion;
    }

    public List<NodoLog> getSiguientes() {
        return siguientes;
    }

    public void agregarSiguiente(NodoLog nodo) {
        siguientes.add(nodo);
    }
}
