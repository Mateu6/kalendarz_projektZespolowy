package studia.projektzespolowy.kalendarz;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class CalendarEvents extends Dialog<String> {
    public CalendarEvents() {
        setTitle("Add Event");

        // Create the dialog content
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        // Add input fields to the VBox (e.g. event name, start time, end time)
        TextField eventNameField = new TextField();
        Label startTimeLabel = new Label("Start Time:");
        TextField startTimeField = new TextField();
        Label endTimeLabel = new Label("End Time:");
        TextField endTimeField = new TextField();
        vbox.getChildren().addAll(eventNameField, startTimeLabel, startTimeField, endTimeLabel, endTimeField);

        // Add buttons to the dialog
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Set the dialog content
        getDialogPane().setContent(vbox);

        // Handle the user's input when the "Add" button is clicked
        setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                String eventName = eventNameField.getText();
                String startTime = startTimeField.getText();
                String endTime = endTimeField.getText();

                // Do something with the event data (e.g. add it to a list, save it to a file, etc.)
                return eventName + ", " + startTime + " - " + endTime;
            }

            return null;
        });
    }
}
