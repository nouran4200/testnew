package com.iti.chat.delegate;

import com.iti.chat.dao.UserDAO;
import com.iti.chat.dao.UserDAOImpl;
import com.iti.chat.model.User;

import java.sql.SQLException;

public class RegiseterDelegate {

    User userResult;
    public User register(User user, String password) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        userResult = userDAO.register(user, password);
        return userResult;
    }

}
