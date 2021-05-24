package main.fxcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainScreenController extends BaseGuiController {

    @FXML
    private Button btn_main_bt_manual, btn_main_jugar;

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if (event.getSource() == btn_main_jugar) {
            stage = (Stage) btn_main_jugar.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
        } else {
            // bluetooth
            stage = (Stage) btn_main_jugar.getScene().getWindow();
            root = btn_main_bt_manual.getParent();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
