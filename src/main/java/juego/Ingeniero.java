package juego;
public class Ingeniero extends Unidad {
    public Ingeniero() {
        // HP, Ataque, Defensa, RangoMovimiento, RangoAtaque
        super(120, 15, 10, 3, 1);
    }

    @Override
    public String getPersonaje() {
        return "Ingeniero";
    }
}
