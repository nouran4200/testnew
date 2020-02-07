package com.iti.chat.service;

import com.iti.chat.dao.UserDAO;
import com.iti.chat.dao.UserDAOImpl;
import com.iti.chat.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class SessionServiceProvider extends UnicastRemoteObject implements SessionService {
    private Map<User, ClientService> managedSessions;
    private static SessionServiceProvider instance;
    private SessionServiceProvider() throws RemoteException {

        managedSessions = new TreeMap<>();
    }

    public static SessionServiceProvider getInstance() throws RemoteException {
        if(instance == null) {
            instance = new SessionServiceProvider();
        }
        return instance;
    }

    @Override
    public ClientService getClient(User user) {

        return managedSessions.get(user);
    }

    @Override
    public User login(String phone, String password, ClientService client) throws SQLException, RemoteException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        User user = userDAO.login(phone, password);
        if(user != null) {
            managedSessions.put(user, client);
            client.setUser(user);
        }
        return user;
    }

    @Override
    public void logout(User user) {

        managedSessions.remove(user);
    }

}
