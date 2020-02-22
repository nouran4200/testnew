package com.iti.chat.service;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.iti.chat.dao.UserDAO;
import com.iti.chat.dao.UserDAOImpl;
import com.iti.chat.model.Notification;
import com.iti.chat.model.NotificationType;
import com.iti.chat.model.User;
import com.iti.chat.model.UserStatus;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class SessionServiceProvider extends UnicastRemoteObject implements SessionService {
    private Map<User, ClientService> managedSessions;
    private Map<Integer, User> totalUsers;
    private static SessionServiceProvider instance;

    private SessionServiceProvider() throws RemoteException {

        try {

            managedSessions = new TreeMap<>();
            totalUsers = new TreeMap<>();
            //populating total users map with all the users in thew system fetched by user dao
            UserDAOImpl.getInstance().getAllUsers().stream().forEach((element) -> {

                totalUsers.put(element.getId(), element);

            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SessionServiceProvider getInstance() throws RemoteException {
        if (instance == null) {
            instance = new SessionServiceProvider();
        }
        return instance;
    }

    @Override
    public ClientService getClient(User user) {

        return managedSessions.get(user);
    }

    public int onlineUsers() {
        return managedSessions.size();
    }

    @Override
    public void updateInfo(User user) {
        UserDAO userDAO = UserDAOImpl.getInstance();
        try {
            userDAO.updateInfo(user);
        } catch (SQLException e) {
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
    public User login(String phone, String password, ClientService client) throws SQLException, RemoteException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        User user = userDAO.login(phone, password);

        //checking the list of all the friends of the user in order to update their status
        //to have the contact list in the client side populated with the most recent update
        //of his friends' statuses

        //looping over all the friends to update their status
        for (User friend : user.getFriends()) {

            //checking the map of the online users if this friend is online

            //if the user exists then get his recent status
            if (managedSessions.get(friend) != null) {

                User onlineFriend = managedSessions.get(friend).getUser();
                friend.setStatus(onlineFriend.getStatus());

            }

            //else the friend's status is by default offline

        }
        if (user != null) {
            //login process
            user.setStatus(UserStatus.ONLINE);
            managedSessions.put(user, client);
            totalUsers.put(user.getId() , user);
            client.setUser(user);
        }

        //Notify all user's friends with his recent presence
        Notification notification = new Notification(user , null , NotificationType.STATUS_UPDATE);
        notifyUsersFriends(notification);

        return user;
    }

    private void notifyUsersFriends(Notification notification){

        List<User> friends = notification.getSource().getFriends();

        for(User friend : friends){

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
        managedSessions.remove(user);
        user.setStatus(UserStatus.OFFLINE);
    }

    public void register(User user, String password) throws SQLException, RemoteException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        userDAO.register(user, password);
    }

    public void uploadImage (RemoteInputStream remoteInputStream,ClientService clientService ,User user) throws IOException, SQLException {
        String token = UUID.randomUUID().toString();
        FileTransferServiceProvider fileTransferServiceProvider = FileTransferServiceProvider.getInstance();
        String remotePath = FileTransferServiceProvider.ROOT_FILES_PATH + "/" + token ;
        fileTransferServiceProvider.uploadImage(remotePath, remoteInputStream, clientService);
        UserDAOImpl userDAO = UserDAOImpl.getInstance();
        userDAO.updateImage(remotePath,user );
    }
    
    public Map<User, ClientService> getClientService ()
    {
        return managedSessions;
    }

}
