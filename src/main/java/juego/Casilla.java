package juego;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Casilla implements Serializable {
    @Expose private int fila, columna;
    @Expose private int costeMovimiento = 1;
    @Expose private int defensaModificador = 0;
    @Expose public Unidad unidadActual;

    public Casilla() {}
    public Casilla(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }
    public Posicion getPosicion() { return new Posicion(fila, columna); }

    public boolean estaOcupada() {
        return unidadActual != null;
    }

    public void setUnidadActual(Unidad unidadActual) {
        this.unidadActual = unidadActual;
    }

    public void removerUnidadActual() {
        this.unidadActual = null;
    }

    public Unidad getUnidadActual() {
        return unidadActual;
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

    public int getDefensaModificador() {
        return defensaModificador;
    }

    public void setDefensaModificador(int defensaModificador) {
        this.defensaModificador = defensaModificador;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Casilla casilla = (Casilla) o;
        return fila == casilla.fila &&
                columna == casilla.columna;
    }

    @Override
    public int hashCode() {
        // Un simple hash basado en fila y columna es suficiente para este caso.
        return 31 * fila + columna;
    }

    @Override
    public String toString() {
        return "Casilla(" + fila + ", " + columna + ")";
    }

}
