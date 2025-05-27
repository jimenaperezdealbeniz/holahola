package com.example.holahola;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import juego.*;




public class Turnos {
    @FXML  private TextField numeroTurnos;
    private String tamTablero;
    @FXML
    private ToggleGroup sizetab;
    private String equipoTxt;
    private String nombre;


    @FXML
    protected void onVolverButtonClick(ActionEvent event) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloConfiguracion.class.getResource("NuevaPartida.fxml"));
        try {
            Scene scene2 = new Scene(fxmlLoader.load(), 314.0, 272.0);
            stage.setTitle("CONQUISTA CONFIGURACION");
            stage.setScene(scene2);
            stage.show();

            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onSiguiente2ButtonClick(ActionEvent event) {
        int valor;
        try {
            valor = Integer.parseInt(numeroTurnos.getText());

            if (valor <= 5 && valor >= 25) {
                mostrarMensaje("Valor fuera de rango (debe estar entre 5 y 25)");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Error: Ingresa un número válido.");
            return;
        }

        RadioButton seleccion = (RadioButton) sizetab.getSelectedToggle();
        tamTablero = seleccion.getText();

        // Creamos la nueva escena
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Tablero.class.getResource("TableroPartida.fxml"));
        try {
            Parent root = fxmlLoader.load();

            // Crear partida real
            int filas = Integer.parseInt(tamTablero.split("X")[0]);
            int columnas = Integer.parseInt(tamTablero.split("X")[1]);
            Partida partida = new Partida(filas, columnas, nombre, "IA", valor);

// Pasar la partida al controlador del tablero
            Tablero controlador = fxmlLoader.getController();
            controlador.setPartida(partida);  // << ¡Esto es clave!



            // También puedes pasarle los datos como antes si quieres mantenerlos
            controlador.recibirDatos(nombre, equipoTxt, valor, tamTablero);

            // Continuar creando la escena
            Scene scene2 = new Scene(root, 636.0, 580.0);
            stage.setTitle("TABLERO");
            stage.setScene(scene2);
            stage.show();

            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void mostrarMensaje(String sTexto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alerta");
        alert.setHeaderText("Información importante");
        alert.setContentText(sTexto);
        alert.showAndWait();
    }

    public void recibirDatos(String nombre2, String equipoTxt) {
        this.nombre = nombre2;
        this.equipoTxt = equipoTxt;
    }
}