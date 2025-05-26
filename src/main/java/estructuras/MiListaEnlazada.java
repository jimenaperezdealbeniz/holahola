package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MiListaEnlazada<T> implements MiLista<T> {

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamano;

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    public MiListaEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamano = 0;
    }

    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (estaVacia()) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            cola.siguiente = nuevoNodo;
            cola = nuevoNodo;
        }
        tamano++;
    }

    @Override
    public void agregar(int indice, T elemento) {
        if (indice < 0 || indice > tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de límites: " + indice);
        }
        if (indice == tamano) {
            agregar(elemento);
            return;
        }
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (indice == 0) {
            nuevoNodo.siguiente = cabeza;
            cabeza = nuevoNodo;
        } else {
            Nodo<T> actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.siguiente;
            }
            nuevoNodo.siguiente = actual.siguiente;
            actual.siguiente = nuevoNodo;
        }
        tamano++;
    }

    @Override
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de límites: " + indice);
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.dato;
    }

    @Override
    public T eliminar(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de límites: " + indice);
        }
        T datoEliminado;
        if (indice == 0) {
            datoEliminado = cabeza.dato;
            cabeza = cabeza.siguiente;
            if (cabeza == null) { // La lista quedó vacía
                cola = null;
            }
        } else {
            Nodo<T> actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.siguiente;
            }
            datoEliminado = actual.siguiente.dato;
            actual.siguiente = actual.siguiente.siguiente;
            if (actual.siguiente == null) { // Eliminamos la cola
                cola = actual;
            }
        }
        tamano--;
        return datoEliminado;
    }

    @Override
    public boolean eliminar(T elemento) {
        if (estaVacia()) {
            return false;
        }
        if (cabeza.dato.equals(elemento)) {
            cabeza = cabeza.siguiente;
            if (cabeza == null) {
                cola = null;
            }
            tamano--;
            return true;
        }
        Nodo<T> actual = cabeza;
        while (actual.siguiente != null && !actual.siguiente.dato.equals(elemento)) {
            actual = actual.siguiente;
        }
        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
            if (actual.siguiente == null) { // Eliminamos la cola
                cola = actual;
            }
            tamano--;
            return true;
        }
        return false;
    }

    @Override
    public int tamano() {
        return tamano;
    }

    @Override
    public boolean estaVacia() {
        return tamano == 0;
    }

    @Override
    public void limpiar() {
        cabeza = null;
        cola = null;
        tamano = 0;
    }

    @Override
    public boolean contiene(T elemento) {
        return indiceDe(elemento) != -1;
    }

    @Override
    public int indiceDe(T elemento) {
        Nodo<T> actual = cabeza;
        int indice = 0;
        while (actual != null) {
            if (actual.dato.equals(elemento)) {
                return indice;
            }
            actual = actual.siguiente;
            indice++;
        }
        return -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new MiListaEnlazadaIterator();
    }

    private class MiListaEnlazadaIterator implements Iterator<T> {
        private Nodo<T> actual = cabeza;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = actual.dato;
            actual = actual.siguiente;
            return data;
        }
    }
}