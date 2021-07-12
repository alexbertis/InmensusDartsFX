package org.brontapps.inmensusdartsfx.fxcontrollers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.brontapps.inmensusdartsfx.beans.Gamer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class NamesScreenController extends BaseGuiController {

    @FXML
    private Button btnNamesBack, btnNamesNext;

    @FXML
    private HBox boxNombres1, boxNombres2, boxNombres3, boxNombres4, boxNombres5, boxNombres6, boxNombres7, boxNombres8;

    @FXML
    private TextField txtNames1, txtNames2, txtNames3, txtNames4, txtNames5, txtNames6, txtNames7, txtNames8;

    @FXML
    private VBox moreNamesBox;

    private Scene scene;

    private EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            KeyCode code = keyEvent.getCode();
            System.out.println("Names: " + code);
            if (code.equals(KeyCode.DOWN)){
                Node node = ((Scene)keyEvent.getSource()).getFocusOwner();
                if (node == btnNamesBack || node == btnNamesNext){
                    tfList.get(0).requestFocus();
                }else {
                    int index = tfList.indexOf(node);

                    if (index >= 0 && index < tfList.size() - 1) {
                        index++;
                        index = index % tfList.size();
                        tfList.get(index).requestFocus();
                    } else if (index == tfList.size() - 1) {
                        btnNamesNext.requestFocus();
                    }
                }
                keyEvent.consume();
            } else if(code.equals(KeyCode.UP)) {
                Node node = ((Scene)keyEvent.getSource()).getFocusOwner();
                if (node == btnNamesBack || node == btnNamesNext){
                    tfList.get(tfList.size() - 1).requestFocus();
                } else {
                    int index = tfList.indexOf(((Scene) keyEvent.getSource()).getFocusOwner());
                    if (index > 0) {
                        index--;
                        index = index % tfList.size();
                        tfList.get(index).requestFocus();
                    } else if (index == 0) {
                        btnNamesNext.requestFocus();
                    }
                }
                keyEvent.consume();

            }
        }
    };

    public HBox[] names;
    List<TextField> tfList;

    public void initOptions(Scene scene) {
        this.scene = scene;
        for (int i = 7; i >= gameInfo.getNumPlayers(); i--) {
            names[i].setVisible(false);
            tfList.remove(i);
        }
        if (gameInfo.getNumPlayers() <= 3)
            moreNamesBox.setVisible(false);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;
        stage = (Stage)scene.getWindow();
        scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);

        Node node = scene.getFocusOwner();
        if (node == btnNamesBack) {
            stage = (Stage) btnNamesBack.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
            root = loader.load();
            stage.getScene().setRoot(root);
            loader.<OptionsScreenController>getController().initOptions(stage.getScene());
        } else if (node == btnNamesNext) {
            // bluetooth
            stage = (Stage) btnNamesNext.getScene().getWindow();
            players = new ArrayList<>();
            boolean emptyField = false;
            for (TextField nameField : tfList) {
                String name = nameField.getText();
                if (name.isBlank()) {
                    name = nameField.getPromptText();
                }
                players.add(new Gamer(name, Integer.parseInt(gameInfo.getGameMode())));
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_x01.fxml"), getStringsBundle());
            root = loader.load();
            X01ScreenController x01ScreenController = loader.getController();
            x01ScreenController.createLayoutGamers();
            x01ScreenController.initGame(selectedDeviceName);
            x01ScreenController.escucharNulo(stage.getScene());
            stage.getScene().setRoot(root);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        names = new HBox[]{boxNombres1, boxNombres2, boxNombres3, boxNombres4, boxNombres5, boxNombres6, boxNombres7, boxNombres8};
        tfList = new LinkedList<TextField>(Arrays.asList(new TextField[]{txtNames1, txtNames2, txtNames3, txtNames4, txtNames5, txtNames6, txtNames7, txtNames8}));
    }
}
