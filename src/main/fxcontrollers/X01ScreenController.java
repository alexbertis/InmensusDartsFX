package main.fxcontrollers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.beans.Gamer;
import purejavacomm.CommPortIdentifier;
import purejavacomm.SerialPort;
import purejavacomm.SerialPortEvent;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class X01ScreenController extends BaseGuiController {

    @FXML
    private Text txtShot1, txtShot2, txtShot3;

    @FXML
    private GridPane padreJugadores;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

    }

    public void attachSerialListener(String deviceName) throws Exception {
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(deviceName);

        // Opening the port
        SerialPort port = (SerialPort) portId.open("InmensusDarts", 1000);

        //OutputStream outStream = port.getOutputStream();
        InputStream inStream = port.getInputStream();
        Scanner scanner = new Scanner(inStream);

        port.addEventListener(serialPortEvent -> {
            if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE && serialPortEvent.getNewValue()) {
                String line = scanner.nextLine();
                // TODO: hacer cosas con la linea y en algun momento romper el bucle
            }
        });
    }

    public void createLayoutGamers() {
        boolean addEmptyLayout = players.size() > 2 && players.size() % 2 != 0;
        System.out.printf("Initializing layout for %d players. second row with spaces? %b\n", players.size(), addEmptyLayout);


        if (players.size() > 2) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(50);
            rowConstraints.setFillHeight(true);
            padreJugadores.getRowConstraints().addAll(rowConstraints, rowConstraints);
        } else {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100);
            rowConstraints.setFillHeight(true);
            padreJugadores.getRowConstraints().add(rowConstraints);
        }

        int corte = (int) Math.ceil(players.size() / 2.0);

        double percentHalfColumn = 100.0 / (2*corte);
        for (int i = 0; i < 2*corte; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(percentHalfColumn);
            constraints.setFillWidth(true);
            padreJugadores.getColumnConstraints().add(constraints);
        }


        float fontSizeName = getFontSizeName(corte);
        float fontSizePoints = getFontSizePoints(corte);
        for (int i = 0; i < players.size(); i++) {
            Gamer gamer = players.get(i);
            int colNumber = 2 * (i%corte);
            if (players.size() <= 2 || i < corte) {
                appendGamerLinearLayout(gamer, fontSizeName, fontSizePoints, padreJugadores, 0, colNumber);
            } else {
                appendGamerLinearLayout(gamer, fontSizeName, fontSizePoints, padreJugadores, 1, addEmptyLayout? colNumber + 1 : colNumber);
            }
        }
    }

    private void appendGamerLinearLayout(Gamer gamer, float sizeName, float sizePoints, GridPane root, int rowIndex, int colIndex) {
        VBox gamerLinearLayout = new VBox();
        VBox.setVgrow(gamerLinearLayout, Priority.ALWAYS);
        gamerLinearLayout.setMaxWidth(Double.POSITIVE_INFINITY);
        gamerLinearLayout.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFA"), new CornerRadii(30), new Insets(10))));
        //gamerLinearLayout.setBorder(new Border(new BorderStroke(Paint.valueOf("#000"))));
        gamerLinearLayout.setAlignment(Pos.CENTER);

        Text tvGamerName = new Text(gamer.getName());
        tvGamerName.getStyleClass().add("gamer-name");
        //tvGamerName.setFont(ResourcesCompat.getFont(this, R.font.malvie));
        tvGamerName.setTextAlignment(TextAlignment.CENTER);
        tvGamerName.setFont(Font.font(sizeName));
        //tvGamerName.(Color.parseColor("#000066"));
        //tvGamerName.setLayoutParams(linearLayoutGamerLp1);

        Text tvGamerPoints = new Text(String.valueOf(gamer.getPuntuacion()));
        tvGamerPoints.setTextAlignment(TextAlignment.CENTER);
        tvGamerPoints.setFont(Font.font(sizePoints));
        //tvGamerPoints.setTextColor(Color.parseColor("#000000"));
        gamer.setTextViewPuntuacion(tvGamerPoints);

        gamerLinearLayout.getChildren().addAll(tvGamerName, tvGamerPoints);
        gamer.setLinearLayout(gamerLinearLayout);

        root.add(gamerLinearLayout, colIndex, rowIndex, 2, 1);
    }

    private float getFontSizeName(int corte){
        float fontSize = 30.0f;
        switch (corte){
            case 1:
                fontSize = 60.0f;
                break;
            case 2:
                fontSize = 40.0f;
                break;
            case 3:
                fontSize = 25.0f;
                break;
            case 4:
                fontSize = 20.0f;
                break;

        }
        return fontSize;
    }

    private float getFontSizePoints(int corte){
        float fontSize = 30.0f;
        switch (corte){
            case 1:
                fontSize = 120.0f;
                break;
            case 2:
                fontSize = 80.0f;
                break;
            case 3:
                fontSize = 70.0f;
                break;
            case 4:
                fontSize = 60.0f;
                break;

        }
        return fontSize;
    }
}
