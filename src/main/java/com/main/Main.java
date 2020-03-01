package com.main;

import com.iti.chat.controller.ServiceManager;
import com.iti.chat.service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //starting the server
        // System.setProperty("java.rmi.server.hostname", "10.145.7.155");
        ServiceManager serviceManager = ServiceManager.getInstance();
        serviceManager.runServer();
        stage.setOnCloseRequest(event -> {
            try {
                serviceManager.closeServer();
//                UnicastRemoteObject.unexportObject(serviceManager.getChatRoomService(), true);
//                UnicastRemoteObject.unexportObject(serviceManager.getSessionService(), true);
//                UnicastRemoteObject.unexportObject(serviceManager.getFileTransferService(), true);
//                UnicastRemoteObject.unexportObject(serviceManager.getFriendRequestsService(), true);

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        });
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        stage.setScene(new Scene(root, 900, 700));
        stage.show();
    }

}
