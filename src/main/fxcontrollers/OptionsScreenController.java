package main.fxcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import main.beans.GameInfo;

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
            stage.setScene(new Scene(root));
            stage.show();
        } else if (event.getSource() == btnOptionsNext) {
            stage = (Stage) btnOptionsNext.getScene().getWindow();

            gameInfo = new GameInfo(Integer.parseInt(
                    getSelectedFromToggleGroup(numPlayersGroup)), getSelectedFromToggleGroup(gameGroup),
                    Integer.parseInt(getSelectedFromToggleGroup(roundsGroup)), getSelectedFromToggleGroup(modeGroup),
                    getSelectedFromToggleGroup(x01ModeGroup));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_nombres.fxml"), getStringsBundle());
            root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } else {

        }
    }

}
