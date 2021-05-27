package main.fxcontrollers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import jssc.SerialPortList;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static main.utils.Constants.*;

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
            Parent root = FXMLLoader.load(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            stage.setScene(scene);
            stage.show();
            if (timeline != null)
                timeline.stop();
        }
    }

    public void loadComboOptions(){
        timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            String previousPort = selectDeviceMain.getValue();
            System.out.println("Scanning ports again...");
            String[] ports = SerialPortList.getPortNames();
            selectDeviceMain.getItems().setAll(ports);
            if (ports.length > 0){
                if (previousPort == null || previousPort.isBlank()) {
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
