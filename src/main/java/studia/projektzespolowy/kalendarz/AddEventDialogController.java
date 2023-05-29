package studia.projektzespolowy.kalendarz;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class AddEventDialogController {
    @FXML
    public StackPane eventList;
    @FXML
    public Button addEventButton;
    @FXML
    public Button cancelButton;
    @FXML
    public TextField eventNameTextField;
    @FXML
    public Button deleteEventButton;
}