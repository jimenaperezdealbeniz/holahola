package estructuras;

public interface MiCola<T> {
    void encolar(T elemento); // Añade un elemento al final de la cola
    T desencolar(); // Elimina y devuelve el elemento del frente de la cola
    T frente(); // Devuelve el elemento del frente sin eliminarlo
    boolean estaVacia();
    int tamano();
    void limpiar();
}