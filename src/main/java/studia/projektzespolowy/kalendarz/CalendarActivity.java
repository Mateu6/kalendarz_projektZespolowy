package studia.projektzespolowy.kalendarz;

import java.time.ZonedDateTime;

public class CalendarActivity {

    private ZonedDateTime date;

    public CalendarActivity(ZonedDateTime date){
        this.date=date;
    }

    public ZonedDateTime getDate(){return date;}

    public void SetDate(ZonedDateTime date){this.date=date;}



}