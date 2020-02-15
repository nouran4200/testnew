package com.iti.chat.service;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.iti.chat.model.ChatRoom;
import com.iti.chat.model.Message;
import com.iti.chat.model.User;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatRoomService extends Remote {
    ChatRoom createNewChatRoom(List<User> users) throws RemoteException;
    void sendMessage(Message message, ChatRoom room) throws RemoteException;
    void sendFile(Message message, RemoteInputStream remoteInputStream) throws IOException;
    void getFile(String path, ClientService clientService) throws IOException;
}
