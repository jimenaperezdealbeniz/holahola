package juego;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Ingeniero extends Unidad implements Serializable {

    public Ingeniero() {super();}
    public Ingeniero(String nombre) {
        // HP, Ataque, Defensa, RangoMovimiento, RangoAtaque
        super(nombre,120, 15, 10, 3, 1);
    }
    @Override
    public String getNombre() {
        return super.getNombre(); // El nombre ya está en la clase base
    }
}
