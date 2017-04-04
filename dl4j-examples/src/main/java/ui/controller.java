package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Created by akshayrajgollahalli on 15/08/15.
 */

public class controller{

    @FXML // makes private field visible to FXML loader and autowire the button to FXML
    private Button myButton;

    @FXML
    private Label myText;

    @FXML
    private void initialize(){
        // if button is clicked run this event
        myButton.setOnAction((event) -> {
            myText.setText("Hello World");
        });
    }
}
