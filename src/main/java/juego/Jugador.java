package juego;

import com.google.gson.annotations.Expose;
import estructuras.*;


public class Jugador {
    @Expose private String nombre;
    @Expose private MiLista<Unidad> unidades;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.unidades = new MiListaEnlazada<>();
    }

    public String getNombre() {
        return nombre;
    }

    public MiLista<Unidad> getUnidades() {
        return unidades;
    }

    public void agregarUnidad(Unidad unidad) {
        unidades.agregar(unidad);
    }

    public void eliminarUnidad(Unidad unidad) {
        unidades.eliminar(unidad);
    }
}
