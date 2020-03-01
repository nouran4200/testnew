package com.iti.chat.controller;


import com.iti.chat.service.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServiceManager {
    private static ServiceManager instance;
    private Registry registry;

    public ChatRoomService getChatRoomService() {
        return chatRoomService;
    }

    private ChatRoomService chatRoomService;
    private SessionService sessionService;
    private FriendRequestsService friendRequestsService;
    private FileTransferService fileTransferService;
    private boolean isRunning;

    private ServiceManager() {
        try {
            registry = LocateRegistry.createRegistry(4000);
            chatRoomService = ChatRoomServiceProvider.getInstance();
            sessionService = SessionServiceProvider.getInstance();
            friendRequestsService = FriendRequestServiceProvider.getInstance();
            fileTransferService = FileTransferServiceProvider.getInstance();
            isRunning = false;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }


    public void runServer() throws RemoteException {
        if (!isRunning){

            registry.rebind("chatRoomService", chatRoomService);
            registry.rebind("sessionService", sessionService);
            registry.rebind("friendRequestsService", friendRequestsService);
            registry.rebind("fileTransferService", fileTransferService);
            System.out.println("server running");
            isRunning = true;
        }
    }


    public void closeServer() throws RemoteException, NotBoundException {
        if (isRunning) {
            registry.unbind("friendRequestsService");
            registry.unbind("chatRoomService");
            registry.unbind("sessionService");
            registry.unbind("fileTransferService");

            isRunning = false;
        }

    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
