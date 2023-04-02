package studia.projektzespolowy.kalendarz;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class AccountManager {

    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;

    @FXML
    private Button changeUserNameButton;
    @FXML
    private Button changePasswordButton;

    public void onChangeUserNameClick(){
        TextInputDialog dialog = new TextInputDialog(userNameLabel.getText());
        dialog.setTitle("User Name Change");
        dialog.setHeaderText("Do you want to change User Name?");
        dialog.setContentText("User Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(userName -> userNameLabel.setText(userName));
    }

    public void onChangePasswordClick(){
        TextInputDialog dialog = new TextInputDialog(passwordLabel.getText());
        dialog.setTitle("Password Change");
        dialog.setHeaderText("Do you want to change Password?");
        dialog.setContentText("Password:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(password -> passwordLabel.setText(password));
    }


}
