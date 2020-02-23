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
    void sendMessage(Message message, int roomId) throws RemoteException;
    void sendFile(Message message, int roomId, RemoteInputStream remoteInputStream) throws IOException;
    void getFile(String path, ClientService clientService) throws IOException;
    ChatRoom getChatRoom(int roomId) throws RemoteException;
    List<ChatRoom> getGroupChatRooms(User user) throws RemoteException;
}
