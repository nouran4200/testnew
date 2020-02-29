package com.iti.chat.service;

import com.iti.chat.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface FriendRequestsService  extends Remote {
    void sendFriendRequest(ClientService client, User receiver) throws RemoteException;
    void acceptFriendRequest(ClientService client, User sender) throws RemoteException;
    void rejectFriendRequest(ClientService client, User sender) throws RemoteException;
    User findUserByPhone(String phone) throws RemoteException, SQLException;
    List<User> searchByPhone(String phone) throws RemoteException, SQLException;
    List<User> pendingFriendRequests(ClientService client) throws RemoteException;
    List<User> pendingFriendRequestsSent(ClientService client) throws RemoteException;
}
