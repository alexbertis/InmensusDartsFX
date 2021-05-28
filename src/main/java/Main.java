import fxcontrollers.MainScreenController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jssc.SerialPortList;

import java.util.Locale;
import java.util.ResourceBundle;

import static utils.Constants.WINDOW_HEIGHT;
import static utils.Constants.WINDOW_WIDTH;

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
        //primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
    }


    public static void main(String[] args) {
        System.out.println("Puertos:");
        for (String port : SerialPortList.getPortNames())
            System.out.println(port);
        System.out.println("-----------");
        launch(args);
    }
}
