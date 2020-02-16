package com.main;

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

    private Registry registry;
    private ChatRoomService chatRoomService;
    private SessionService sessionService;
    private FriendRequestsService friendRequestsService;
    private FileTransferService fileTransferService;

    @Override
    public void start(Stage stage) throws Exception {

        //starting the server

        try {

            registry = LocateRegistry.createRegistry(4000);
            chatRoomService = ChatRoomServiceProvider.getInstance();
            sessionService = SessionServiceProvider.getInstance();
            friendRequestsService = FriendRequestServiceProvider.getInstance();
            fileTransferService = FileTransferServiceProvider.getInstance();
            registry.rebind("chatRoomService", chatRoomService);
            registry.rebind("sessionService", sessionService);
            registry.rebind("friendRequestsService", friendRequestsService);
            registry.rebind("fileTransferService", fileTransferService);
            System.out.println("server running");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        stage.setOnCloseRequest(event -> {

            try {

                registry.unbind("friendRequestsService");
                registry.unbind("chatRoomService");
                registry.unbind("sessionService");
                registry.unbind("fileTransferService");

                UnicastRemoteObject.unexportObject(chatRoomService , false);
                UnicastRemoteObject.unexportObject(sessionService , false);
                UnicastRemoteObject.unexportObject(friendRequestsService , false);
                UnicastRemoteObject.unexportObject(fileTransferService, false);

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }

        });

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        stage.setScene(new Scene(root, 600, 700));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
