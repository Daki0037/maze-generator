module me.danilo.mazegenerator {
    requires javafx.controls;
    requires javafx.fxml;


    opens me.danilo.mazegenerator to javafx.fxml;
    exports me.danilo.mazegenerator;
    opens me.danilo.mazegenerator.controllers to javafx.fxml;
    exports me.danilo.mazegenerator.controllers to javafx.fxml;
}