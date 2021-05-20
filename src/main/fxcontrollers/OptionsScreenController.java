package main.fxcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class OptionsScreenController implements Initializable {

    @FXML
    private Button btn_opciones_atras, btn_opciones_continuar;


    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;
        System.out.println("HOLAAAAAAAAA opciones");

        if (event.getSource() == btn_opciones_atras) {
            stage = (Stage) btn_opciones_atras.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_principal.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } else if (event.getSource() == btn_opciones_continuar) {
            // bluetooth
            stage = (Stage) btn_opciones_continuar.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_nombres.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } else {

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
