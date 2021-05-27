package main.fxcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.beans.Gamer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static main.utils.Constants.WINDOW_HEIGHT;
import static main.utils.Constants.WINDOW_WIDTH;

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
            root = FXMLLoader.load(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
            stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
            stage.show();
        } else if (event.getSource() == btnNamesNext) {
            // bluetooth
            stage = (Stage) btnNamesNext.getScene().getWindow();
            players = new ArrayList<>();
            for (TextField nameField : textFields) {
                // FIXME: dejar de confiar en el parseInt porque puede haber otro "modo" de 301
                String name = nameField.getText();
                if (nameField.isVisible())
                    players.add(new Gamer(name, Integer.parseInt(gameInfo.getGameMode())));
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_x01.fxml"), getStringsBundle());
            root = loader.load();
            X01ScreenController x01ScreenController = loader.getController();
            x01ScreenController.createLayoutGamers();
            x01ScreenController.initGame(selectedDeviceName);
            stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
            stage.show();
        } else {

        }
    }

    public void hideTextFields(){
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
