package fxcontrollers;

import beans.GameInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;


public class OptionsScreenController extends BaseGuiController {

    @FXML
    private Button btnOptionsBack, btnOptionsNext;

    @FXML
    private ToggleGroup numPlayersGroup, gameGroup, x01ModeGroup, roundsGroup, modeGroup;

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if (event.getSource() == btnOptionsBack) {
            stage = (Stage) btnOptionsBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_principal.fxml"), getStringsBundle());
            stage.getScene().setRoot(root);
        } else if (event.getSource() == btnOptionsNext) {
            stage = (Stage) btnOptionsNext.getScene().getWindow();

            gameInfo = new GameInfo(Integer.parseInt(
                    getSelectedFromToggleGroup(numPlayersGroup, "1")),
                    getSelectedFromToggleGroup(gameGroup, "X01"),
                    Integer.parseInt(getSelectedFromToggleGroup(roundsGroup, "10")),
                    getSelectedFromToggleGroup(modeGroup, "Single In Single Out"),
                    getSelectedFromToggleGroup(x01ModeGroup, "301"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_nombres.fxml"), getStringsBundle());
            root = loader.load();
            loader.<NamesScreenController>getController().hideTextFields();
            stage.getScene().setRoot(root);
        } else {

        }
    }

}
