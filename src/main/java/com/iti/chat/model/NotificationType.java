package com.iti.chat.model;

import java.io.Serializable;

public class NotificationType implements Serializable{
    public static final int FRIENDSHIP_ACCEPTED = 1;
    public static final int FRIENDSHIP_REJECTED = 2;
    public static final int FRIENDSHIP_REQUEST_RECEIVED = 3;
    public static final int MESSAGE_RECEIVED = 4;
}
