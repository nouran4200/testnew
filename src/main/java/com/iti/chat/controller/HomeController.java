package com.iti.chat.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
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

public class HomeController implements Initializable {

    @FXML
    FlowPane root;

    public BarChart barCountry(int a , int b) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Countries");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("No of Users");
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Egypt");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("USA");
        
        series1.getData().add(new XYChart.Data<>("Egypt", 5.0)); 
        series1.getData().add(new XYChart.Data<>("USA", 9.0)); 


        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis); 
        barChart.setTitle("Population of Countries");
        barChart.getData().addAll(series1, series2);

        return barChart;

    }

    public PieChart makePieFemaleMale(double male, double female) {
        PieChart pie = new PieChart();
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.addAll(new PieChart.Data("Male", male),
                new PieChart.Data("Female", female)
        );
        pie.setData(data);
        pie.setTitle("Females and Males");
        double sum = male + female;
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

        root.getChildren().add(makePieFemaleMale(20, 30));
        root.getChildren().add(makeOnlineOffline(10, 20));
        root.getChildren().add(barCountry(10, 20));

    }

}
