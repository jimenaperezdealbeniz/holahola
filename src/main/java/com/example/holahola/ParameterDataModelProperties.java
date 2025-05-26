package com.example.holahola;

import javafx.beans.property.*;

public class ParameterDataModelProperties {
    protected ParameterDataModel original;

    private IntegerProperty vida = new SimpleIntegerProperty();
    private IntegerProperty movimientos = new SimpleIntegerProperty();
    private StringProperty nombre = new SimpleStringProperty();

    public ParameterDataModelProperties(ParameterDataModel original) {
        setOriginal(original);
    }
    public void commit(){
        original.setMovimientos(movimientos.get());
        original.setVida(vida.get());
        original.setNombre(nombre.get());
    }
    public void rollback(){
        movimientos.set(original.getMovimientos());
        vida.set(original.getVida());
        nombre.set(original.getNombre());
    }

    public ParameterDataModel getOriginal() {
        return original;
    }
    public void setOriginal(ParameterDataModel original) {
        this.original = original;
        rollback();
    }

    public Property<Number> vidaProperty() {
        return vida;
    }
    public Property<Number> movimientosProperty() {
        return movimientos;
    }
    public Property<String> nombreProperty() {
        return nombre;
    }
}
