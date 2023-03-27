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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.logInUser(event,tf_username.getText(),tf_password.getText());
            }
        });

        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "sign-up.fxml", "Sign Up!", null);
            }
        });
    }
}

