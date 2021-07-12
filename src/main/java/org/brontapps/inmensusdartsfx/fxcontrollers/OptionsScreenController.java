package org.brontapps.inmensusdartsfx.fxcontrollers;

import org.brontapps.inmensusdartsfx.beans.GameInfo;
import org.brontapps.inmensusdartsfx.utils.Constants;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
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

    private Scene scene;

    private EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
                Node nodo = scene.getFocusOwner();
                if (nodo instanceof ToggleButton) {
                    ((ToggleButton) nodo).setSelected(true);
                    mouseEvent.consume();
                }
            }
    };

    private EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            KeyCode code = keyEvent.getCode();
            System.out.println("Options: " + code);
            if (code.equals(KeyCode.DOWN) || code.equals(KeyCode.UP) || code.equals(KeyCode.RIGHT)) {

                Parent focusedParent = scene.getFocusOwner().getParent();
                if (code.equals(KeyCode.RIGHT)) {
                	if (focusedParent.getId().equals("gameBox")) {
                        ((Node) gameGroup.getSelectedToggle()).requestFocus();
                        keyEvent.consume();
                	}
                }else if (code.equals(KeyCode.DOWN)) {
                    switch (focusedParent.getId()) {
                        case "numPlayersBox":
                            ((Node) gameGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "gameBox":
                            ((Node) x01ModeGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "x01ModeBox":
                            ((Node) modeGroup.getSelectedToggle()).requestFocus();
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
                    keyEvent.consume();
                } else if (code.equals(KeyCode.UP)) {
                    switch (focusedParent.getId()) {
                        case "numPlayersBox":
                            btnOptionsNext.requestFocus();
                            break;
                        case "gameBox":
                            ((Node) numPlayersGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "x01ModeBox":
                            ((Node) gameGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "modeBox":
                            ((Node) x01ModeGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "roundsBox":
                            ((Node) modeGroup.getSelectedToggle()).requestFocus();
                            break;
                        case "buttonsBox":
                            ((Node) roundsGroup.getSelectedToggle()).requestFocus();
                            break;
                    }
                    keyEvent.consume();

                }
            } else if (code.equals(KeyCode.ENTER)) {
                Node nodo = ((Node) keyEvent.getTarget());
                if (nodo instanceof ToggleButton) {
                    ((ToggleButton) nodo).setSelected(true);
                    keyEvent.consume();
                }
            }

        }
    };

    public void initOptions(Scene scene) {
        this.scene = scene;
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEventHandler);

//        gameGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//            @Override
//            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
//                x01ModeBox.setDisable(!X01Button.isSelected());
//                modeBox.setDisable(!CricketButton.isSelected());
//            }
//
//        });

    }


    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;
        stage = (Stage)scene.getWindow();
        scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
        scene.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseEventHandler);

        if (event.getSource() == btnOptionsBack) {
            root = FXMLLoader.load(getClass().getResource("pantalla_principal.fxml"), getStringsBundle());
            stage.getScene().setRoot(root);
        } else if (event.getSource() == btnOptionsNext) {
        	String numPlayers = getSelectedFromToggleGroup(numPlayersGroup, "1");
        	String gameType = getSelectedFromToggleGroup(gameGroup, "X01");
        	String rounds = getSelectedFromToggleGroup(roundsGroup, "10");
        	String mode = getSelectedFromToggleGroup(modeGroup, Constants.X01_SINGLEINSINGLEOUT);
        	String x01Mode = getSelectedFromToggleGroup(x01ModeGroup, "301");

        	gameInfo = new GameInfo(Integer.parseInt(
                    getSelectedFromToggleGroup(numPlayersGroup, "1")),
                    getSelectedFromToggleGroup(gameGroup, "X01"),
                    Integer.parseInt(getSelectedFromToggleGroup(roundsGroup, "10")),
                    getSelectedFromToggleGroup(modeGroup, Constants.X01_SINGLEINSINGLEOUT),
                    getSelectedFromToggleGroup(x01ModeGroup, "301"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_nombres.fxml"), getStringsBundle());
            root = loader.load();
            loader.<NamesScreenController>getController().initOptions(stage.getScene());
            stage.getScene().setRoot(root);
        }
    }

}
