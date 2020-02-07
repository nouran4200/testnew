module ChatServer {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires java.rmi;
    requires java.naming;
    requires mysql.connector.java;
    opens com.main;
    exports com.iti.chat.controller to javafx.fxml;
}