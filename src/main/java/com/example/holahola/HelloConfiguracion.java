package com.example.holahola;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class HelloConfiguracion {
    @FXML
    private Label Variante;
    @FXML
    private TextField nombre;
    @FXML
    private ToggleGroup equipo;



    @FXML
    protected void onVolverButtonClick(ActionEvent event) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("hello-view.fxml"));
        try{
            Scene scene2 = new Scene(fxmlLoader.load(), 314.0, 272.0);
            stage.setTitle("CONQUISTA");
            stage.setScene(scene2);
            stage.show();

            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close();

        }catch (Exception  e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onSiguienteButtonClick(ActionEvent event) {
        String nombre2 = nombre.getText();
        if(nombre2.equalsIgnoreCase("xxx")){
            mostrarMensaje("Mete un nombre");
        }else {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Turnos.class.getResource("ConfiguracionTurnos.fxml"));

            try {
                RadioButton seleccion = (RadioButton) equipo.getSelectedToggle();
                String equipoTxt = seleccion.getText();

                Parent root = fxmlLoader.load();

                // Obtener el controlador de la nueva ventana
                Turnos controlador = fxmlLoader.getController();
                controlador.recibirDatos(nombre2, equipoTxt);


                Scene scene2 = new Scene(root, 349.0, 185.0);
                stage.setTitle("NUEVA PARTIDA CONFIGURACION");
                stage.setScene(scene2);
                stage.show();

                Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stageActual.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarMensaje(String sTexto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alerta");
        alert.setHeaderText("Información importante");
        alert.setContentText(sTexto);
        alert.showAndWait();
    }



}
