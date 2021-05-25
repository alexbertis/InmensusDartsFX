package main.fxcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static main.utils.Constants.*;

public class MainScreenController extends BaseGuiController {

    @FXML
    private Button btnMainManualSetup, btnMainPlay;

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if (event.getSource() == btnMainPlay) {
            stage = (Stage) btnMainPlay.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
        } else {
            // bluetooth
            stage = (Stage) btnMainPlay.getScene().getWindow();
            root = btnMainManualSetup.getParent();
        }
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

}
