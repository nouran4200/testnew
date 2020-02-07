package com.iti.chat.exception;

import java.sql.SQLException;

public class DuplicatePhoneException extends SQLException {
    public DuplicatePhoneException(String message) {
        super(message);
    }
}
