package studia.projektzespolowy.kalendarz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Window;


import java.net.URL;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
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

        if(monthPicker.getItems().isEmpty()) {

            for (Month month : Month.values()) {
                MenuItem menuItem = new MenuItem(month.toString());
                monthPicker.getItems().add(menuItem);
                menuItem.setOnAction(event -> {
                    // Update the MenuButton text to show the selected month
                    monthPicker.setText(month.toString());
                    dateFocus = dateFocus.withMonth(month.getValue());
                    calendar.getChildren().clear();
                    drawCalendar();
                });
            }
        }

    }
    CalendarEvents eventPopup = new CalendarEvents();
    private List<EventInfo> events;

    public CalendarController() {
        events = new ArrayList<>();
    }

    public void addEvent(EventInfo eventInfo) {
        events.add(eventInfo);
    }

    public List<EventInfo> getEvents() {
        return events;
    }

    void drawCalendar() {

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

        //check for leap year
        if(dateFocus.getYear() % 4 !=0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
           int dateOffSet = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        CalendarController calendarController = new CalendarController();
        List<EventInfo> eventInfos = calendarController.getEvents();

        for(int i=0; i<6; i++){
            for(int j=0; j<7; j++){

                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                Rectangle eventRectangle = new Rectangle();
                Circle dot = new Circle();
                VBox eventContainer = new VBox();

                //day in the month tile
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.GRAY);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle); //shows the calendar tiles

                //day in the month with event attached to it tile
                dot.setFill(Color.TRANSPARENT);
                dot.setStroke(Color.YELLOW);
                dot.setStrokeWidth(strokeWidth);
                dot.setRadius(10);
                eventContainer.setAlignment(Pos.TOP_RIGHT);
                eventContainer.getChildren().addAll(eventRectangle, dot); //is ready to replace a given tile


                int calculateDate = (j + 1) + (7 * i);

                if(calculateDate > dateOffSet){

                    int currentDate = calculateDate - dateOffSet;

//                    LocalDate currentDate1 = LocalDate.from(dateFocus.withDayOfMonth(currentDate));
//                    for (EventInfo eventInfo : eventInfos) {
//                        if (eventInfo.getDate().equals(currentDate1)) {
//                            Text eventNameText = new Text(eventInfo.getName());
//                            eventContainer.getChildren().add(eventNameText);
//                        }
//                    }
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);
                    } else {
                        rectangle.setDisable(true);
                    }
                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);
                        rectangle.setFill(Color.DARKGRAY);
                    }
                } else {
                    rectangle.setDisable(true);}

                if (!rectangle.isDisable()) {
                    rectangle.setOnMouseClicked(event -> {
                        // Determine the x and y coordinates of the click relative to the screen
                        double x = event.getScreenX();
                        double y = event.getScreenY();

                        // Show the popup at the specified location
                        eventPopup.show(rectangle.getScene().getWindow(), x, y);
                    });
                }
                if(rectangle.isDisable()) {
                    rectangle.setFill(Color.LIGHTGRAY);
                }
                calendar.getChildren().add(stackPane);


            }
        }
    }
}