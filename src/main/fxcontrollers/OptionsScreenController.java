package main.fxcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class OptionsScreenController extends BaseGuiController {

    @FXML
    private Button btn_opciones_atras, btn_opciones_continuar;


    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;
        System.out.println("HOLAAAAAAAAA opciones");

        if (event.getSource() == btn_opciones_atras) {
            stage = (Stage) btn_opciones_atras.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_principal.fxml"), getStringsBundle());
            stage.setScene(new Scene(root));
            stage.show();
        } else if (event.getSource() == btn_opciones_continuar) {
            // bluetooth
            stage = (Stage) btn_opciones_continuar.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_nombres.fxml"), getStringsBundle());
            stage.setScene(new Scene(root));
            stage.show();
        } else {

        }
    }

}
