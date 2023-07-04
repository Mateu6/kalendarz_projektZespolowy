package studia.projektzespolowy.kalendarz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Window;


import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    @FXML
    private Button AddButton;
    @FXML
    private ColorPicker ColorPicker;
    @FXML
    private TextField GroupNamePicker;
    @FXML
    private Button ConfirmButton;

    @FXML
    private Pane TestImage;

    @FXML
    private Label TestLabel;
    @FXML
    public void addButtonClicked(ActionEvent ignoredEvent) throws IOException {
        GroupNamePicker.setVisible(true);
        ColorPicker.setVisible(true);
        ConfirmButton.setVisible(true);
        AddButton.setVisible(false);

        /*Add.displayAddWindow();*/
        /*try {
            System.out.println("User is adding an event ...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Window");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading AddWindow.fxml: " + e.getMessage());
            e.printStackTrace();
        }*/
    }


    public void ConfirmButtonClicked(ActionEvent event){
        GroupNamePicker.setVisible(false);
        ColorPicker.setVisible(false);
        ConfirmButton.setVisible(false);
        AddButton.setVisible(true);
        Color PickedColor = ColorPicker.getValue();
        String GroupName = GroupNamePicker.getText();
        TestImage.setBackground(new Background(new BackgroundFill(PickedColor, null, null)));
        TestLabel.setText(GroupName);


    }
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
    private ObservableList<CalendarEvents> tvObservableList = FXCollections.observableArrayList();


    /**Initialize the dateFocus and today variables with the current date
     * Draw the calendar based on the current date*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }
    /**Move the dateFocus back by one month.
     * Clear the calendar and redraw it with the updated date*/
    @FXML
    void backOneMonth(ActionEvent ignoredEvent) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }
    /**Move the dateFocus forward by one month.
     * Clear the calendar and redraw it with the updated date.*/
    @FXML
    void forwardOneMonth(ActionEvent ignoredEvent) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }
    /**redraws the calendar based on selected month in monthPicker*/
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
    //Create an instance of CalendarEvents
    CalendarEvents eventPopup = new CalendarEvents();

    /**
     * Draw the calendar based on the dateFocus.
     * This method constructs the calendar grid, populates it with dates, and handles highlighting and interaction.
     */
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

        //check for leap year
        if(dateFocus.getYear() % 4 !=0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
           int dateOffSet = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

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