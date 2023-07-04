package studia.projektzespolowy.kalendarz;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController  implements Initializable {
    @FXML
    private Button button_signup;
    @FXML
    private Button button_log_in;
    @FXML
    private RadioButton rb_woman;
    @FXML
    private RadioButton rb_man;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;

    public SignUpController() {

    }


    /**Method called when initializing the controller.
     * Creates a ToggleGroup for the radio buttons.
     * Sets the ToggleGroup for rb_woman and rb_man.
     * Sets rb_woman as the initially selected radio button.
     * Sets an event handler for the button_signup button that handles sign up logic.
     * Sets an event handler for the button_log_in button that handles navigation to the login screen.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        rb_woman.setToggleGroup(toggleGroup);
        rb_man.setToggleGroup(toggleGroup);

        rb_woman.setSelected(true);

        button_signup.setOnAction(new EventHandler<ActionEvent>() {
            /**Event handler for the button_signup button.
             * Checks if the username and password fields are not empty.
             * Calls the signUpUser method from the DBUtils class to handle sign up logic.
             * Displays an error message if any required information is missing.*/

            @Override
            public void handle(ActionEvent event) {
                String toggleName = ((RadioButton) toggleGroup.getSelectedToggle()).getText();

                if (!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()){
                    DBUtils.signUpUser(event, tf_username.getText(),tf_password.getText(), toggleName);
                }else{
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });
        /**Event handler for the button_log_in button.
         *Calls the changeScene method from the DBUtils class to navigate to the login screen. */
        button_log_in.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"Login.fxml","Log  in", null);
            }
        });
    }
}