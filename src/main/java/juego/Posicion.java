package juego;

import java.util.Objects;

public class Posicion {
    private int x;
    private int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int distanciaA(Posicion otra) {
        return Math.abs(this.x - otra.x) + Math.abs(this.y - otra.y); // Distancia Manhattan
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Posicion posicion = (Posicion) obj;
        return x == posicion.x && y == posicion.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
