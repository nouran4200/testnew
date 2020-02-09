package com.iti.chat.model;

import java.io.Serializable;

public class Notification implements Serializable{
    public User source;
    public User receiver;
    public int notificationType;

    public Notification(User source, User receiver, int notificationType) {
        this.source = source;
        this.receiver = receiver;
        this.notificationType = notificationType;
    }

    public User getSource() {
        return source;
    }

    public void setSource(User source) {
        this.source = source;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    @Override
    public String toString() {
        String message = source.getFirstName();
        switch (notificationType) {
            case NotificationType.FRIENDSHIP_ACCEPTED:
                message += " accepted your friend request";
                break;
            case NotificationType.FRIENDSHIP_REJECTED:
                message += " rejected your friend request";
                break;
            case NotificationType.MESSAGE_RECEIVED:
                message += " sent you a message";
                break;
            default:
                message += " sent you a friend request";
        }
        return message;
    }
}
