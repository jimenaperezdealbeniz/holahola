package juego;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import java.util.Objects; // Para Objects.hash y Objects.equals

public class Posicion implements Serializable {
    @Expose private int x;
    @Expose private int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Constructor vacío para Gson
    public Posicion() {}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Calcular distancia Manhattan
    public int distanciaA(Posicion otraPosicion) {
        return Math.abs(this.x - otraPosicion.x) + Math.abs(this.y - otraPosicion.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posicion posicion = (Posicion) o;
        return x == posicion.x && y == posicion.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}