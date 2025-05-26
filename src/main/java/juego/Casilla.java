package juego;

public class Casilla {
    private int fila, columna;
    private int costeMovimiento = 1;
    private int modificadorDefensa = 0;
    public Unidad unidad;

    public Casilla(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public boolean estaOcupada() {
        return unidad != null;
    }

    public void colocarUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public void removerUnidad() {
        this.unidad = null;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getCosteMovimiento() {
        return costeMovimiento;
    }

    public void setCosteMovimiento(int costeMovimiento) {
        this.costeMovimiento = costeMovimiento;
    }

    public int getModificadorDefensa() {
        return modificadorDefensa;
    }

    public void setModificadorDefensa(int modificadorDefensa) {
        this.modificadorDefensa = modificadorDefensa;
    }

}
