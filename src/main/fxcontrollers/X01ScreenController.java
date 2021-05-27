package main.fxcontrollers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.beans.DatosTirada;
import main.beans.GameInfo;
import main.beans.Gamer;
import main.beans.WaitInfo;
import purejavacomm.CommPortIdentifier;
import purejavacomm.SerialPort;
import purejavacomm.SerialPortEvent;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class X01ScreenController extends BaseGuiController {

    @FXML
    private Text txtShot1, txtShot2, txtShot3, txtShotTotal, txtRoundNumber;

    @FXML
    private HBox contShot1, contShot2, contShot3;

    @FXML
    private GridPane padreJugadores;


    GameInfo gameInfo = null;
    int jugadorActual = 0;
    int round = 1;
    int tirada = 1;
    int totalTirada = 0;
    int puntuacionInicial = 0;

    Gamer winner = null;
    private final WaitInfo waitInfo = new WaitInfo();


    private void attachSerialListener(String deviceName) throws Exception {
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(deviceName);

        // Opening the port
        SerialPort port = (SerialPort) portId.open("InmensusDarts", 1000);

        //OutputStream outStream = port.getOutputStream();
        InputStream inStream = port.getInputStream();
        Scanner scanner = new Scanner(inStream);

        port.addEventListener(serialPortEvent -> {
            if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE && serialPortEvent.getNewValue()) {
                String sector = scanner.nextLine();
                // TODO: hacer cosas con la linea y en algun momento romper el bucle
                if (!waitInfo.isWaiting()) {
                    tirada(sector);
                }
            }
        });
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

    private void clearTirada() {
        totalTirada = 0;
        txtShotTotal.setText(String.valueOf(totalTirada));

        contShot1.setVisible(false);
        contShot2.setVisible(false);
        contShot3.setVisible(false);
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


    private void tirada(String nuevosPuntos){
        int puntosParaRestar = 0;
        DatosTirada datosTirada = codigoAPuntos(nuevosPuntos);
        if (datosTirada.getPuntos() == 0){
            //dardoNulo.start();
        }
        if (datosTirada.isTriple()){
            //dardoTriple.start();
        }else{
            //dardoSimple.start();
        }
        puntosParaRestar = datosTirada.getPuntos();

        int puntosAcumulados = players.get(jugadorActual).getPuntuacion() - puntosParaRestar;
        if (puntosAcumulados == 0){
            if (gameInfo.isDoubleInDoubleOutType() && !datosTirada.isDoble()){
                players.get(jugadorActual).getTextViewPuntuacion().setText(String.valueOf(puntuacionInicial));
                players.get(jugadorActual).setPuntuacion(puntuacionInicial);
            }else {
                players.get(jugadorActual).setPuntuacion(puntosAcumulados);
                players.get(jugadorActual).getTextViewPuntuacion().setText(String.valueOf(puntosAcumulados));
                if (winner == null) {
                    winner = players.get(jugadorActual);
                }
            }
            waitInfo.setWaiting(true);
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.retirar_dardos).setTitle(R.string.retirar_dardos_title);
            builder.setPositiveButton(R.string.btn_continuar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    nextPlayer();
                    waitInfo.setWaiting(false);
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();
            */
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Retirar dardos");
            alert.setContentText("Retire los dardos y pulse continuar");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && ButtonType.OK.equals(result.get())) {
                nextPlayer();
                waitInfo.setWaiting(false);
            }

        } else  if (puntosAcumulados > 0) {
            players.get(jugadorActual).setPuntuacion(puntosAcumulados);
            players.get(jugadorActual).getTextViewPuntuacion().setText(String.valueOf(puntosAcumulados));

            tirada++;

            if (tirada > 3) {
                if ((jugadorActual == (players.size() - 1) &&
                        (winner != null) || round == gameInfo.getRounds())){
                    nextPlayer();
                }else{
                    waitInfo.setWaiting(true);
                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.retirar_dardos).setTitle(R.string.retirar_dardos_title);
                    builder.setPositiveButton(R.string.btn_continuar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            nextPlayer();
                            waitInfo.setWaiting(false);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    */

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Retirar dardos");
                    alert.setContentText("Retire los dardos y pulse continuar");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && ButtonType.OK.equals(result.get())) {
                        nextPlayer();
                        waitInfo.setWaiting(false);
                    }
                }
            }
        } else {
            waitInfo.setWaiting(true);
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.excedido).replace("\\n","\n")).setTitle(R.string.excedido_title);
            builder.setPositiveButton(R.string.btn_continuar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    gamers.get(jugadorActual).getTextViewPuntuacion().setText(String.valueOf(puntuacionInicial));
                    gamers.get(jugadorActual).setPuntuacion(puntuacionInicial);
                    nextPlayer();
                    waitInfo.setWaiting(false);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();*/

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Te has pasado");
            alert.setContentText("Has excedido la puntuación.\\\\nRetira los dardos y pulsa continuar");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && ButtonType.OK.equals(result.get())) {
                players.get(jugadorActual).getTextViewPuntuacion().setText(String.valueOf(puntuacionInicial));
                players.get(jugadorActual).setPuntuacion(puntuacionInicial);
                nextPlayer();
                waitInfo.setWaiting(false);
            }
        }

    }

    private void nextPlayer(){
        setInactivePlayer(players.get(jugadorActual).getLinearLayout());
        jugadorActual++;
        jugadorActual %= players.size();
        boolean finPartida = false;
        if (jugadorActual == 0) {
            round++;
            finPartida = verificaFinPartida();
        }

        if (finPartida){
            waitInfo.setWaiting(true);
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String sbFinPartida = "El ganador es " + winner.getName() + "\n" +
                    getString(R.string.fin_partida).replace("\\n", "\n");
            builder.setMessage(sbFinPartida).setTitle(R.string.fin_partida_title);
            builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //initGame();
                    Intent intent = getIntent();
                    int puntuacion = Integer.parseInt(gameInfo.getGameMode());
                    for (Gamer gamer : gamers){
                        gamer.setPuntuacion(puntuacion);
                    }
//                        intent = getIntent();
//                        finish();
//                        startActivity(intent);
                    recreate();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Hay que volver a la pantalla de opciones
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();*/

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
        if (nuevosPuntos.equals("Cext")){
            datosTirada = actualizaTirada(25, "1","Diana");
        } else  if (nuevosPuntos.equals("Cint")) {
            datosTirada = actualizaTirada(50, "2","Diana");
        }else if (nuevosPuntos.trim().equals("")) {
            datosTirada = actualizaTirada(0, (String) null);
        }else {
            lados = nuevosPuntos.split("x");
            int puntuacionTirada = Integer.parseInt(lados[0]) * Integer.parseInt(lados[1]);
            datosTirada = actualizaTirada(puntuacionTirada, lados);
        }
        return datosTirada;
    }

    private DatosTirada actualizaTirada(int puntuacionTirada, String... datos ){
        DatosTirada datosTirada = new DatosTirada();

        int puntosSuma = puntuacionTirada;
        StringBuilder textoTirada = new StringBuilder(16);
        if (null == datos[0]){
            textoTirada.append("Nulo").append (" ");
        }else if (Integer.parseInt(datos[0]) == 1){
            textoTirada.append(datos[1]);
        }else if (Integer.parseInt(datos[0]) == 2){
            datosTirada.setDoble(true);
            textoTirada.append("Doble").append (" ");
            textoTirada.append(datos[1]);
        }else if (Integer.parseInt(datos[0]) == 3){
            datosTirada.setTriple(true);
            textoTirada.append("Triple").append (" ");
            textoTirada.append(datos[1]);
        }
        String textoPuntos = textoTirada.toString();
        if (gameInfo.isDoubleInDoubleOutType()){
            if ((players.get(jugadorActual).getPuntuacion() == Integer.parseInt(gameInfo.getGameMode())) && !datosTirada.isDoble()) {
                puntosSuma = 0;
                textoPuntos = "Nulo";
            }
        }
        datosTirada.setPuntos(puntosSuma);
        totalTirada += puntosSuma;
        txtShotTotal.setText(String.valueOf(totalTirada));

        switch (tirada){
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
        if (round > gameInfo.getRounds()){
            finPartida = true;
            if (winner == null){
                ordenaJugadores();
                winner = players.get(0);
            }
        }else if (winner != null){
            finPartida = true;
        }
        return finPartida;
    }

    private void ordenaJugadores(){
        Collections.sort(players, new Comparator<Gamer>() {
            @Override
            public int compare(Gamer o1, Gamer o2) {
                return Integer.compare(o1.getPuntuacion(), o2.getPuntuacion());
            }
        });
    }



    private void setInactivePlayer(VBox box){
        box.setBackground(new Background(new BackgroundFill(Paint.valueOf("#AAB1BCBE"), new CornerRadii(20), Insets.EMPTY)));
    }

    private void setActivePlayer(VBox box){
        box.setBackground(new Background(new BackgroundFill(Paint.valueOf("#AA00FF00"), new CornerRadii(20), Insets.EMPTY)));
    }

}
