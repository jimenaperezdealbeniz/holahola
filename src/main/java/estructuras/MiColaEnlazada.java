package estructuras;

import java.util.NoSuchElementException;

public class MiColaEnlazada<T> implements MiCola<T> {

    private Nodo<T> frente;
    private Nodo<T> trasero;
    private int tamano;

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    public MiColaEnlazada() {
        this.frente = null;
        this.trasero = null;
        this.tamano = 0;
    }

    @Override
    public void encolar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (estaVacia()) {
            frente = nuevoNodo;
        } else {
            trasero.siguiente = nuevoNodo;
        }
        trasero = nuevoNodo;
        tamano++;
    }

    @Override
    public T desencolar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía.");
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) { // Si la cola queda vacía, el trasero también debe ser null
            trasero = null;
        }
        tamano--;
        return dato;
    }

    @Override
    public T frente() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía.");
        }
        return frente.dato;
    }

    @Override
    public boolean estaVacia() {
        return tamano == 0;
    }

    @Override
    public int tamano() {
        return tamano;
    }

    @Override
    public void limpiar() {
        frente = null;
        trasero = null;
        tamano = 0;
    }
}