package juego;

import java.io.Serializable;

public class Poeta extends Unidad implements Serializable {
    public Poeta() { super(); }

    public Poeta(String nombre) {
        // HP, Ataque, Defensa, RangoMovimiento, RangoAtaque
        super(nombre,100, 12, 6, 4, 2);
    }
    @Override
    public String getNombre() {
        return super.getNombre(); // El nombre ya está en la clase base
    }
}
