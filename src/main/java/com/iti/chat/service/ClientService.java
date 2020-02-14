package com.iti.chat.service;

import com.iti.chat.model.ChatRoom;
import com.iti.chat.model.Message;
import com.iti.chat.model.Notification;
import com.iti.chat.model.User;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface ClientService extends Remote {
    User getUser() throws RemoteException;
    void setUser(User user) throws RemoteException;
    void sendMessage(Message message, ChatRoom room) throws RemoteException, NotBoundException;
    void receiveMessage(Message message) throws RemoteException;
    void receiveNotification(Notification notification) throws RemoteException;
    void updateUserInfo(User user) throws RemoteException, SQLException;
    void didSendNBytes(long n) throws RemoteException;
}
