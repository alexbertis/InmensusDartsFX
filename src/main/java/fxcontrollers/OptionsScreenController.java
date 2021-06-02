package fxcontrollers;

import beans.GameInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class OptionsScreenController extends BaseGuiController {

    @FXML
    private Button btnOptionsBack, btnOptionsNext;

    @FXML
    private ToggleGroup numPlayersGroup, gameGroup, x01ModeGroup, roundsGroup, modeGroup;

    @FXML
    private ToggleButton X01Button, CricketButton;

    @FXML
    private HBox modeBox, x01ModeBox;

    public void initOptions() {
        btnOptionsBack.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            System.out.println(code);
            if (code.equals(KeyCode.DOWN) || code.equals(KeyCode.UP)) {
                Scene scene = btnOptionsBack.getScene();
                Parent focusedParent = scene.getFocusOwner().getParent();
                if (code.equals(KeyCode.DOWN)) {
                    switch (focusedParent.getId()) {
                        case "numPlayersBox":
                            ((Node) gameGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "gameBox":
                            if (!x01ModeBox.isDisable())
                                ((Node) x01ModeGroup.getSelectedToggle()).requestFocus();
                            else
                                ((Node) modeGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "x01ModeBox":
                            ((Node) roundsGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "modeBox":
                            ((Node) roundsGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "roundsBox":
                            btnOptionsNext.requestFocus();
                            break;
                        case "buttonsBox":
                            ((Node) numPlayersGroup.getSelectedToggle()).requestFocus();
                            break;
                    }
                    event.consume();
                } else if (code.equals(KeyCode.UP)) {
                    switch (focusedParent.getId()) {
                        case "numPlayersBox":
                            btnOptionsNext.requestFocus();
                            break;
                        case "gameBox":
                            ((Node) numPlayersGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "x01ModeBox":
                        case "modeBox":
                            ((Node) gameGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "roundsBox":
                            if (!modeBox.isDisable())
                                ((Node) modeGroup.getSelectedToggle()).requestFocus();
                            else
                                ((Node) x01ModeGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "buttonsBox":
                            ((Node) roundsGroup.getSelectedToggle()).requestFocus();
                            break;
                    }
                    event.consume();
                }
            }
        });

        gameGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {

                x01ModeBox.setDisable(!X01Button.isSelected());
                modeBox.setDisable(!CricketButton.isSelected());
            }

        });

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
