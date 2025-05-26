package juego;

import java.io.Serializable;
import java.util.Objects;

public class NodoEntidad implements Serializable {
    private String id;      // Ej: "Ingeniero_J1_01", "(3,4)"
    private String tipo;    // Ej: "Unidad", "Casilla", "Objeto", etc.

    public NodoEntidad(String id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodoEntidad)) return false;
        NodoEntidad that = (NodoEntidad) o;
        return Objects.equals(id, that.id) && Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo);
    }

    @Override
    public String toString() {
        return tipo + ": " + id;
    }
}
