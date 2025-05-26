public abstract class Unidad {
    protected int hp;
    protected int ataque;
    protected int defensa;
    protected int rangoMovimiento;
    protected int rangoAtaque;
    protected int movimientoRestante;

    public Unidad(int hp, int ataque, int defensa, int rangoMovimiento, int rangoAtaque) {
        this.hp = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.rangoMovimiento = rangoMovimiento;
        this.rangoAtaque = rangoAtaque;
        this.movimientoRestante = rangoMovimiento;
    }

    public void reiniciarMovimiento() {
        this.movimientoRestante = rangoMovimiento;
    }

    // Getters y setters necesarios...
    public int getMovimientoRestante() { return movimientoRestante; }
    public void setMovimientoRestante(int m) { this.movimientoRestante = m; }

    // Agrega métodos como recibirDaño(), atacar(), etc.
}
