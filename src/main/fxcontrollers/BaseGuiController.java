package main.fxcontrollers;

import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.net.URL;
import java.util.ResourceBundle;

public class BaseGuiController implements Initializable {

    private ResourceBundle stringsBundle;


    protected ResourceBundle getStringsBundle() {
        return stringsBundle;
    }

    private void setStringsBundle(ResourceBundle stringsBundle) {
        this.stringsBundle = stringsBundle;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setStringsBundle(resourceBundle);
    }
}
