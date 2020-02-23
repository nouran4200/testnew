package com.iti.chat.service;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.iti.chat.model.User;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface SessionService extends Remote {
    User login(String phone, String password, ClientService client) throws RemoteException, SQLException, NotBoundException;
    void logout(User user) throws RemoteException;
    void register(User user, String password) throws RemoteException, SQLException;
    ClientService getClient(User user) throws RemoteException;
    void updateInfo(User user) throws RemoteException;
    void updateUserPassword(User user) throws RemoteException;
    void uploadImage (RemoteInputStream remoteInputStream, ClientService clientService , User user) throws IOException, SQLException;
    void userInfoDidChange(User user) throws RemoteException;

}
