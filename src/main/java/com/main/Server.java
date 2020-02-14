package com.main;

import com.iti.chat.service.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public Server() {
        try {
            
            Registry registry = LocateRegistry.createRegistry(4000);
            ChatRoomService chatRoomService = ChatRoomServiceProvider.getInstance();
            SessionService sessionService = SessionServiceProvider.getInstance();
            FriendRequestsService friendRequestsService = FriendRequestServiceProvider.getInstance();
            registry.rebind("chatRoomService", chatRoomService);
            registry.rebind("sessionService", sessionService);
            registry.rebind("friendRequestsService", friendRequestsService);
            System.out.println("server running");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
        //Main.main(args);
    }
}
