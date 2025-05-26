package juego;
public class Poeta extends Unidad {
    public Poeta() {
        // HP, Ataque, Defensa, RangoMovimiento, RangoAtaque
        super(100, 12, 6, 4, 2);
    }

    @Override
    public String getNombre() {
        return "Poeta";
    }
}
