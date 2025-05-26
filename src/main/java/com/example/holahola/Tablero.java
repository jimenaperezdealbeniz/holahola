package com.example.holahola;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import com.google.gson.JsonArray;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Tablero {
    private String nombre;
    private String equipoTxt;
    private int numTurnos;
    private String tamTablero;
    @FXML
    private Label welcomeText;
    @FXML
    private GridPane tablero;
    @FXML
    private Label name;
    @FXML
    protected void onInicioButtonClick(){
        tablero.getChildren().clear();
        tablero.setHgap(0);
        tablero.getColumnConstraints().clear();
        tablero.getRowConstraints().clear();


        String[] tamTableroArray = tamTablero.split("X");
        int iNumFilas= Integer.parseInt(tamTableroArray[0]);
        int iNumColumnas= Integer.parseInt(tamTableroArray[1]);



        for (int row = 0; row < iNumFilas; row++) {
            for (int col = 0; col < iNumColumnas; col++) {
                Label placeholder = new Label(" ");
                placeholder.setStyle("-fx-border-color: black; -fx-background-color: pink");
                placeholder.setPrefSize(60,60);
                tablero.add(placeholder, col, row); // Agregar al GridPane
            }
        }
        name.setText(nombre);

    }
    @FXML
    protected void onCerrarButtonClick(){
        System.exit(0);
    }
    @FXML
    protected void onGuardarButtonClick(){
        //JsonArray jsonArray = new JSONArray();
        JsonArray jsonArray = new JsonArray();
        for (Node node : tablero.getChildren()) {
            if (node instanceof Label) { // Verificar si la celda es un Label
                int col = GridPane.getColumnIndex(node);
                int row = GridPane.getRowIndex(node);
                String valor = ((Label) node).getText();

                // Crear un objeto JSON por celda
                JsonObject celdaJSON = new JsonObject();
                celdaJSON.addProperty("fila", row);
                celdaJSON.addProperty("columna", col);
                celdaJSON.addProperty("valor", valor);

                jsonArray.add(celdaJSON);
            }
        }

        //System.out.println(jsonArray.toString());
        // Procedo a guardar el json en el temporal
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String fechaHora = LocalDateTime.now().format(formatter);
        String filename = System.getProperty("java.io.tmpdir") + "/tablero_" + fechaHora + ".json";

        // Save JSON to file
        try (FileWriter writer = new FileWriter(filename)) {
            new Gson().toJson(jsonArray, writer);
            System.out.println("JSON file guardada en la ruta: " + filename);
        } catch (IOException e) {
            System.err.println("Error guardando el json file: " + e.getMessage());
        }


    }

    public void recibirDatos(String nombre, String equipoTxt, int numTurnos, String tamTablero) {
        this.nombre = nombre;
        this.equipoTxt = equipoTxt;
        this.numTurnos = numTurnos;
        this.tamTablero = tamTablero;
    }

    public void mostrarMensaje(String sTexto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alerta");
        alert.setHeaderText("Información importante");
        alert.setContentText(sTexto);
        alert.showAndWait();
    }





}
