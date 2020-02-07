package com.iti.chat.service;

import com.iti.chat.model.User;

public interface SessionService {
    User login(String phone, String password);
    void logout(User user);
    ClientService getClient(User user);
}
