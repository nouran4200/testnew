package com.main;

import com.iti.chat.service.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        new Server();
        Main.main(args);
    }
}
