package org.brontapps.inmensusdartsfx.beans;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Gamer {

    private String name;
    private int puntuacion;
    private Text textViewPuntuacion;
    private GridPane gamerPane;

    public Gamer(String name, int puntuacion) {
        this.name = name;
        this.puntuacion = puntuacion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Text getTextViewPuntuacion() {
        return textViewPuntuacion;
    }

    public void setTextViewPuntuacion(Text textViewPuntuacion) {
        textViewPuntuacion.setText(String.valueOf(puntuacion));
        this.textViewPuntuacion = textViewPuntuacion;
    }

    public GridPane getLinearLayout() {
        return gamerPane;
    }

    public void setLinearLayout(GridPane gamerPane) {
        this.gamerPane = gamerPane;
    }
}
