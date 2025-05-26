package juego;

import juego.AtaqueInvalidoException;
import juego.MovimientoInvalidoException;
import juego.Tablero;

public abstract class Unidad {
    protected int hp;
    protected int ataque;
    protected int defensa;
    protected int rangoMovimiento;
    protected int rangoAtaque;
    protected int x;
    protected int y;
    protected String nombre;

    public Unidad(int hp, int ataque, int defensa, int rangoMovimiento, int rangoAtaque) {
        this.hp = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.rangoMovimiento = rangoMovimiento;
        this.rangoAtaque = rangoAtaque;
    }

    // Métodos
    public boolean moverA(int nuevaX, int nuevaY, Tablero tablero) throws MovimientoInvalidoException {
        int distancia = Math.abs(nuevaX - x) + Math.abs(nuevaY - y);
        if (distancia > rangoMovimiento || !tablero.esCasillaValida(nuevaX, nuevaY)) {
            throw new MovimientoInvalidoException("Movimiento inválido.");
        }

        this.x = nuevaX;
        this.y = nuevaY;
        return true;
    }
    public String obtenerInformacion() {
        return nombre + " HP: " + hp + " Atk: " + ataque + " Def: " + defensa;
    }
    public void atacar(Unidad objetivo) throws AtaqueInvalidoException {
        int distancia = Math.abs(objetivo.x - x) + Math.abs(objetivo.y - y);
        if (distancia > rangoAtaque || this.getPersonaje().equals(objetivo.getPersonaje())) {
            throw new AtaqueInvalidoException("Ataque inválido.");
        }

        int factor = (int)(Math.random() * 3); // 0, 1 o 2
        int dano = factor * this.ataque - objetivo.defensa;
        if (dano > 0) {
            objetivo.hp -= dano;
        }
    }

    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract String getPersonaje();

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

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }


}