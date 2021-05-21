package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Locale.setDefault(new Locale("es"));

        ResourceBundle resourceBundle = ResourceBundle.getBundle("main/i18n/strings_gui");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxcontrollers/pantalla_principal.fxml"), resourceBundle);
        Parent root = loader.load();
        // Can access controller as a normal class
        // MainScreenController controller = loader.getController();

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
