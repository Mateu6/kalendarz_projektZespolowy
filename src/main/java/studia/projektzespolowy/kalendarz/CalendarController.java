package studia.projektzespolowy.kalendarz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import java.net.URL;
import java.time.*;
import java.util.*;

public class CalendarController implements Initializable {

    private ZonedDateTime dateFocus;
    private ZonedDateTime today;

    private Map<LocalDate, List<EventInfo>> eventMap = new HashMap<>();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(dateFocus == null){
            dateFocus = ZonedDateTime.now();
        }
        today = ZonedDateTime.now();
        eventMap = new HashMap<>();
        drawCalendar(this);
    }

    @FXML
    void backOneMonth(ActionEvent ignoredEvent) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar(this);
    }

    @FXML
    void forwardOneMonth(ActionEvent ignoredEvent) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar(this);
    }

    @FXML
    private void drawMonthsList() {
        if (monthPicker.getItems().isEmpty()) {
            for (Month month : Month.values()) {
                MenuItem menuItem = new MenuItem(month.toString());
                monthPicker.getItems().add(menuItem);
                menuItem.setOnAction(event -> {
                    // Update the MenuButton text to show the selected month
                    monthPicker.setText(month.toString());
                    dateFocus = dateFocus.withMonth(month.getValue());
                    calendar.getChildren().clear();
                    drawCalendar(this);
                });
            }
        }
    }

    public void addEvent(EventInfo event) {
        LocalDate eventDate = event.getDate();
        eventMap.computeIfAbsent(eventDate, key -> new ArrayList<>()).add(event);
    }

    private void showEventPopup(List<EventInfo> events, Node anchorNode) {
        // Create an AnchorPane to hold the pop-up content
        AnchorPane popupContent = new AnchorPane();
        popupContent.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        // Add event details to the pop-up content
        VBox eventDetails = new VBox();
        for (EventInfo event : events) {
            Text eventName = new Text(event.getName());
            Text eventTime = new Text(event.getDate().toString());
            eventDetails.getChildren().addAll(eventName, eventTime);
        }
        popupContent.getChildren().add(eventDetails);

        // Create a Popup and set its content
        Popup popup = new Popup();
        popup.setAutoHide(true);
        popup.getContent().add(popupContent);

        // Show the pop-up below the anchorNode
        Bounds bounds = anchorNode.getBoundsInLocal();
        Bounds screenBounds = anchorNode.localToScreen(bounds);
        popup.show(anchorNode, screenBounds.getMinX(), screenBounds.getMaxY());
    }

    void drawCalendar(CalendarController calendarController) {
        year.setText(String.valueOf(dateFocus.getYear()));
        monthPicker.setText(String.valueOf(dateFocus.getMonth()));

        year.setFocusTraversable(false);
        monthPicker.setFocusTraversable(false);

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

        int dateOffSet = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone())
                .getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                Rectangle eventRectangle = new Rectangle();
                Circle dot = new Circle();
                VBox eventContainer = new VBox();

                // Day in the month tile
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.GRAY);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle); // Show the calendar tiles

                // Day in the month with event attached to it tile
                dot.setFill(Color.TRANSPARENT);
                dot.setStroke(Color.YELLOW);
                dot.setStrokeWidth(strokeWidth);
                dot.setRadius(10);
                eventContainer.setAlignment(Pos.TOP_RIGHT);
                eventContainer.getChildren().addAll(eventRectangle, dot); // Is ready to replace a given tile

                int calculateDate = (j + 1) + (7 * i);

                if (calculateDate > dateOffSet) {
                    int currentDate = calculateDate - dateOffSet;

                    rectangle.setOnMouseClicked(event -> {
                        LocalDate clickedDate = LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), currentDate);

                        List<EventInfo> events = eventMap.get(clickedDate);
                        if (events != null && !events.isEmpty()) {
                            // Show pop-up with event details
                            showEventPopup(events, rectangle);
                        }
                    });

                    if (currentDate <= monthMaxDate) {
                        LocalDate currentDateObj = LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), currentDate);
                        List<EventInfo> events = eventMap.get(currentDateObj);

                        if (events != null) {
                            for (EventInfo eventInfo : events) {
                                Text eventNameText = new Text(eventInfo.getName());
                                eventContainer.getChildren().add(eventNameText);
                                calendar.getChildren().add(eventRectangle);
                            }
                        }

                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);
                    } else {
                        rectangle.setDisable(true);
                    }

                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth()
                            && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                        rectangle.setFill(Color.DARKGRAY);
                    }
                } else {
                    rectangle.setDisable(true);
                }

                if (!rectangle.isDisable()) {
                    rectangle.setOnMouseClicked(event -> {
                        // Determine the x and y coordinates of the click relative to the screen
                        double x = event.getScreenX();
                        double y = event.getScreenY();

                        // Show the popup at the specified location
                        CalendarEvents eventPopup = new CalendarEvents(calendarController);
                        eventPopup.show(rectangle.getScene().getWindow(), x, y);

                    });
                }



                if (rectangle.isDisable()) {
                    rectangle.setFill(Color.LIGHTGRAY);
                }

                calendar.getChildren().add(stackPane);
            }
        }
    }
}
