package juego;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LoggerPartida {
    private BufferedWriter writer;

    public LoggerPartida(String nombreArchivo) {
        try {
            writer = new BufferedWriter(new FileWriter(nombreArchivo, true)); // true = append mode
        } catch (IOException e) {
            System.err.println("Error al iniciar el logger: " + e.getMessage());
        }
    }

    public void log(String mensaje) {
        try {
            String tiempo = LocalDateTime.now().toString();
            writer.write("[" + tiempo + "] " + mensaje);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error al escribir en el log: " + e.getMessage());
        }
    }

    public void cerrar() {
        try {
            if (writer != null) writer.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar el logger: " + e.getMessage());
        }
    }
}
