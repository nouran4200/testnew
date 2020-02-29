package com.iti.chat.service;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.iti.chat.dao.UserDAO;
import com.iti.chat.dao.UserDAOImpl;
import com.iti.chat.model.Notification;
import com.iti.chat.model.NotificationType;
import com.iti.chat.model.User;
import com.iti.chat.model.UserStatus;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class SessionServiceProvider extends UnicastRemoteObject implements SessionService {
    private Map<User, ClientService> managedSessions;
    private Map<Integer, User> onlineUsers;
    private static SessionServiceProvider instance;
    boolean changeStatus=false;

    private SessionServiceProvider() throws RemoteException {

        managedSessions = new TreeMap<>();
        onlineUsers = new TreeMap<>();
    }

    public static SessionServiceProvider getInstance() throws RemoteException {
        if (instance == null) {
            instance = new SessionServiceProvider();
        }
        return instance;
    }

    @Override
    public ClientService getClient(User user) {
        if(onlineUsers.containsKey(user.getId())) {
            return managedSessions.get(onlineUsers.get(user.getId()));
        }
        return null;

    }

    public User getUser(int userId) {
        return onlineUsers.get(userId);
    }

    protected void setUser(User user) throws RemoteException, NotBoundException {
        if(onlineUsers.containsKey(user.getId())) {
            ClientService clientService = managedSessions.remove(onlineUsers.get(user.getId()));
            clientService.setUser(user);
            managedSessions.put(user, clientService);
            onlineUsers.put(user.getId(), user);
        }
    }



    public int onlineUsers() {
        return managedSessions.size();
    }

    @Override
    public void updateInfo(User user) throws RemoteException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        System.out.println("update Info"+user);
        try {
            userDAO.updateInfo(user);
            setUser(user);
            userInfoDidChange(user);
           /* Notification notification = new Notification(user, null, NotificationType.STATUS_UPDATE);
            notifyUsersFriends(notification);
      */

            changeStatus=true;
        } catch (SQLException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserPassword(User user) throws RemoteException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        try {
            userDAO.updateUserPassword(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User login(String phone, String password, ClientService client) throws SQLException, RemoteException, NotBoundException {

        UserDAO userDAO = UserDAOImpl.getInstance();
        User user = userDAO.login(phone, password);
        if (user != null) {
            //login process
            onlineUsers.put(user.getId() , user);
            managedSessions.put(user, client);
            for(User friend : user.getFriends()) {
                if(managedSessions.containsKey(friend)) {
                    friend.setStatus(onlineUsers.get(friend.getId()).getStatus());
                }
            }
            client.setUser(user);
            userInfoDidChange(user);
            Notification notification = new Notification(user , null , NotificationType.STATUS_UPDATE);
            notifyUsersFriends(notification);


        }

        //Notify all user's friends with his recent presence


        return user;
    }

    private void notifyUsersFriends(Notification notification){

        List<User> friends = notification.getSource().getFriends();
        for(User friend : friends){
            System.out.println("online size"+friends.size());
            System.out.println("notify user freinds "+friend);
            if (managedSessions.get(friend) != null){

                try {
                    managedSessions.get(friend).receiveNotification(notification);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    @Override
    public void logout(User user) {

        user.setStatus(UserStatus.OFFLINE);
        userInfoDidChange(user);
        managedSessions.remove(onlineUsers.remove(user.getId()));
        notifyUsersFriends(new Notification(user,null,NotificationType.STATUS_UPDATE));
    }

    public void register(User user, String password) throws SQLException, RemoteException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        user.setEmail(user.getFirstName() + "@gmail.com");
        userDAO.register(user, password);
    }

    public void uploadImage (RemoteInputStream remoteInputStream,ClientService clientService ,User user) throws IOException, SQLException, NotBoundException {
        String token = UUID.randomUUID().toString();
        FileTransferServiceProvider fileTransferServiceProvider = FileTransferServiceProvider.getInstance();
        String remotePath = FileTransferServiceProvider.ROOT_FILES_PATH + "/" + token ;
        fileTransferServiceProvider.uploadImage(remotePath, remoteInputStream, clientService);
        UserDAOImpl userDAO = UserDAOImpl.getInstance();
        userDAO.updateImage(remotePath,user);
        user.setRemoteImagePath(remotePath);
        if(onlineUsers.containsKey(user.getId())) {
            setUser(user);
            clientService.setUser(user);
            userInfoDidChange(user);
        }
    }

    @Override
    public void userInfoDidChange(User user) {
        try {
            ChatRoomServiceProvider.getInstance().userInfoChanged(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        broadCastChange(user);
    }

    @Override
    public void updateStatus(User user) throws RemoteException{
        UserDAO userDAO = UserDAOImpl.getInstance();
        System.out.println("update Info"+user);
        try {
            userDAO.updateInfo(user);
            setUser(user);
            userInfoDidChange(user);
            Notification notification = new Notification(user, null, NotificationType.STATUS_UPDATE);
            notifyUsersFriends(notification);


            changeStatus=true;
        } catch (SQLException | NotBoundException e ) {
            e.printStackTrace();
        }
    }


    private void broadCastChange(User user) {
        System.out.printf("broadcasting user data change " + user);
        try {
            getClient(user).userInfoDidChange(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        user.getFriends().stream().filter(u -> onlineUsers.containsKey(u.getId())).
                map(u -> getClient(u)).forEach(client -> {
            try {
                client.userInfoDidChange(user);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public Map<User, ClientService> getClientService ()
    {
        return managedSessions;
    }

}
