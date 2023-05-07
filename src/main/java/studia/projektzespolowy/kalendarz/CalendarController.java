package studia.projektzespolowy.kalendarz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


import java.net.URL;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private TextField year;

    @FXML
    public TextField month;

    @FXML
    public FlowPane calendar;

    @FXML
    public GridPane calendarLabels;

    @FXML
    public GridPane calendarControls;
    private ObservableList<CalendarEvents> tvObservableList = FXCollections.observableArrayList();


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

        ObservableList<String> monthsList = FXCollections.observableArrayList(String.valueOf(dateFocus.getMonth()));
        ComboBox<String> month = new ComboBox<>(monthsList);

        month.setOnAction(event -> {
            String selectedMonth = month.getItems().toString().toUpperCase();
            System.out.println("Selected month: " + selectedMonth);
        });

            calendarControls.getChildren().add(month);
//            monthMenuItem.setOnAction(event -> {
//                // Get the selected month from the menu item
//                Month selectedMonth = Month.valueOf(monthMenuItem.getText().toUpperCase());
//
//                // Update the calendar to display the selected month
//                LocalDate newDate = LocalDate.of(dateFocus.getYear(), selectedMonth, 1);
//                drawCalendar();
//            });
//
//            month.getItems().add(monthMenuItem);
        //}
    }
    EventHandler<MouseEvent> eventHandler = mouseEvent -> {

        CalendarEvents dialog = new CalendarEvents();
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String eventDetails = result.get();

            // Do something with the event details (e.g. display them in the UI, add them to a database, etc.)
        }
    };

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));
        year.setFocusTraversable(false);
        month.setFocusTraversable(false);


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
        int dateOffSet = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();

        for(int i=0; i<6; i++){
            for(int j=0; j<7; j++){

                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();


                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.GRAY);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculateDate = (j + 1) + (7 * i);

                if(calculateDate > dateOffSet){

                    int currentDate = calculateDate - dateOffSet;

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
                    rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
                }

                calendar.getChildren().add(stackPane);


            }
        }
    }
}