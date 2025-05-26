package juego;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public abstract class Unidad implements Serializable {
    @Expose protected String nombre;
    @Expose protected int hp;
    @Expose protected int ataque;
    @Expose protected int defensa;
    @Expose protected int rangoMovimiento;
    @Expose protected int rangoAtaque;
    @Expose protected Posicion posicion;

    public Unidad() {
        this.posicion = new Posicion(); // Inicializar para evitar NPE
    }
    public Unidad(String nombre,int hp, int ataque, int defensa, int rangoMovimiento, int rangoAtaque) {
        this.nombre = nombre;
        this.hp = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.rangoMovimiento = rangoMovimiento;
        this.rangoAtaque = rangoAtaque;
        this.posicion = new Posicion(0,0);
    }

    // Métodos getter y setter...
    public void setPosicion(Posicion nuevaPosicion) {
        this.posicion = nuevaPosicion;
    }

    public String getNombre(){
        return nombre;
    }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }

    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }

    public int getRangoMovimiento() { return rangoMovimiento; }
    public void setRangoMovimiento(int rangoMovimiento) { this.rangoMovimiento = rangoMovimiento; }

    public int getRangoAtaque() { return rangoAtaque; }
    public void setRangoAtaque(int rangoAtaque) { this.rangoAtaque = rangoAtaque; }

    public void setX(int x) { this.posicion.setX(x); }
    public int getX() { return posicion.getX(); }

    public void setY(int y) { this.posicion.setY(y); }
    public int getY() { return posicion.getY(); }

    public Posicion getPosicion() {
        return posicion;
    }
    public boolean estaViva() {
        return hp > 0;
    }

    public void recibirDano(int dano) {
        this.hp -= dano;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }
}
