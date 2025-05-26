package com.example.holahola;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class HelloController {

    static int contadorVentanas = 0;

    @FXML
    private Label welcomeText;


    @FXML
    protected void onNuevoButtonClick(ActionEvent event) {


        welcomeText.setText("¡Nueva partida se ha dicho!");
        // Creo una nueva ventana
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloConfiguracion.class.getResource("NuevaPartida.fxml"));
        try{
            Scene scene2 = new Scene(fxmlLoader.load(), 350, 272.0);
            stage.setTitle("CONQUISTA CONFIGURACION PARTIDA");
            stage.setScene(scene2);
            stage.show();
            // Cierro la ventana actual
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close();



        }catch (Exception  e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onGuardadaButtonClick() {
        welcomeText.setText("Buscando la partida guardada...");
    }
    @FXML
    protected void onSalirButtonClick() {
        welcomeText.setText("¡Hasta pronto!");
        System.exit(0);
    }

    @FXML
    protected void onNuevaClick() {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewPartida.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 400, 400);
            stage.setTitle("CONQUISTA");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ParameterDataModel parametrosData = new ParameterDataModel(3,5,"Sara");
    private ParameterDataModelProperties modeloParaGUICompartido = new ParameterDataModelProperties(parametrosData);


}