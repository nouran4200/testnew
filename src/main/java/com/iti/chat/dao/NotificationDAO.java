package com.iti.chat.dao;

import com.iti.chat.model.Notification;
import com.iti.chat.model.User;

import java.sql.SQLException;
import java.util.List;

public interface NotificationDAO {
    public void createNotification(User sender, User receiver, int notificationType) throws SQLException;
    public List<Notification> displayNotification(User user) throws SQLException;
    public void createNotification(Notification notification) throws SQLException;
}
