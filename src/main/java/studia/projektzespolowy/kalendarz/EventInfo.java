package studia.projektzespolowy.kalendarz;

import java.time.LocalDate;

public class EventInfo {
    private String name;
    private LocalDate date;

    public EventInfo(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }
}
