package main.fxcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NamesScreenController extends BaseGuiController {

    @FXML
    private Button btnNamesBack, btnNamesNext;

    @FXML
    private TextField txtNames1, txtNames2, txtNames3, txtNames4, txtNames5, txtNames6, txtNames7, txtNames8;

    private final TextField[] textFields =
            new TextField[]{txtNames1, txtNames2, txtNames3, txtNames4, txtNames5, txtNames6, txtNames7, txtNames8};


    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if (event.getSource() == btnNamesBack) {
            stage = (Stage) btnNamesBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_opciones.fxml"), getStringsBundle());
            stage.setScene(new Scene(root));
            stage.show();
        } else if (event.getSource() == btnNamesNext) {
            // bluetooth
            stage = (Stage) btnNamesNext.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("pantalla_x01.fxml"), getStringsBundle());
            stage.setScene(new Scene(root));
            stage.show();
        } else {

        }
    }

}
