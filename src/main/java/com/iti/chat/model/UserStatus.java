package com.iti.chat.model;

import java.io.Serializable;

public class UserStatus implements Serializable {
    public static final int OFFLINE = 0;
    public static final int BUSY = 1;
    public static final int AWAY = 2;
    public static final int ONLINE = 3;

    public static String statusToString(int status) {

        String stringStatus = null;
        switch (status) {
            case OFFLINE:
                stringStatus = "offline";
                break;
            case BUSY:
                stringStatus = "busy";
                break;
            case AWAY:
                stringStatus = "away";
                break;
            case ONLINE:
                stringStatus = "online";
                break;

        }

        return stringStatus;

    }
}
