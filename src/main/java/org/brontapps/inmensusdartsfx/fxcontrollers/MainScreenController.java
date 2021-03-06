package org.brontapps.inmensusdartsfx.fxcontrollers;

import com.fazecast.jSerialComm.SerialPort;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainScreenController extends BaseGuiController {

    Timeline timeline;

    @FXML
    private Button btnMainManualSetup, btnMainPlay;

    @FXML
    private ComboBox<String> selectDeviceMain;

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        if (event.getSource() == btnMainPlay) {
            Stage stage = (Stage) btnMainPlay.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
            Parent root = loader.load();
            stage.getScene().setRoot(root);
            loader.<OptionsScreenController>getController().initOptions(stage.getScene());
            if (timeline != null)
                timeline.stop();
        }
    }

    public void loadComboOptions() {
        timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            String previousPort = selectDeviceMain.getValue();
            String selectedPort = null;
            System.out.println("Scanning ports again...");
            SerialPort[] serialPorts = SerialPort.getCommPorts();
            String ports[] = new String[serialPorts.length];
            for (int i = 0; i < serialPorts.length; i++) {
            	ports[i] = serialPorts[i].getPortDescription();
            	if (ports[i].toUpperCase().contains("USB"))
            		selectedPort = ports[i];
            }
            	
            selectDeviceMain.getItems().setAll(ports);
            if (ports.length > 0) {
            	if (selectedPort != null) {
            		selectDeviceMain.setValue(selectedPort);
            	} else if (previousPort == null || previousPort.isBlank()) {
                    selectDeviceMain.setValue(ports[0]);
                } else {
                    selectDeviceMain.setValue(previousPort);
                }
            } else {
                selectDeviceMain.setValue("");
            }
        }), new KeyFrame(Duration.seconds(5)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        selectDeviceMain.setOnAction(actionEvent -> {
            selectedDeviceName = selectDeviceMain.getValue();
        });
    }
}
