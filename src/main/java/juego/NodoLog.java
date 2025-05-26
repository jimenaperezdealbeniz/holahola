package juego;

import java.io.Serializable;
import com.google.gson.annotations.Expose;

public class NodoLog implements Serializable {
    @Expose private String mensaje;

    public NodoLog(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    // Si quieres un grafo real, aquí necesitarías una lista de nodos a los que apunta
    // private MiLista<NodoLog> siguientes;
}