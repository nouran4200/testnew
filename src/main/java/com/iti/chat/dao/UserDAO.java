package com.iti.chat.dao;

import com.iti.chat.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    public User login(String phone, String password) throws SQLException;
    public User findUserByPhone(String phone) throws SQLException;
    public User register(User user, String password) throws SQLException;
    public List<User> searchByName(String searchQuery) throws SQLException;
}
