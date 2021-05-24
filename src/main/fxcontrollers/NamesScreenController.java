package main.fxcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NamesScreenController extends BaseGuiController {

    @FXML
    private Button btn_nombres_atras, btn_nombres_continuar;


    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if (event.getSource() == btn_nombres_atras) {
            stage = (Stage) btn_nombres_atras.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
            stage.setScene(new Scene(root));
            stage.show();
        } else if (event.getSource() == btn_nombres_continuar) {
            // bluetooth
            stage = (Stage) btn_nombres_continuar.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_x01.fxml"), getStringsBundle());
            stage.setScene(new Scene(root));
            stage.show();
        } else {

        }
    }

}
