package com.iti.chat.service;

import com.iti.chat.dao.FriendRequestDAO;
import com.iti.chat.dao.FriendRequestDAOImpl;
import com.iti.chat.dao.UserDAO;
import com.iti.chat.dao.UserDAOImpl;
import com.iti.chat.model.Notification;
import com.iti.chat.model.NotificationType;
import com.iti.chat.model.User;

import java.rmi.NotBoundException;
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
    public void sendFriendRequest(ClientService client, User receiver) throws RemoteException, NotBoundException {
        friendRequestDAO.sendFriendRequest(client.getUser(), receiver);
        User sender = client.getUser();
        SessionServiceProvider ssp = SessionServiceProvider.getInstance();
        if(receiver.getFriends().contains(sender)) {
            sender.getFriends().add(receiver);
            ssp.setUser(sender);
            ssp.setUser(receiver);
            ssp.userInfoDidChange(receiver);
            ssp.userInfoDidChange(sender);
        }
        ClientService receiverClient = ssp.getClient(receiver);
        if(receiverClient != null) {
            receiverClient.receiveNotification(new Notification(receiver, sender, NotificationType.FRIENDSHIP_REQUEST_RECEIVED));
        }

        //client.receiveNotification(new Notification(client.getUser(),receiver,NotificationType.FRIENDSHIP_REQUEST_RECEIVED));
    }

    @Override
    public void acceptFriendRequest(ClientService client, User sender) throws RemoteException, NotBoundException {
        friendRequestDAO.acceptFriendRequest(client.getUser(), sender);
        SessionServiceProvider ssp = SessionServiceProvider.getInstance();
        User receiver = client.getUser();
        receiver.getFriends().add(sender);
        ssp.setUser(sender);
        ssp.setUser(receiver);
        ssp.userInfoDidChange(receiver);
        ssp.userInfoDidChange(sender);
        ClientService senderClient = ssp.getClient(sender);
        if(senderClient != null) {
            senderClient.receiveNotification(new Notification(sender, receiver, NotificationType.FRIENDSHIP_ACCEPTED));
        }
        //client.receiveNotification(new Notification(client.getUser(),sender,NotificationType.FRIENDSHIP_ACCEPTED));

    }

    @Override
    public void rejectFriendRequest(ClientService client, User sender) throws RemoteException {
        friendRequestDAO.rejectFriendRequest(client.getUser(), sender);
        SessionServiceProvider ssp = SessionServiceProvider.getInstance();
        ClientService senderClient = ssp.getClient(sender);
        User receiver = client.getUser();
        if(senderClient != null) {
            senderClient.receiveNotification(new Notification(sender, receiver, NotificationType.FRIENDSHIP_ACCEPTED));
        }
        //client.receiveNotification(new Notification(client.getUser(),sender,NotificationType.FRIENDSHIP_REJECTED));

    }

    public User findUserByPhone(String phone) throws RemoteException, SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        return userDAO.findUserByPhone(phone);
    }

    @Override
    public List<User> searchByPhone(String phone) throws RemoteException, SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        return userDAO.searchByPhone(phone);
    }


    @Override
    public List<User> pendingFriendRequests(ClientService client) throws RemoteException {
        return friendRequestDAO.pendingFriendRequests(client.getUser());
    }

    @Override
    public List<User> pendingFriendRequestsSent(ClientService client) throws RemoteException {
        return friendRequestDAO.pendingFriendRequestsSent(client.getUser());
    }
}
