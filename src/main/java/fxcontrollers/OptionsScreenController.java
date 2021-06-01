package fxcontrollers;

import beans.GameInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class OptionsScreenController extends BaseGuiController {

    @FXML
    private Button btnOptionsBack, btnOptionsNext;

    @FXML
    private ToggleGroup numPlayersGroup, gameGroup, x01ModeGroup, roundsGroup, modeGroup;

    @FXML
    private void handleKeyEvent(KeyEvent event) {
        KeyCode code = event.getCode();
        System.out.println(code);
        Scene scene = btnOptionsBack.getScene();
        Parent focusedParent = scene.getFocusOwner().getParent();
        if (scene.getFocusOwner().equals(btnOptionsBack) || scene.getFocusOwner().equals(btnOptionsNext))
            return;
        if (code.equals(KeyCode.DOWN) || code.equals(KeyCode.ENTER)){
            switch (focusedParent.getId()){
                case "numPlayersBox":
                    ((ToggleButton)gameGroup.getToggles().get(0)).requestFocus();
                    break;
                case "gameBox":
                    ((ToggleButton)x01ModeGroup.getToggles().get(0)).requestFocus();
                    break;
                case "x01ModeBox":
                    ((ToggleButton)roundsGroup.getToggles().get(0)).requestFocus();
                    break;
                case "roundsBox":
                    ((ToggleButton)modeGroup.getToggles().get(0)).requestFocus();
                    break;
                case "modeBox":
                    btnOptionsNext.requestFocus();
                    break;
            }
        } else if (code.equals(KeyCode.UP)){
            switch (focusedParent.getId()){
                case "numPlayersBox":
                    btnOptionsBack.requestFocus();
                    break;
                case "gameBox":
                    ((ToggleButton)numPlayersGroup.getToggles().get(0)).requestFocus();
                    break;
                case "x01ModeBox":
                    ((ToggleButton)gameGroup.getToggles().get(0)).requestFocus();
                    break;
                case "roundsBox":
                    ((ToggleButton)x01ModeGroup.getToggles().get(0)).requestFocus();
                    break;
                case "modeBox":
                    ((ToggleButton)roundsGroup.getToggles().get(0)).requestFocus();
                    break;
            }
        }
    }

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
