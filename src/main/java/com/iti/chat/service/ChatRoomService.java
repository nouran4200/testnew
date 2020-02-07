package com.iti.chat.service;

import com.iti.chat.model.ChatRoom;
import com.iti.chat.model.Message;
import com.iti.chat.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatRoomService extends Remote {
    public ChatRoom createNewChatRoom(List<User> users) throws RemoteException;
    public void sendMessage(Message message, ChatRoom room) throws RemoteException;
}
