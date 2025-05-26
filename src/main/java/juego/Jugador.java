package juego;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private List<Unidad> unidades;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.unidades = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Unidad> getUnidades() {
        return unidades;
    }

    public void agregarUnidad(Unidad unidad) {
        unidades.add(unidad);
    }

    public void removerUnidad(Unidad unidad) {
        unidades.remove(unidad);
    }
}
