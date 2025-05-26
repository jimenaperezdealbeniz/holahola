public class Tablero {
    private int filas;
    private int columnas;
    private Casilla[][] casillas;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        casillas = new Casilla[filas][columnas];
        inicializarCasillas();
    }

    private void inicializarCasillas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Puedes personalizar este valor según el terreno
                casillas[i][j] = new Casilla(i, j, 1, 0);
            }
        }
    }

    public Casilla getCasilla(int x, int y) {
        if (x >= 0 && x < filas && y >= 0 && y < columnas) {
            return casillas[x][y];
        }
        return null;
    }

    public boolean moverUnidad(int origenX, int origenY, int destinoX, int destinoY) {
        Casilla origen = getCasilla(origenX, origenY);
        Casilla destino = getCasilla(destinoX, destinoY);

        if (origen == null || destino == null || !origen.estaOcupada() || destino.estaOcupada()) {
            return false;
        }

        Unidad unidad = origen.getUnidad();
        int coste = destino.getCosteMovimiento();

        if (unidad.getMovimientoRestante() >= coste) {
            unidad.setMovimientoRestante(unidad.getMovimientoRestante() - coste);
            destino.colocarUnidad(unidad);
            origen.quitarUnidad();
            return true;
        }

        return false;
    }

    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
}
