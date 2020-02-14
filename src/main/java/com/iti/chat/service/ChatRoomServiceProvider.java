package com.iti.chat.service;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.iti.chat.model.ChatRoom;
import com.iti.chat.model.Message;
import com.iti.chat.model.User;
import com.iti.chat.model.UserStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomServiceProvider extends UnicastRemoteObject implements ChatRoomService{
    private static ChatRoomServiceProvider instance;
    private static final int BUFFER_SIZE = 1024 * 1024;
    private ChatterBotSession chatterBotSession;
    private ChatRoomServiceProvider() throws RemoteException {

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
        broadcast(message, room, false);

    }

    @Override
    public void sendFile(Message message, RemoteInputStream remoteInputStream) throws IOException {
        InputStream fileData = RemoteInputStreamClient.wrap(remoteInputStream);
        ReadableByteChannel from = Channels.newChannel(fileData);
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        String path = "uploaded files/" + message.getContent();
        System.out.println("saving at " + path);
        WritableByteChannel to = FileChannel.open(Paths.get(path), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        while (from.read(buffer) != -1)
        {
            buffer.flip();
            while (buffer.hasRemaining()) {
                to.write(buffer);
            }
            buffer.clear();
        }
        from.close();
        to.close();
        fileData.close();

        // set message remotePath
    }

    private void sendAutomatedMessages(ChatRoom room, Message lastMessage) {
        List<User> busyUsers = room.getUsers().stream().
                filter(user -> user.isChatBotEnabled() && user.getStatus() == UserStatus.BUSY).
                collect(Collectors.toList());
        if(!busyUsers.isEmpty()) {
            initChatBot();
            try {
                String automatedMessage = chatterBotSession.think(lastMessage.getContent());
                busyUsers.forEach(user -> {
                    Message message = new Message();
                    message.setSender(user);
                    message.setContent(automatedMessage);
                    try {
                        broadcast(message, room, true);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void initChatBot() {
        ChatterBotFactory factory = new ChatterBotFactory();
        try {
            ChatterBot bot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            chatterBotSession = bot.createSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void broadcast(Message message, ChatRoom room, boolean automated) throws RemoteException {
        SessionServiceProvider sessionServiceProvider = SessionServiceProvider.getInstance();
        room.getUsers().parallelStream().filter(user -> !(user.getStatus() == UserStatus.OFFLINE))
            .map(user -> sessionServiceProvider.getClient(user)).forEach(client -> {
            try {
                client.receiveMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        if(!automated) {
            sendAutomatedMessages(room, message);
        }

    }
}
