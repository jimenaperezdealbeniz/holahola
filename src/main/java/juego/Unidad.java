package juego;
public abstract class Unidad {
    protected int hp;
    protected int ataque;
    protected int defensa;
    protected int rangoMovimiento;
    protected int rangoAtaque;
    protected Posicion posicion;
    protected String nombre;

    public Unidad(String nombre,int hp, int ataque, int defensa, int rangoMovimiento, int rangoAtaque) {
        this.nombre = nombre;
        this.hp = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.rangoMovimiento = rangoMovimiento;
        this.rangoAtaque = rangoAtaque;
        this.posicion = new Posicion(0,0);
    }

    // Métodos getter y setter...
    public void setPosicion(Posicion nuevaPosicion) {
        this.posicion = nuevaPosicion;
    }

    public String getNombre(){
        return nombre;
    }

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

    public int getX() { return posicion.getX(); }

    public int getY() { return posicion.getY(); }

    public Posicion getPosicion() {
        return posicion;
    }
}
