package com.iti.chat.service;

import com.iti.chat.dao.*;
import com.iti.chat.model.Notification;
import com.iti.chat.model.NotificationType;
import com.iti.chat.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class FriendRequestServiceProvider extends UnicastRemoteObject implements FriendRequestsService {
    FriendRequestDAO friendRequestDAO = FriendRequestDAOImpl.getInstance();
    private static FriendRequestServiceProvider instance;
    private FriendRequestServiceProvider() throws RemoteException {

    }

    public static FriendRequestServiceProvider getInstance() throws RemoteException {
        if(instance == null) {
            instance = new FriendRequestServiceProvider();
        }
        return instance;
    }
    @Override
    public void sendFriendRequest(ClientService client, User receiver) throws RemoteException {
        friendRequestDAO.sendFriendRequest(client.getUser(), receiver);
        Notification notification = new Notification(client.getUser(), receiver, NotificationType.FRIENDSHIP_REQUEST_RECEIVED);
        NotificationDAO notificationDAO=new NotificationDAOImpl();
        try {
            notificationDAO.createNotification(notification);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void acceptFriendRequest(ClientService client, User sender) throws RemoteException {
        friendRequestDAO.acceptFriendRequest(client.getUser(), sender);
        Notification notification = new Notification(client.getUser(), sender, NotificationType.FRIENDSHIP_ACCEPTED);
        NotificationDAO notificationDAO=new NotificationDAOImpl();
        try {
            notificationDAO.createNotification(notification);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rejectFriendRequest(ClientService client, User sender) throws RemoteException {
        friendRequestDAO.rejectFriendRequest(client.getUser(), sender);
        Notification notification = new Notification(client.getUser(), sender, NotificationType.FRIENDSHIP_REJECTED);
        NotificationDAO notificationDAO=new NotificationDAOImpl();
        try {
            notificationDAO.createNotification(notification);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User searchByPhone(String phone) throws RemoteException, SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        return userDAO.findUserByPhone(phone);
    }

    @Override
    public List<User> pendingFriendRequests(ClientService client) throws RemoteException {
        return friendRequestDAO.pendingFriendRequests(client.getUser());
    }
}
