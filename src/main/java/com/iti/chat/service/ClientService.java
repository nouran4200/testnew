package com.iti.chat.service;

import com.iti.chat.model.ChatRoom;
import com.iti.chat.model.Message;
import com.iti.chat.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientService extends Remote {
    User login(String phone, String password) throws RemoteException;
    void logout() throws RemoteException;
    void sendMessage(Message message, ChatRoom chatRoom) throws RemoteException;
    void receiveMessage(Message message, ChatRoom chatRoom) throws RemoteException;
}
