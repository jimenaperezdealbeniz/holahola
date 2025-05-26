package juego;

public class Tablero {
    private int filas;
    private int columnas;
    private Casilla[][] casillas;

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.casillas = new Casilla[filas][columnas];
        inicializarCasillas();
    }

    private void inicializarCasillas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                casillas[i][j] = new Casilla(i, j);
            }
        }
    }
    public boolean estaDentroDeLimites(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }
    public Casilla getCasilla(int fila, int columna) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            return casillas[fila][columna];
        }
        return null;
    }
}
