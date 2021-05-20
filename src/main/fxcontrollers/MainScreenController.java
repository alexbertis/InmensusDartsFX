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

public class MainScreenController implements Initializable {

    @FXML
    private Button btn_main_bt_manual, btn_main_jugar;

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;
        System.out.println("HOLAAAAAAAAA");

        if (event.getSource() == btn_main_jugar) {
            stage = (Stage) btn_main_jugar.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_opciones.fxml"));
        } else {
            // bluetooth
            stage = (Stage) btn_main_jugar.getScene().getWindow();
            root = btn_main_bt_manual.getParent();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
