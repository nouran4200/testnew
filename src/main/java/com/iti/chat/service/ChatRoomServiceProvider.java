package com.iti.chat.service;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.iti.chat.dao.NotificationDAO;
import com.iti.chat.dao.NotificationDAOImpl;
import com.iti.chat.model.*;
import com.iti.chat.model.ChatRoom;
import com.iti.chat.model.Message;
import com.iti.chat.model.User;
import com.iti.chat.model.UserStatus;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ChatRoomServiceProvider extends UnicastRemoteObject implements ChatRoomService{
    private static ChatRoomServiceProvider instance;
    private ChatterBotSession chatterBotSession;
    ExecutorService executorService;
    ClientService clientService;
    private ChatRoomServiceProvider() throws RemoteException {
        executorService = Executors.newFixedThreadPool(3);
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

        ClientService clientService = SessionServiceProvider.getInstance().getClient(message.getSender());
        executorService.submit(() -> {
                    for (int i = 0; i < room.getUsers().size(); i++) {

                        if (!message.getSender().equals(room.getUsers().get(i))) {
                            NotificationDAO notificationDAO = new NotificationDAOImpl();
                            try {
                               Notification notification = new Notification(message.getSender(), room.getUsers().get(i), NotificationType.MESSAGE_RECEIVED);
                                notificationDAO.createNotification(notification);
                                 clientService.receiveNotification(notification);
                                System.out.println("send Notification");
                            } catch (SQLException | RemoteException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });

        /*for(int i=0;i<room.getUsers().size();i++){

            if(!message.getSender().equals(room.getUsers().get(i))){
                NotificationDAO notificationDAO=new NotificationDAOImpl();
                try {
                    notification=new  Notification(message.getSender(),room.getUsers().get(i),NotificationType.MESSAGE_RECEIVED);
                    notificationDAO.createNotification(notification);
                   // clientService.receiveNotification(notification);
                    System.out.println("send Notification");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

         */



    }

    @Override
    public void sendFile(Message message, ChatRoom room, RemoteInputStream remoteInputStream) throws IOException {
        String token = UUID.randomUUID().toString();
        FileTransferServiceProvider fileTransferServiceProvider = FileTransferServiceProvider.getInstance();
        ClientService clientService = SessionServiceProvider.getInstance().getClient(message.getSender());
        executorService.submit(() -> {
            try {
                String remotePath = FileTransferServiceProvider.ROOT_FILES_PATH + "/" + token + message.getContent();
                fileTransferServiceProvider.uploadFile(remotePath, remoteInputStream, clientService);
                message.setRemotePath(remotePath);
                sendMessage(message, room);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getFile(String path, ClientService clientService) throws IOException {
        FileTransferServiceProvider fileTransferServiceProvider = FileTransferServiceProvider.getInstance();
        executorService.submit(() -> {
            try {
                fileTransferServiceProvider.downloadFile(path, clientService);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
//        room.getUsers().parallelStream().filter(user -> !(user.getStatus() == UserStatus.OFFLINE))
//            .map(user -> sessionServiceProvider.getClient(user)).forEach(client -> {
//            try {
//                client.receiveMessage(message);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        });

        room.getUsers().parallelStream()
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
