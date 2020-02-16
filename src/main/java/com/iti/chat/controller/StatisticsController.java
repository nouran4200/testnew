package com.iti.chat.controller;

import com.iti.chat.dao.StatisticsDAOImpl;
import com.iti.chat.model.Gender;
import com.iti.chat.service.SessionServiceProvider;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatisticsController implements Initializable {
    @FXML
    private VBox statsBox;

    @FXML
    private JFXButton countryStatsBtn;

    @FXML
    private JFXButton genderStatsBtn;

    @FXML
    private JFXButton statusStatsBtn;

    StatisticsDAOImpl dao = StatisticsDAOImpl.getInstance();
    Map<String, Integer> countryMap = dao.countriesStats();
    Map<Integer, Integer> genderMap = dao.genderStats();

    public BarChart barCountry(Map<String, Integer> aMap) {
        System.out.println(aMap);
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Countries");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("No of Users");
        BarChart barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle("Population of Countries");
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        countryMap.forEach((key, value) -> {

            series1.getData().add(new XYChart.Data(key, value));

        });
        barChart.getData().addAll(series1);
        return barChart;

    }

    public PieChart makePieFemaleMale(int male, int female) {

        PieChart pie = new PieChart();
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.addAll(new PieChart.Data("Male", male),
                new PieChart.Data("Female", female)
        );
        pie.setData(data);
        pie.setTitle("Females and Males");
        int sum = male + female;
        for (PieChart.Data d : pie.getData()) {
            Node slice = d.getNode();
            double Precent = (d.getPieValue() / sum) * 100;
            String tip = d.getName() + " = " + String.format("%.2f", Precent) + "%";
            Tooltip tt = new Tooltip(tip);
            Tooltip.install(slice, tt);
        }
        return pie;
    }

    public PieChart makeOnlineOffline(double online, double offline) {
        PieChart pie = new PieChart();
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.addAll(new PieChart.Data("Online", online),
                new PieChart.Data("Offline", offline)
        );
        pie.setData(data);
        pie.setTitle("Online And Offline");
        double sum = online + offline;
        for (PieChart.Data d : pie.getData()) {
            Node slice = d.getNode();
            double Precent = (d.getPieValue() / sum) * 100;
            String tip = d.getName() + " = " + String.format("%.2f", Precent) + "%";
            Tooltip tt = new Tooltip(tip);
            Tooltip.install(slice, tt);
        }
        return pie;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int males = genderMap.getOrDefault(Gender.MALE, 0);
        int females = genderMap.getOrDefault(Gender.FEMALE, 0);
        int allCount = dao.allUsersCount();
        int online = 0;
        int offline = 0;
        try {
            online = SessionServiceProvider.getInstance().onlineUsers();
            offline = allCount - online;
        } catch (RemoteException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        countryStatsBtn.setOnAction(ae -> {
            System.out.println("country");
            statsBox.getChildren().clear();
            statsBox.getChildren().add(barCountry(countryMap));

        });
        genderStatsBtn.setOnAction(ae -> {
            System.out.println("gender");
            statsBox.getChildren().clear();
            statsBox.getChildren().add(makePieFemaleMale(males, females));

        });
        int finalOnline = online;
        int finalOffline = offline;
        statusStatsBtn.setOnAction(ae -> {
            System.out.println("status");
            statsBox.getChildren().clear();
            statsBox.getChildren().add(makeOnlineOffline(finalOnline, finalOffline));

        });


    }
}
