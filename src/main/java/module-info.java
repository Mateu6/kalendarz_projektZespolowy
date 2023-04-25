module studia.projektzespolowy.kalendarz {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens studia.projektzespolowy.kalendarz to javafx.fxml;
    exports studia.projektzespolowy.kalendarz;
}