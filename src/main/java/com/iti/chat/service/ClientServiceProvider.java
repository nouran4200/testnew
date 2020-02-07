package com.iti.chat.service;

import com.iti.chat.model.ChatRoom;
import com.iti.chat.model.Message;
import com.iti.chat.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientServiceProvider extends UnicastRemoteObject implements ClientService {
    public User currentUser;

    public ClientServiceProvider() throws RemoteException {

    }

    public User getCurrentUser() {

        return currentUser;
    }

    public void setCurrentUser(User currentUser) {

        this.currentUser = currentUser;
    }

    @Override
    public void logout() {
        if(currentUser != null) {
            currentUser = null;
        }
    }

    @Override
    public User login(String phone, String password) {
        return null;
    }

    @Override
    public void sendMessage(Message message, ChatRoom chatRoom) {

    }

    @Override
    public void receiveMessage(Message message, ChatRoom chatRoom) {

    }
}
