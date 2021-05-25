package main.fxcontrollers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import main.beans.GameInfo;

import java.net.URL;
import java.util.ResourceBundle;

public class BaseGuiController implements Initializable {

    private ResourceBundle stringsBundle;
    protected static GameInfo gameInfo;


    protected ResourceBundle getStringsBundle() {
        return stringsBundle;
    }

    private void setStringsBundle(ResourceBundle stringsBundle) {
        this.stringsBundle = stringsBundle;
    }

    protected String getSelectedFromToggleGroup(ToggleGroup toggleGroup){
        return ((Button)toggleGroup.getSelectedToggle()).getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setStringsBundle(resourceBundle);
    }
}
