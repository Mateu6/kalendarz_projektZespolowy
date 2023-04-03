package studia.projektzespolowy.kalendarz;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AccountManager {
    @FXML
    public AnchorPane accountManagerPanel;

    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;

    @FXML
    private Button changeUserNameButton;
    @FXML
    private Button changePasswordButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Button saveButton;

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

    public void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void onSaveButtonClick(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("You are changing your account credentials");
        alert.setContentText("Do you want to save changes?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            //send new data to database and load them into account manager
        } else {
            //do nothing
        }
    }


}
