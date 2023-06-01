package studia.projektzespolowy.kalendarz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.net.URL;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    private ContextMenu contextMenu;
    @FXML
    private TextField year;
    @FXML
    public MenuButton monthPicker;
    @FXML
    public FlowPane calendar;
    @FXML
    public GridPane calendarLabels;
    @FXML
    public GridPane calendarControls;
    private ObservableList<CalendarEvent> eventList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent ignoredEvent) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent ignoredEvent) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    private void drawMonthsList() {
        if (monthPicker.getItems().isEmpty()) {
            for (Month month : Month.values()) {
                MenuItem menuItem = new MenuItem(month.toString());
                monthPicker.getItems().add(menuItem);
                menuItem.setOnAction(event -> {
                    monthPicker.setText(month.toString());
                    dateFocus = dateFocus.withMonth(month.getValue());
                    calendar.getChildren().clear();
                    drawCalendar();
                });
            }
        }
    }

    private void drawCalendar() {
        year.setFocusTraversable(false);
        monthPicker.setFocusTraversable(false);

        year.setText(String.valueOf(dateFocus.getYear()));
        monthPicker.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        int monthMaxDate = dateFocus.getMonth().maxLength();

        // Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }

        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();

                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.GRAY);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculateDate = (j + 1) + (7 * i);

                if (calculateDate > dateOffset) {
                    int currentDate = calculateDate - dateOffset;

                    if (currentDate <= monthMaxDate) {
                        Text dateText = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        dateText.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(dateText);

                        // Check if there is an event on this date
                        boolean hasEvent = hasEventOnDate(dateFocus.withDayOfMonth(currentDate));
                        if (hasEvent) {
                            Circle eventIndicator = createEventIndicator();
                            stackPane.getChildren().add(eventIndicator);
                        }
                    } else {
                        rectangle.setDisable(true);
                    }

                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                        rectangle.setFill(Color.DARKGRAY);
                    }
                } else {
                    rectangle.setDisable(true);
                }

                if (!rectangle.isDisable()) {
                    final int clickedDate = calculateDate; // Store the current date for the event handler
                    rectangle.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            handleDateClick(dateFocus.withDayOfMonth(clickedDate), event.getScreenX(), event.getScreenY());
                        }
                    });
                }

                if (rectangle.isDisable()) {
                    rectangle.setFill(Color.LIGHTGRAY);
                }

                calendar.getChildren().add(stackPane);
            }
        }
    }

    private boolean hasEventOnDate(ZonedDateTime date) {
        return eventList.stream()
                .anyMatch(event -> event.getDate().equals(date.toLocalDate()));
    }

    private Circle createEventIndicator() {
        Circle eventIndicator = new Circle(8, Color.RED);
        eventIndicator.setTranslateY(-12);
        return eventIndicator;
    }

    private void handleDateClick(ZonedDateTime clickedDate, double x, double y) {
        LocalDate date = clickedDate.toLocalDate();
        LocalTime time = clickedDate.toLocalTime();

        // Create the popup
        Popup popup = new Popup();
        popup.setAutoHide(true);

        // Create the event form
        VBox eventForm = new VBox(10);
        eventForm.setStyle("-fx-background-color: white; -fx-padding: 10px;");
        eventForm.setPrefWidth(300);

        // Create form components
        TextField titleField = new TextField();
        TextArea descriptionArea = new TextArea();
        DatePicker datePicker = new DatePicker(date);
        TextField timeField = new TextField(time.toString());
        Button createButton = new Button("Create Event");

        // Add event handler for create button
        createButton.setOnAction(event -> {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            LocalDate selectedDate = datePicker.getValue();
            LocalTime selectedTime = LocalTime.parse(timeField.getText());

            CalendarEvent eventObject = new CalendarEvent(title, description, selectedDate, selectedTime);
            // Perform necessary operations with the event object (e.g., add to data structure)
            eventList.add(eventObject);
            popup.hide();
        });

        // Add form components to the event form
        eventForm.getChildren().addAll(
                new Label("Title:"),
                titleField,
                new Label("Description:"),
                descriptionArea,
                new Label("Date:"),
                datePicker,
                new Label("Time:"),
                timeField,
                createButton
        );

        // Set the content of the popup to the event form
        popup.getContent().add(eventForm);

        // Position the popup relative to the calendar
        Node source = calendar; // Set the source node for positioning
        Window window = source.getScene().getWindow();
        double posX = window.getX() + x - eventForm.getPrefWidth() / 2;
        double posY = window.getY() + y - eventForm.getHeight() / 2;
        popup.show(window, posX, posY);
    }


    private void showEventPopup(CalendarEvent event, double x, double y) {
        // Show the popup with event details and modification options
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Event Details");
        alert.setHeaderText("Event on " + event.getDate());
        alert.setContentText("Event Description: " + event.getDescription());

        ButtonType editButton = new ButtonType("Edit");
        ButtonType deleteButton = new ButtonType("Delete");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(editButton, deleteButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == editButton) {
                // Handle edit event
                showEditEventPopup(event);
            } else if (result.get() == deleteButton) {
                // Handle delete event
                deleteEvent(event);
            }
        }
    }

    private void showCreateEventPopup(ZonedDateTime date) {
        // Create the popup
        Popup popup = new Popup();
        popup.setAutoHide(true);

        // Create the event form
        VBox eventForm = new VBox();
        eventForm.setPadding(new Insets(10));
        eventForm.setSpacing(10);

        // Add form fields for event details (e.g., title, description, time)
        TextField titleField = new TextField();
        TextArea descriptionArea = new TextArea();
        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();

        // Add a "Create Event" button
        Button createButton = new Button("Create Event");
        createButton.setOnAction(event -> {
            // Retrieve the entered event details
            String title = titleField.getText();
            String description = descriptionArea.getText();
            LocalDate eventDate = datePicker.getValue();
            LocalTime eventTime = LocalTime.parse(timeField.getText());

            // Create a new CalendarEvent object with the entered details
            CalendarEvent newEvent = new CalendarEvent(title, description, eventDate, eventTime);

            // Add the new event to your data structure or perform any necessary operations

            // Close the popup
            popup.hide();
        });

        // Add form fields to the event form
        eventForm.getChildren().addAll(
                new Label("Title:"),
                titleField,
                new Label("Description:"),
                descriptionArea,
                new Label("Date:"),
                datePicker,
                new Label("Time:"),
                timeField,
                createButton
        );

        // Set the content of the popup to the event form
        popup.getContent().add(eventForm);

        // Position the popup relative to the calendar
        Node source = calendar; // Set the source node for positioning
        Window window = source.getScene().getWindow();
        double x = window.getX() + source.localToScene(0, 0).getX() + source.getBoundsInParent().getWidth() / 2;
        double y = window.getY() + source.localToScene(0, 0).getY() + source.getBoundsInParent().getHeight() / 2;
        popup.show(window, x, y);
    }

    private void showEditEventPopup(CalendarEvent eventToEdit) {
        // Create the popup
        Popup popup = new Popup();
        popup.setAutoHide(true);

        // Create the event form
        VBox eventForm = new VBox(10);
        eventForm.setStyle("-fx-background-color: white; -fx-padding: 10px;");
        eventForm.setPrefWidth(300);

        // Create form components
        TextField titleField = new TextField(eventToEdit.getTitle());
        TextArea descriptionArea = new TextArea(eventToEdit.getDescription());
        DatePicker datePicker = new DatePicker(eventToEdit.getDate());
        TextField timeField = new TextField(eventToEdit.getTime().toString());
        Button saveButton = new Button("Save Changes");

        // Add event handler for save button
        saveButton.setOnAction(event -> {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            LocalDate selectedDate = datePicker.getValue();
            LocalTime selectedTime = LocalTime.parse(timeField.getText());

            // Update the event with new values
            eventToEdit.setTitle(title);
            eventToEdit.setDescription(description);
            eventToEdit.setDate(selectedDate);
            eventToEdit.setTime(selectedTime);

            // Perform necessary operations with the updated event object

            popup.hide();
        });

        // Add form components to the event form
        eventForm.getChildren().addAll(
                new Label("Title:"),
                titleField,
                new Label("Description:"),
                descriptionArea,
                new Label("Date:"),
                datePicker,
                new Label("Time:"),
                timeField,
                saveButton
        );

        // Set the content of the popup to the event form
        popup.getContent().add(eventForm);

        // Position the popup relative to the calendar
        Node source = calendar; // Set the source node for positioning
        Window window = source.getScene().getWindow();
        double posX = window.getX() + source.getBoundsInParent().getMinX() + 50;
        double posY = window.getY() + source.getBoundsInParent().getMinY() + 50;
        popup.show(window, posX, posY);
    }



    private void deleteEvent(CalendarEvent event) {
        // Delete the event from the eventList and redraw the calendar
        eventList.remove(event);
        drawCalendar();
    }
}
