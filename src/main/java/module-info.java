module ChatServer {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires java.rmi;
    requires java.naming;
    requires mysql.connector.java;
    requires rmiio;
    requires chatter.bot.api;
    opens com.main;
    opens com.iti.chat.controller to javafx.fxml;
    exports com.iti.chat.controller to javafx.fxml;
    exports com.iti.chat.service to java.rmi;
}