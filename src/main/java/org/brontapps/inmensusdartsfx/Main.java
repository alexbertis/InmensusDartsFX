package org.brontapps.inmensusdartsfx;

import org.brontapps.inmensusdartsfx.fxcontrollers.MainScreenController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(new Locale("es"));

        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/strings_gui");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxcontrollers/pantalla_principal.fxml"), resourceBundle);
        Parent root = loader.load();
        // Can access controller as a normal class
        MainScreenController controller = loader.getController();
        controller.loadComboOptions();

        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.setTitle("InmensusDarts");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/target.png")));
        primaryStage.setFullScreen(true);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
