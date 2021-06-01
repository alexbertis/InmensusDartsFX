package fxcontrollers;

import beans.Gamer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class NamesScreenController extends BaseGuiController {

    @FXML
    private Button btnNamesBack, btnNamesNext;

    @FXML
    private TextField txtNames1, txtNames2, txtNames3, txtNames4, txtNames5, txtNames6, txtNames7, txtNames8;

    public TextField[] textFields;


    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if (event.getSource() == btnNamesBack) {
            stage = (Stage) btnNamesBack.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
            root = loader.load();
            stage.getScene().setRoot(root);
            loader.<OptionsScreenController>getController().initOptions();
        } else if (event.getSource() == btnNamesNext) {
            // bluetooth
            stage = (Stage) btnNamesNext.getScene().getWindow();
            players = new ArrayList<>();
            boolean emptyField = false;
            for (TextField nameField : textFields) {
                // FIXME: dejar de confiar en el parseInt porque puede haber otro "modo" de 301
                String name = nameField.getText();
                if (nameField.isVisible()) {
                    if (name.isBlank()) {
                        emptyField = true;
                        break;
                    }
                    players.add(new Gamer(name, Integer.parseInt(gameInfo.getGameMode())));
                }
            }
            if (emptyField) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Rellenar nombres");
                alert.setHeaderText("Campos para nombres no rellenados");
                alert.setContentText("Rellena todos los nombres para poder continuar al juego");
                alert.show();
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_x01.fxml"), getStringsBundle());
                root = loader.load();
                X01ScreenController x01ScreenController = loader.getController();
                x01ScreenController.createLayoutGamers();
                x01ScreenController.initGame(selectedDeviceName);
                x01ScreenController.escucharNulo(stage.getScene());
                stage.getScene().setRoot(root);
            }
        } else {

        }
    }

    public void hideTextFields() {
        for (int i = 7; i >= gameInfo.getNumPlayers(); i--) {
            textFields[i].setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        textFields = new TextField[]{txtNames1, txtNames2, txtNames3, txtNames4, txtNames5, txtNames6, txtNames7, txtNames8};
    }
}
