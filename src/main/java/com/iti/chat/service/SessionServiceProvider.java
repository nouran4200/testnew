package com.iti.chat.service;

import com.iti.chat.model.User;

import java.util.Map;
import java.util.TreeMap;

public class SessionServiceProvider implements SessionService {
    private Map<User, ClientService> managedSessions;
    private static SessionServiceProvider instance;
    private SessionServiceProvider() {

        managedSessions = new TreeMap<>();
    }

    public static SessionServiceProvider getInstance() {
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
    public User login(String phone, String password) {
        return null;
    }

    @Override
    public void logout(User user) {

    }

}
