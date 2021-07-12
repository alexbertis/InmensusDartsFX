package org.brontapps.inmensusdartsfx.fxcontrollers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.brontapps.inmensusdartsfx.beans.DatosTirada;
import org.brontapps.inmensusdartsfx.beans.Gamer;
import org.brontapps.inmensusdartsfx.beans.WaitInfo;

import com.fazecast.jSerialComm.SerialPort;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class X01ScreenController extends BaseGuiController {

    private final WaitInfo waitInfo = new WaitInfo();
    int jugadorActual = 0;
    int round = 1;
    int tirada = 1;
    int totalTirada = 0;
    int puntuacionInicial = 0;
    Gamer winner = null;
    
    private MediaPlayer sound_dardo_nulo = null;
    private MediaPlayer sound_dardo_simple = null;
    private MediaPlayer sound_dardo_triple = null;

    private SerialPort port;
    private Timer timer;
    
    private final String REPETIR_PARTIDA = "Repetir partida";
    private final String EMPEZAR_NUEVA = "Empezar partida";

    @FXML
    private Text txtShot1, txtShot2, txtShot3, txtShotTotal, txtRoundNumber;
    @FXML
    private HBox contShot1, contShot2, contShot3;
    @FXML
    private GridPane padreJugadores;

    private void attachSerialListener(String deviceName) throws Exception {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort puerto : ports){
            if (puerto.getPortDescription().equals(deviceName)){
            	System.out.println("Encontrado puerto");
                port= puerto;
                break;
            }
        }

        // Opening the port
        if (port != null) {
            port.openPort();
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
            InputStream portStream = port.getInputStream();
            Scanner scanner = new Scanner(portStream);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String sector = scanner.nextLine();
                    System.out.println("Recibido desde diana: " + sector);
                    // TODO: hacer cosas con la linea y en algun momento romper el bucle
                    if (!waitInfo.isWaiting()) {
                        tirada(sector);
                    }
                }
            }, 0, 200);
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
    
        Platform.runLater(() -> {
            try {
    	        sound_dardo_nulo = new MediaPlayer(new Media(getClass().getResource("/org/brontapps/inmensusdartsfx/music/dardo_nulo.wav").toURI().toString()));
    	        sound_dardo_simple = new MediaPlayer(new Media(getClass().getResource("/org/brontapps/inmensusdartsfx/music/dardo_simple.wav").toURI().toString()));
    	        sound_dardo_triple = new MediaPlayer(new Media(getClass().getResource("/org/brontapps/inmensusdartsfx/music/dardo_triple.wav").toURI().toString()));

            }catch(Exception e) {
            	sound_dardo_nulo = null;
            	sound_dardo_simple = null;
            	sound_dardo_triple = null;
            }
        	
        });

        txtRoundNumber.setText(String.valueOf(round));
        clearTirada();

        waitInfo.setWaiting(false);

    }

    public void restartGame() {

        for (Gamer gamer : players) {
            setInactivePlayer(gamer.getLinearLayout());
            gamer.setPuntuacion(Integer.parseInt(gameInfo.getGameMode()));
            gamer.getTextViewPuntuacion().setText(gameInfo.getGameMode());
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
        GridPane.setValignment(tvGamerName, VPos.TOP);
        GridPane.setValignment(tvGamerPoints, VPos.TOP);
        GridPane.setHalignment(tvGamerName, HPos.CENTER);
        GridPane.setHalignment(tvGamerPoints, HPos.CENTER);
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
        if (datosTirada != null)
        {
	        if (datosTirada.getPuntos() == 0) {
	            playMp3(sound_dardo_nulo);
	        }
	        if (datosTirada.isTriple()) {
	            playMp3(sound_dardo_triple);
	        } else {
	            playMp3(sound_dardo_simple);
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
	                CustomDialog dialog = new CustomDialog("Retirar dardos", "Retire los dardos\ny pulse continuar", "Continuar", null);
	                dialog.setOnHidden(dialogEvent -> {
	                	nextPlayer();
	                	waitInfo.setWaiting(false);
	                });
	//                dialog.openDialog();
	                dialog.showAndWait();
	                System.out.println("ButtonPressed " + dialog.getButtonPressed());
	
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
	                        CustomDialog dialog = new CustomDialog("Retirar dardos", "Retire los dardos\ny pulse continuar", "Continuar", null);
	                        dialog.setOnHidden(dialogEvent -> {
	                        	nextPlayer();
	                        	waitInfo.setWaiting(false);
	                        });
	//                      dialog.openDialog();
	                        dialog.showAndWait();
	                        System.out.println("ButtonPressed " + dialog.getButtonPressed());
	
	                    });
	                }
	            }
	        } else {
	            waitInfo.setWaiting(true);
	
	            Platform.runLater(() -> {
	                CustomDialog dialog = new CustomDialog("Te has pasado", "Has excedido la puntuación.\nRetira los dardos y pulsa continuar", "Continuar", null);
	                dialog.setOnHidden(dialogEvent -> {
	                    players.get(jugadorActual).getTextViewPuntuacion().setText(String.valueOf(puntuacionInicial));
	                    players.get(jugadorActual).setPuntuacion(puntuacionInicial);
	                    nextPlayer();
	                    waitInfo.setWaiting(false);
	                });
	                dialog.showAndWait();
	
	            });
	        }
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

                String sbFinPartida = "El ganador es " + winner.getName() + "\n\n" +
                        "Fin de la partida.\n¿Quiere repetir la partida?";
                CustomDialog dialog = new CustomDialog("Fin de partida", sbFinPartida.toString(), REPETIR_PARTIDA, EMPEZAR_NUEVA);
                dialog.showAndWait();
                String response = dialog.getButtonPressed();
                if (null != response && response.equals(EMPEZAR_NUEVA)) {
                    if (timer != null) timer.cancel();
                    if (port != null) port.closePort();
                    try {
                    	port.removeDataListener();
                        Stage stage = (Stage) txtRoundNumber.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
                        Parent root = loader.load();
                        stage.getScene().setRoot(root);
                        loader.<OptionsScreenController>getController().initOptions(stage.getScene());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Platform.exit();
                    }
                }else {
                	restartGame();
                }
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
        	try {
        		lados = nuevosPuntos.split("x");
        		int puntuacionTirada = Integer.parseInt(lados[0]) * Integer.parseInt(lados[1]);
        		datosTirada = actualizaTirada(puntuacionTirada, lados);
        	}catch(Exception e) {
        		datosTirada = null;
        	}
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

    private void playMp3(MediaPlayer player) {
    	if (player != null) {
	    	Runnable runSound = new Runnable() {
					
				@Override
				public void run() {
		            if (null != player) {
		            	player.setVolume(1.0);
		            	player.seek(Duration.ZERO);
		                player.play();
		            }
				}
			};
			runSound.run();
    	}
    }
    

    private String getStringTotal(int valor){
        return "Total: " + String.valueOf(valor);
    }
    
    
    private static final Interpolator EXP_IN = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 1.0) ? 1.0 : 1 - Math.pow(2.0, -10 * t);
        }
    };

    private static final Interpolator EXP_OUT = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 0.0) ? 0.0 : Math.pow(2.0, 10 * (t - 1));
        }
    };

    private static class CustomDialog extends Stage {
    	private String buttonPressed = null;

        private ScaleTransition scale1 = new ScaleTransition();
        private ScaleTransition scale2 = new ScaleTransition();

        private SequentialTransition anim = new SequentialTransition(scale1, scale2);

        CustomDialog(String header, String content, String button1, String button2) {
            Pane root = new Pane();

            scale1.setFromX(0.01);
            scale1.setFromY(0.01);
            scale1.setToY(1.0);
            scale1.setDuration(Duration.seconds(0.33));
            scale1.setInterpolator(EXP_IN);
            scale1.setNode(root);

            scale2.setFromX(0.01);
            scale2.setToX(1.0);
            scale2.setDuration(Duration.seconds(0.33));
            scale2.setInterpolator(EXP_OUT);
            scale2.setNode(root);

            initStyle(StageStyle.TRANSPARENT);
            initModality(Modality.APPLICATION_MODAL);


            Text headerText = new Text(header);
            headerText.setFont(Font.font("Copperplate Gothic Light", 60));
            headerText.setTextAlignment(TextAlignment.CENTER);


            Text contentText = new Text(content);
            contentText.setFont(Font.font("Candara",50));
            contentText.setTextAlignment(TextAlignment.CENTER);

            VBox box = new VBox(10,
                    headerText,
                    new Separator(Orientation.HORIZONTAL),
                    contentText
            );
            box.setPadding(new Insets(15));
            box.setAlignment(Pos.CENTER);

            double headerWidht = headerText.getLayoutBounds().getWidth();
            double contentWidth = contentText.getLayoutBounds().getWidth();
            double maxWidth = Math.max(headerWidht, contentWidth);
            Rectangle bg = new Rectangle(maxWidth + 50, 400);
            bg.setStroke(Color.BLACK);
            bg.setStrokeWidth(1.5);

            Stop[] stops = new Stop[] { new Stop(0, Color.GREY), new Stop(1, Color.RED)};
            LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
            bg.setFill(lg1);
            
            Button btn = new Button(button1);
            btn.setFont(Font.font("Copperplate Gothic Light", 20));
            btn.setTranslateX(50);
            btn.setTranslateY(bg.getHeight() - 50);
            btn.setPrefWidth(bg.getWidth() - 100);
            btn.setPrefHeight(50);
            btn.setOnAction(e -> closeDialog(button1));
            
            Button btn2 = null;
            if (button2 != null) {
            	btn2 = new Button(button2);
                btn2.setFont(Font.font("Copperplate Gothic Light", 20));
                btn.setTranslateX((bg.getWidth() / 2) + 50);
                btn2.setPrefHeight(50);
                btn.setPrefWidth((bg.getWidth() / 2) - 50);
                btn2.setPrefWidth((bg.getWidth() / 2) - 50);
                btn2.setTranslateY(bg.getHeight() - 50);
                btn2.setOnAction(e -> closeDialog(button2));
            }

            if (btn2 != null) {
            	root.getChildren().addAll(bg,box,btn, btn2);
            }else {
            	root.getChildren().addAll(bg,box,btn);
            }

            setScene(new Scene(root, null));
        }

        void openDialog() {
            show();

            anim.play();
        }

        void closeDialog(String buttonPressed) {
        	this.buttonPressed = buttonPressed;
            anim.setOnFinished(e -> close());
            anim.setAutoReverse(true);
            anim.setCycleCount(2);
            anim.playFrom(Duration.seconds(0.66));
        }
        
        public String getButtonPressed() {
        	return buttonPressed;
        }
    }

}
