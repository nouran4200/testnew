package com.iti.chat.service;

import com.iti.chat.model.ChatRoom;
import com.iti.chat.model.Message;
import com.iti.chat.model.User;
import com.iti.chat.model.UserStatus;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomServiceProvider extends UnicastRemoteObject implements ChatRoomService{
    private static ChatRoomServiceProvider instance;
    private ChatRoomServiceProvider() throws RemoteException{

    }
    public static ChatRoomServiceProvider getInstance() throws RemoteException {
        if(instance == null) {
            instance = new ChatRoomServiceProvider();
        }
        return instance;
    }

    @Override
    public ChatRoom createNewChatRoom(List<User> users) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUsers(users);
        String name = users.stream().map(user -> user.getFirstName()).collect(Collectors.joining(","));
        chatRoom.setName(name);
        users.forEach(user -> user.getChatRooms().add(chatRoom));
        return chatRoom;
    }

    @Override
    public void sendMessage(Message message, ChatRoom room) throws RemoteException {

        SessionServiceProvider sessionServiceProvider = SessionServiceProvider.getInstance();
        room.getMessages().add(message);
        message.setChatRoom(room);
        broadcast(message, room);

    }

    public void broadcast(Message message, ChatRoom room) throws RemoteException {
        SessionServiceProvider sessionServiceProvider = SessionServiceProvider.getInstance();
        room.getUsers().parallelStream().filter(user -> !(user.getStatus() == UserStatus.OFFLINE))
            .map(user -> sessionServiceProvider.getClient(user)).forEach(client -> {
            try {
                client.receiveMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
