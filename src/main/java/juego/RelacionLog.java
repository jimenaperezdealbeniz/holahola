package juego;

import java.io.Serializable;

public class RelacionLog implements Serializable {
    private NodoEntidad origen;
    private NodoEntidad destino;
    private String accion;
    private int turno;
    private String timestamp;

    public RelacionLog(NodoEntidad origen, NodoEntidad destino, String accion, int turno, String timestamp) {
        this.origen = origen;
        this.destino = destino;
        this.accion = accion;
        this.turno = turno;
        this.timestamp = timestamp;
    }

    public NodoEntidad getOrigen() {
        return origen;
    }

    public void setOrigen(NodoEntidad origen) {
        this.origen = origen;
    }

    public NodoEntidad getDestino() {
        return destino;
    }

    public void setDestino(NodoEntidad destino) {
        this.destino = destino;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] "
                + origen + " --" + accion + "--> " + destino
                + " (Turno " + turno + ")";
    }
}
