import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private List<Unidad> unidades;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.unidades = new ArrayList<>();
    }

    public void agregarUnidad(Unidad unidad) {
        unidades.add(unidad);
    }

    public void eliminarUnidad(Unidad unidad) {
        unidades.remove(unidad);
    }

    public boolean tieneUnidades() {
        return !unidades.isEmpty();
    }

    public List<Unidad> getUnidades() {
        return unidades;
    }

    public String getNombre() {
        return nombre;
    }
}
