package org.brontapps.inmensusdartsfx.fxcontrollers;

import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import org.brontapps.inmensusdartsfx.beans.DatosTirada;
import org.brontapps.inmensusdartsfx.beans.Gamer;
import org.brontapps.inmensusdartsfx.beans.WaitInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
//import purejavacomm.CommPortIdentifier;
//import purejavacomm.SerialPort;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class X01ScreenController extends BaseGuiController {

    private final WaitInfo waitInfo = new WaitInfo();
    int jugadorActual = 0;
    int round = 1;
    int tirada = 1;
    int totalTirada = 0;
    int puntuacionInicial = 0;
    Gamer winner = null;

    private SerialPort port;
    private Timer timer;

    @FXML
    private Text txtShot1, txtShot2, txtShot3, txtShotTotal, txtRoundNumber;
    @FXML
    private HBox contShot1, contShot2, contShot3;
    @FXML
    private GridPane padreJugadores;

    private void attachSerialListener(String deviceName) throws Exception {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort puerto : ports){
            if (puerto.getSystemPortName().equals(deviceName)){
                port= puerto;
                break;
            }
        }

        // Opening the port
        if (port != null) {
            boolean isOpen = port.openPort();
            
            port.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        return;
                    byte[] newData = new byte[port.bytesAvailable()];
                    int numRead = port.readBytes(newData, newData.length);
                    String receivedData = new String(newData);
                    if (!waitInfo.isWaiting()) {
                        tirada(receivedData.trim());
                    }
                }

            });
        }
    }

    public void initGame(String usbDeviceName) {
        try {
            attachSerialListener(usbDeviceName);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (Gamer gamer : players) {
            setInactivePlayer(gamer.getLinearLayout());
            gamer.getLinearLayout().setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(4))));
        }
        setActivePlayer(players.get(0).getLinearLayout());

        round = 1;
        tirada = 1;
        jugadorActual = 0;
        totalTirada = 0;
        winner = null;

        txtRoundNumber.setText(String.valueOf(round));
        clearTirada();

        waitInfo.setWaiting(false);

    }

    public void escucharNulo(Scene scene) {
        if (scene != null)
            scene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode().equals(KeyCode.SPACE) && !waitInfo.isWaiting()) tirada("0");
            });
    }

    private void clearTirada() {
        totalTirada = 0;
        txtShotTotal.setText(getStringTotal(totalTirada));

        contShot1.setVisible(false);
        contShot2.setVisible(false);
        contShot3.setVisible(false);
    }

    public void createLayoutGamers() {
        boolean addEmptyLayout = players.size() > 2 && players.size() % 2 != 0;

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

        int corte = players.size() > 2 ? (int) Math.ceil(players.size() / 2.0) : players.size();

        double percentHalfColumn = 100.0 / (2 * corte);
        for (int i = 0; i < 2 * corte; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(percentHalfColumn);
            constraints.setFillWidth(true);
            padreJugadores.getColumnConstraints().add(constraints);
        }


        float fontSizeName = getFontSizeName(corte);
        float fontSizePoints = getFontSizePoints(corte);
        for (int i = 0; i < players.size(); i++) {
            Gamer gamer = players.get(i);
            int colNumber = 2 * (i % corte);
            if (players.size() <= 2 || i < corte) {
                appendGamerLinearLayout(gamer, fontSizeName, fontSizePoints, padreJugadores, 0, colNumber);
            } else {
                appendGamerLinearLayout(gamer, fontSizeName, fontSizePoints, padreJugadores, 1, addEmptyLayout ? colNumber + 1 : colNumber);
            }
        }
    }

    private void appendGamerLinearLayout(Gamer gamer, float sizeName, float sizePoints, GridPane root, int rowIndex, int colIndex) {
        GridPane gamerPane = new GridPane();
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(40.0);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(60.0);
        gamerPane.getRowConstraints().addAll(row1, row2);

        gamerPane.setMaxWidth(Double.POSITIVE_INFINITY);
        gamerPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFA"), new CornerRadii(30), new Insets(10))));
        gamerPane.setAlignment(Pos.CENTER);

        Text tvGamerName = new Text(gamer.getName());
        tvGamerName.getStyleClass().add("gamer-name");
        tvGamerName.setTextAlignment(TextAlignment.CENTER);
        tvGamerName.setFont(Font.font(sizeName));

        Text tvGamerPoints = new Text(String.valueOf(gamer.getPuntuacion()));
        tvGamerPoints.setTextAlignment(TextAlignment.CENTER);
        tvGamerPoints.setFont(Font.font(sizePoints));
        tvGamerPoints.getStyleClass().add("gamer-points");
        gamer.setTextViewPuntuacion(tvGamerPoints);

        gamerPane.add(tvGamerName,0, 0);
        gamerPane.add(tvGamerPoints,0, 1);
        gamerPane.setValignment(tvGamerName, VPos.TOP);
        gamerPane.setValignment(tvGamerPoints, VPos.TOP);
        gamerPane.setHalignment(tvGamerName, HPos.CENTER);
        gamerPane.setHalignment(tvGamerPoints, HPos.CENTER);
        gamer.setLinearLayout(gamerPane);

        root.add(gamerPane, colIndex, rowIndex, 2, 1);
    }

    private float getFontSizeName(int corte) {
        float fontSize = 30.0f;
        switch (corte) {
            case 1:
                fontSize = 90.0f;
                break;
            case 2:
                fontSize = 60.0f;
                break;
            case 3:
                fontSize = 30.0f;
                break;
            case 4:
                fontSize = 20.0f;
                break;

        }
        return fontSize;
    }

    private float getFontSizePoints(int corte) {
        float fontSize = 30.0f;
        switch (corte) {
            case 1:
                fontSize = 150.0f;
                break;
            case 2:
                fontSize = 120.0f;
                break;
            case 3:
                fontSize = 90.0f;
                break;
            case 4:
                fontSize = 60.0f;
                break;

        }
        return fontSize;
    }


    private void tirada(String nuevosPuntos) {
        if (nuevosPuntos.contains("NeoDardos") || nuevosPuntos.isBlank()) {
            return;
        }

        int puntosParaRestar = 0;
        DatosTirada datosTirada = codigoAPuntos(nuevosPuntos);
        if (datosTirada.getPuntos() == 0) {
            playMp3("/music/dardo_nulo.mp3");
        }
        if (datosTirada.isTriple()) {
            playMp3("/music/dardo_triple.mp3");
        } else {
            playMp3("/music/dardo_simple.mp3");
        }
        puntosParaRestar = datosTirada.getPuntos();

        int puntosAcumulados = players.get(jugadorActual).getPuntuacion() - puntosParaRestar;
        if (puntosAcumulados == 0) {
            if (gameInfo.isDoubleInDoubleOutType() && !datosTirada.isDoble()) {
                players.get(jugadorActual).getTextViewPuntuacion().setText(String.valueOf(puntuacionInicial));
                players.get(jugadorActual).setPuntuacion(puntuacionInicial);
            } else {
                players.get(jugadorActual).setPuntuacion(puntosAcumulados);
                players.get(jugadorActual).getTextViewPuntuacion().setText(String.valueOf(puntosAcumulados));
                if (winner == null) {
                    winner = players.get(jugadorActual);
                }
            }
            waitInfo.setWaiting(true);

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Retirar dardos");
                alert.setHeaderText("Retirar dardos");
                alert.setContentText("Retire los dardos y pulse continuar");

                alert.setOnHidden(dialogEvent -> {
                    nextPlayer();
                    waitInfo.setWaiting(false);
                });
                alert.show();
            });
        } else if (puntosAcumulados > 0) {
            players.get(jugadorActual).setPuntuacion(puntosAcumulados);
            players.get(jugadorActual).getTextViewPuntuacion().setText(String.valueOf(puntosAcumulados));

            tirada++;

            if (tirada > 3) {
                if ((jugadorActual == (players.size() - 1) &&
                        (winner != null) || round == gameInfo.getRounds())) {
                    nextPlayer();
                } else {
                    waitInfo.setWaiting(true);
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Retirar dardos");
                        alert.setHeaderText("Retirar dardos");
                        alert.setContentText("Retire los dardos y pulse continuar");

                        alert.setOnHidden(dialogEvent -> {
                            nextPlayer();
                            waitInfo.setWaiting(false);
                        });
                        alert.show();
                    });
                }
            }
        } else {
            waitInfo.setWaiting(true);

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Te has pasado");
                alert.setHeaderText("Te has pasado");
                alert.setContentText("Has excedido la puntuación.\nRetira los dardos y pulsa continuar");

                alert.setOnHidden(dialogEvent -> {
                    players.get(jugadorActual).getTextViewPuntuacion().setText(String.valueOf(puntuacionInicial));
                    players.get(jugadorActual).setPuntuacion(puntuacionInicial);
                    nextPlayer();
                    waitInfo.setWaiting(false);
                });
                alert.show();
            });
        }
    }

    private void nextPlayer() {
        setInactivePlayer(players.get(jugadorActual).getLinearLayout());
        jugadorActual++;
        jugadorActual %= players.size();
        boolean finPartida = false;
        if (jugadorActual == 0) {
            round++;
            finPartida = verificaFinPartida();
        }

        if (finPartida) {
            waitInfo.setWaiting(true);
            Platform.runLater(() -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                String sbFinPartida = "El ganador es " + winner.getName() + "\n\n" +
                        "Fin de la partida.\n¿Quiere repetir la partida?";
                alert.setTitle("Fin de partida");
                alert.setHeaderText("Fin de partida");
                alert.setContentText(sbFinPartida);

                alert.setOnHidden(dialogEvent -> {
                    if (timer != null) timer.cancel();
                    if (port != null) port.closePort();
                    if (alert.getResult().equals(ButtonType.OK)) {
                        int puntuacion = Integer.parseInt(gameInfo.getGameMode());
                        for (Gamer gamer : players) {
                            gamer.setPuntuacion(puntuacion);
                        }
                        initGame(port.getDescriptivePortName());
                    } else {
                        try {
                            Stage stage = (Stage) txtRoundNumber.getScene().getWindow();
                            Parent root = FXMLLoader.load(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
                            stage.getScene().setRoot(root);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Platform.exit();
                        }
                    }
                });
                alert.show();
            });

        } else {
            txtRoundNumber.setText(String.valueOf(round));
            clearTirada();
            setActivePlayer(players.get(jugadorActual).getLinearLayout());
            puntuacionInicial = players.get(jugadorActual).getPuntuacion();
            tirada = 1;
        }
    }

    private DatosTirada codigoAPuntos(String nuevosPuntos) {
        DatosTirada datosTirada = new DatosTirada();
        String[] lados = null;
        if (nuevosPuntos.equals("Cext")) {
            datosTirada = actualizaTirada(25, "1", "Diana");
        } else if (nuevosPuntos.equals("Cint")) {
            datosTirada = actualizaTirada(50, "2", "Diana");
        } else if (nuevosPuntos.isBlank() || nuevosPuntos.trim().equals("0")) {
            datosTirada = actualizaTirada(0, (String) null);
        } else {
            lados = nuevosPuntos.split("x");
            int puntuacionTirada = Integer.parseInt(lados[0]) * Integer.parseInt(lados[1]);
            datosTirada = actualizaTirada(puntuacionTirada, lados);
        }
        return datosTirada;
    }

    private DatosTirada actualizaTirada(int puntuacionTirada, String... datos) {
        DatosTirada datosTirada = new DatosTirada();

        int puntosSuma = puntuacionTirada;
        StringBuilder textoTirada = new StringBuilder(16);
        if (null == datos[0]) {
            textoTirada.append("Nulo").append(" ");
        } else if (Integer.parseInt(datos[0]) == 1) {
            textoTirada.append(datos[1]);
        } else if (Integer.parseInt(datos[0]) == 2) {
            datosTirada.setDoble(true);
            textoTirada.append("Doble").append(" ");
            textoTirada.append(datos[1]);
        } else if (Integer.parseInt(datos[0]) == 3) {
            datosTirada.setTriple(true);
            textoTirada.append("Triple").append(" ");
            textoTirada.append(datos[1]);
        }
        String textoPuntos = textoTirada.toString();
        if (gameInfo.isDoubleInDoubleOutType()) {
            if ((players.get(jugadorActual).getPuntuacion() == Integer.parseInt(gameInfo.getGameMode())) && !datosTirada.isDoble()) {
                puntosSuma = 0;
                textoPuntos = "Nulo";
            }
        }
        datosTirada.setPuntos(puntosSuma);
        totalTirada += puntosSuma;
        txtShotTotal.setText(getStringTotal(totalTirada));

        switch (tirada) {
            case 1:
                txtShot1.setText(textoPuntos);
                contShot1.setVisible(true);
                break;
            case 2:
                txtShot2.setText(textoPuntos);
                contShot2.setVisible(true);
                break;
            case 3:
                txtShot3.setText(textoPuntos);
                contShot3.setVisible(true);
                break;

        }
        return datosTirada;
    }

    private boolean verificaFinPartida() {
        boolean finPartida = false;
        if (round > gameInfo.getRounds()) {
            finPartida = true;
            if (winner == null) {
                ordenaJugadores();
                winner = players.get(0);
            }
        } else if (winner != null) {
            finPartida = true;
        }
        return finPartida;
    }

    private void ordenaJugadores() {
        Collections.sort(players, new Comparator<Gamer>() {
            @Override
            public int compare(Gamer o1, Gamer o2) {
                return Integer.compare(o1.getPuntuacion(), o2.getPuntuacion());
            }
        });
    }


    private void setInactivePlayer(GridPane box) {
        box.setBackground(new Background(new BackgroundFill(Paint.valueOf("#B1BCBEAA"), new CornerRadii(20), Insets.EMPTY)));
    }

    private void setActivePlayer(GridPane box) {
        box.setBackground(new Background(new BackgroundFill(Paint.valueOf("#00FF00AA"), new CornerRadii(20), Insets.EMPTY)));
    }

    private void playMp3(String filename) {
        try {
            URL resource = getClass().getResource(filename);
            if (null != resource) {
                Media media = new Media(resource.toURI().toString());
                MediaPlayer player = new MediaPlayer(media);
                player.play();
            }
        } catch (URISyntaxException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private String getStringTotal(int valor){
        return "Total: " + String.valueOf(valor);
    }
}
