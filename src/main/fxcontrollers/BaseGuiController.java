package main.fxcontrollers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import main.beans.GameInfo;
import main.beans.Gamer;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BaseGuiController implements Initializable {

    private ResourceBundle stringsBundle;
    protected static GameInfo gameInfo;
    protected static List<Gamer> players;


    protected ResourceBundle getStringsBundle() {
        return stringsBundle;
    }

    private void setStringsBundle(ResourceBundle stringsBundle) {
        this.stringsBundle = stringsBundle;
    }

    protected String getSelectedFromToggleGroup(ToggleGroup toggleGroup, String defaultValue){
        if (toggleGroup == null) return defaultValue;
        ToggleButton button = (ToggleButton)toggleGroup.getSelectedToggle();
        return button != null ? button.getText() : defaultValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setStringsBundle(resourceBundle);
    }
}
