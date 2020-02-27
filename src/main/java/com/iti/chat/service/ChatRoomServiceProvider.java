package com.iti.chat.service;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.healthmarketscience.rmiio.RemoteInputStream;
import com.iti.chat.model.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChatRoomServiceProvider extends UnicastRemoteObject implements ChatRoomService {
    private static ChatRoomServiceProvider instance;
    private ChatterBotSession chatterBotSession;
    ExecutorService executorService;
    ClientService clientService;
    Map<Integer, ChatRoom> chatRooms;

    private ChatRoomServiceProvider() throws RemoteException {
        executorService = Executors.newFixedThreadPool(3);
        chatRooms = new HashMap<>();
    }

    public static ChatRoomServiceProvider getInstance() throws RemoteException {
        if (instance == null) {
            instance = new ChatRoomServiceProvider();
        }
        return instance;
    }

    @Override
    public ChatRoom createNewChatRoom(List<User> users) {
        for (ChatRoom chatRoom : chatRooms.values()) {
            if (chatRoom.getUsers().containsAll(users) && users.containsAll(chatRoom.getUsers())) {
                return chatRoom;
            }
        }
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUsers(users);
        String name = users.stream().map(user -> user.getFirstName()).collect(Collectors.joining(","));
        chatRoom.setName(name);
        users.forEach(user -> user.getChatRooms().add(chatRoom));
        chatRooms.put(chatRoom.getId(), chatRoom);
        return chatRoom;
    }

    public void userInfoChanged(User user) {
        for(ChatRoom room : chatRooms.values()) {
            for(int i = 0; i < room.getUsers().size(); i++) {
                User userWithOldData = room.getUsers().get(i);
                if(user.equals(userWithOldData)) {
                    room.getUsers().set(i, user);
                }
            }
            for(Message message : room.getMessages()) {
                if(message.getSender().equals(user)) {
                    message.setSender(user);
                }
            }
        }
    }

    @Override
    public List<ChatRoom> getGroupChatRooms(User user) {
        Predicate<ChatRoom> isGroupChat = (ChatRoom room) -> room.getUsers().size() > 2;
        Predicate<ChatRoom> belongsToUser = (ChatRoom room) -> room.getUsers().contains(user);
        return chatRooms.values().stream().filter(belongsToUser.and(isGroupChat)).collect(Collectors.toList());
    }

    public ChatRoom getChatRoom(int roomId) {
        return chatRooms.get(roomId);
    }

    @Override
    public void sendMessage(Message message, int roomId) throws RemoteException {
        ChatRoom room = chatRooms.get(roomId);
        SessionServiceProvider sessionServiceProvider = SessionServiceProvider.getInstance();
        room.getMessages().add(message);
        message.setChatRoom(room);
        User sender = SessionServiceProvider.getInstance().getUser(message.getSender().getId());
        message.setSender(sender);
        broadcast(message, room, false);

        //ClientService clientService = SessionServiceProvider.getInstance().getClient(message.getSender());
        System.out.println("sender " + message.getSender());


//        executorService.submit(() -> {
//            for (int i = 0; i < room.getUsers().size(); i++) {
//
//                if (!message.getSender().equals(room.getUsers().get(i))) {
//                    System.out.println("reciever " + room.getUsers().get(i));
//                    NotificationDAO notificationDAO = new NotificationDAOImpl();
//                    try {
//                        Notification notification = new Notification(message.getSender(), room.getUsers().get(i), NotificationType.MESSAGE_RECEIVED);
//                        notificationDAO.createNotification(notification);
//                        clientService.receiveNotification(notification);
//                        System.out.println("send Notification");
//                    } catch (SQLException | RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });


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
    public void sendFile(Message message, int roomId, RemoteInputStream remoteInputStream) throws IOException {
        String token = UUID.randomUUID().toString();
        FileTransferServiceProvider fileTransferServiceProvider = FileTransferServiceProvider.getInstance();
        ClientService clientService = SessionServiceProvider.getInstance().getClient(message.getSender());
        executorService.submit(() -> {
            try {
                String remotePath = FileTransferServiceProvider.ROOT_FILES_PATH + "/" + token + message.getContent();
                fileTransferServiceProvider.uploadFile(remotePath, remoteInputStream, clientService);
                message.setRemotePath(remotePath);
                sendMessage(message, roomId);
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
        if (!busyUsers.isEmpty()) {
            executorService.submit(() -> {
                initChatBot();
                for (User user : busyUsers) {
                    if (user.equals(lastMessage.getSender())) {
                        continue;
                    }
                    Message message = new Message();
                    message.setStyle(new MessageStyle());
                    message.setSender(user);
                    try {
                        message.setContent(chatterBotSession.think(lastMessage.getContent()));
                        room.getMessages().add(message);
                        message.setChatRoom(room);
                        broadcast(message, room, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
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
        for (User user : room.getUsers()) {
            if(user.getStatus() == UserStatus.OFFLINE) {
                continue;
            }
            ClientService clientService = sessionServiceProvider.getClient(user);
            if (clientService != null) {
                clientService.receiveMessage(message);
                if (!clientService.getUser().equals(message.getSender()))
                    clientService.receiveNotification(new Notification(message.getSender(), clientService.getUser(), NotificationType.MESSAGE_RECEIVED));
            }
        }
        if (!automated) {
            sendAutomatedMessages(room, message);
        }

    }
}
