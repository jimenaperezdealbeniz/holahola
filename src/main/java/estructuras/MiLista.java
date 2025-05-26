package estructuras;

public interface MiLista<T> extends Iterable<T> {
    void agregar(T elemento);
    void agregar(int indice, T elemento);
    T obtener(int indice);
    T eliminar(int indice);
    boolean eliminar(T elemento);
    int tamano();
    boolean estaVacia();
    void limpiar();
    boolean contiene(T elemento);
    int indiceDe(T elemento);
}