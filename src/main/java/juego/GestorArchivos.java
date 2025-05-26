package juego;

import com.google.gson.*;
import java.io.*;

public class GestorArchivos {

    public static void guardarPartida(Partida partida, String rutaArchivo) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(rutaArchivo);
            gson.toJson(partida, writer);
            writer.close();
            System.out.println("Partida guardada en " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    public static Partida cargarPartida(String rutaArchivo) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(rutaArchivo);
            Partida partida = gson.fromJson(reader, Partida.class);
            reader.close();
            System.out.println("Partida cargada desde " + rutaArchivo);
            return partida;
        } catch (IOException e) {
            System.out.println("Error al cargar la partida: " + e.getMessage());
            return null;
        }
    }
}
