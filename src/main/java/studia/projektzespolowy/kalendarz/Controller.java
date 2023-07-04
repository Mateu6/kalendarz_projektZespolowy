package studia.projektzespolowy.kalendarz;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button button_login;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private Button button_sign_up;

    /**Method called when initializing the controller.
     *Sets event handlers for the login and sign-up buttons. */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_login.setOnAction(new EventHandler<ActionEvent>() {

           //Event handler for the login button.
            //Calls the logInUser method from DBUtils and passes the entered username and password.
            @Override
            public void handle(ActionEvent event) {
                DBUtils.logInUser(event,tf_username.getText(),tf_password.getText());
            }
        });

        /**Event handler for the sign-up button.
         * Calls the changeScene method from DBUtils to switch to the "sign-up.fxml" scene.*/
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "sign-up.fxml", "Sign Up!", null);
            }
        });
    }
}

