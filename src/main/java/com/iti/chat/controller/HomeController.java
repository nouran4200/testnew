package com.iti.chat.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import com.iti.chat.dao.StatisticsDAOImpl;
import com.iti.chat.model.Gender;
import com.iti.chat.service.SessionService;
import com.iti.chat.service.SessionServiceProvider;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class HomeController implements Initializable {

    @FXML
    AdminbarController adminbarController;

    @FXML
    AnchorPane container;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        adminbarController.serviceButton.setOnAction(ae -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HomeController.class.getResource("/fxml/Service.fxml"));
                Parent parent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(parent);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        adminbarController.announcButton.setOnAction(ae -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HomeController.class.getResource("/fxml/Announcment.fxml"));
                Parent parent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(parent);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        adminbarController.StatsButton.setOnAction(ae -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HomeController.class.getResource("/fxml/statisticsScene.fxml"));
                Parent parent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(parent);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        adminbarController.addContactButton.setOnAction(ae -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HomeController.class.getResource("/fxml/newContatct.fxml"));
                Parent parent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(parent);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

//        root.getChildren().add(makePieFemaleMale(males, females));
//        root.getChildren().add(makeOnlineOffline(online, offline));
//        root.getChildren().add(barCountry(countryMap));

    }

}
