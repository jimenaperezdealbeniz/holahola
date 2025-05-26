public class Casilla {
    private int x, y;
    private int costeMovimiento;
    private int modificadorDefensa;
    private Unidad unidad; // Puede ser null si está vacía

    public Casilla(int x, int y, int costeMovimiento, int modificadorDefensa) {
        this.x = x;
        this.y = y;
        this.costeMovimiento = costeMovimiento;
        this.modificadorDefensa = modificadorDefensa;
        this.unidad = null;
    }

    public boolean estaOcupada() {
        return unidad != null;
    }

    public void colocarUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public void quitarUnidad() {
        this.unidad = null;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getCosteMovimiento() { return costeMovimiento; }
    public int getModificadorDefensa() { return modificadorDefensa; }
    public Unidad getUnidad() { return unidad; }
}
