package com.iti.chat.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController implements Initializable {

    @FXML
    AdminbarController adminbarController;

    @FXML
    BorderPane root;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        adminbarController.serviceButton.setOnAction(ae -> {
//            try {
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation(HomeController.class.getResource("/fxml/Service.fxml"));
//                Parent parent = loader.load();
//                root.setCenter(parent);
//            } catch (IOException ex) {
//                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
        adminbarController.announceButton.setOnAction(ae -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HomeController.class.getResource("/fxml/Announcement.fxml"));
                Parent parent = loader
                        .load();
                root.setCenter(parent);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        adminbarController.StatsButton.setOnAction(ae -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HomeController.class.getResource("/fxml/stats.fxml"));
                Parent parent = loader.load();
                root.setCenter(parent);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        adminbarController.addContactButton.setOnAction(ae -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HomeController.class.getResource("/fxml/addContact.fxml"));
                Parent parent = loader.load();
                root.setCenter(parent);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        adminbarController.crudButton.setOnAction(ae -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HomeController.class.getResource("/fxml/TableView.fxml"));
                Parent parent = loader
                        .load();
                root.setCenter(parent);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

//        root.getChildren().add(makePieFemaleMale(males, females));
//        root.getChildren().add(makeOnlineOffline(online, offline));
//        root.getChildren().add(barCountry(countryMap));

    }

}
