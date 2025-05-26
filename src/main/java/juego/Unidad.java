package juego;
public abstract class Unidad {
    protected int hp;
    protected int ataque;
    protected int defensa;
    protected int rangoMovimiento;
    protected int rangoAtaque;
    protected int x;
    protected int y;

    public Unidad(int hp, int ataque, int defensa, int rangoMovimiento, int rangoAtaque) {
        this.hp = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.rangoMovimiento = rangoMovimiento;
        this.rangoAtaque = rangoAtaque;
    }

    // Métodos getter y setter...
    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract String getNombre();

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }

    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }

    public int getRangoMovimiento() { return rangoMovimiento; }
    public void setRangoMovimiento(int rangoMovimiento) { this.rangoMovimiento = rangoMovimiento; }

    public int getRangoAtaque() { return rangoAtaque; }
    public void setRangoAtaque(int rangoAtaque) { this.rangoAtaque = rangoAtaque; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
}
