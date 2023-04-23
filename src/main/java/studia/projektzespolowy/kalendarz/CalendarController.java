package studia.projektzespolowy.kalendarz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private TextField year;

    @FXML
    private TextField month;

    @FXML
    private GridPane calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus=ZonedDateTime.now();
        today=ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent ignoredEvent){
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent ignoredEvent){
        dateFocus= dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar(){
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate=28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for(int i=0; i<6; i++){
            for(int j=0; j<7;j++){
                Button button = new Button();

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate>dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate<=monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(button.getScaleY()/2)*0.75;
                        date.setTranslateY(textTranslationY);
                        button.setText(date.toString());
                    }

                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                        button.fire();
                    }
                }

            }
        }
    }


}
