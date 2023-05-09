package studia.projektzespolowy.kalendarz;

import studia.projektzespolowy.kalendarz.CalendarEvents;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import java.time.LocalDate;
import java.time.ZonedDateTime;



    public class CalendarEvents extends Popup {

        private DatePicker datePicker;
        private  TextField nameField;
        private  Button addButton;
        private  Button cancelButton;

        public CalendarEvents() {
            super();

            // Create UI components
            Label titleLabel = new Label("Add Event");
            Label nameLabel;
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            nameLabel = new Label("Name:");
            nameField = new TextField();
            datePicker = new DatePicker(LocalDate.now());
            addButton = new Button("Add");
            cancelButton = new Button("Cancel");

            // Set event handlers
            addButton.setOnAction(event -> addEvent());
            cancelButton.setOnAction(event -> hide());

            // Create layout
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(5);
            gridPane.setPadding(new Insets(10));
            gridPane.addRow(0, nameLabel, nameField);
            gridPane.addRow(1, new Label("Date:"), datePicker);

            HBox buttonBox = new HBox(10, addButton, cancelButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);

            VBox vBox = new VBox(10, titleLabel, gridPane, buttonBox);
            vBox.setPadding(new Insets(10));
            vBox.setStyle("-fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 1px; -fx-border-radius: 5px;");
            vBox.setMaxWidth(300);

            // Set content
            getContent().add(vBox);
        }
        private String name;
        private LocalDate date;

        public CalendarEvents(String name, LocalDate date) {
            this.name = name;
            this.date = date;
        }
        private void addEvent() {
            String name = nameField.getText();
            LocalDate date = datePicker.getValue();
            CalendarEvents event = new CalendarEvents(name, date);
            // Do something with the new event
            hide();
        }
}
