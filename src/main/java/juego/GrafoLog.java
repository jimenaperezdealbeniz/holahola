package juego;

public class GrafoLog {
    private NodoLog raiz;
    private NodoLog ultimo;
    private LoggerPartida logger;  // logger para archivo

    public GrafoLog(LoggerPartida logger) {
        this.raiz = null;
        this.ultimo = null;
        this.logger = logger;
    }

    public void agregarAccion(String accion) {
        NodoLog nuevoNodo = new NodoLog(accion);
        if (raiz == null) {
            raiz = nuevoNodo;
        } else {
            ultimo.agregarSiguiente(nuevoNodo);
        }
        ultimo = nuevoNodo;

        // Loguear en archivo también
        if (logger != null) {
            logger.log(accion);
        }
    }

    public void imprimirGrafo() {
        imprimirNodo(raiz, 0);
    }

    private void imprimirNodo(NodoLog nodo, int nivel) {
        if (nodo == null) return;
        System.out.println("  ".repeat(nivel) + "- " + nodo.getAccion());
        for (NodoLog siguiente : nodo.getSiguientes()) {
            imprimirNodo(siguiente, nivel + 1);
        }
    }
}
