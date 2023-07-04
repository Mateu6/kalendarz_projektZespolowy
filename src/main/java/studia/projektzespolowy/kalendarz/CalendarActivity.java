package studia.projektzespolowy.kalendarz;

import java.time.ZonedDateTime;

public class CalendarActivity {


    private ZonedDateTime date;

    /**
     * Sets description to the recaived value
     *
     * @param date New date
     */
    public CalendarActivity(ZonedDateTime date){
        this.date=date;
    }

    /**returns the value of the date variable.*/
    public ZonedDateTime getDate(){return date;}

    /**method is used to set the date to a new value*/
    public void SetDate(ZonedDateTime date){this.date=date;}



}
