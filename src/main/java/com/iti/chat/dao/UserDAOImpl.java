package com.iti.chat.dao;

import com.iti.chat.exception.DuplicatePhoneException;
import com.iti.chat.model.User;
import com.iti.chat.model.UserStatus;
import com.iti.chat.service.DBConnection;
import com.iti.chat.util.adapter.UserAdapter;

import java.sql.*;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public List<User> searchByName(String searchQuery) throws SQLException {
        searchQuery = String.format("'%%s%'", searchQuery);
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String nameQuery = "select * from users where first_name like " + searchQuery +
                    " or las_name like " + searchQuery;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(nameQuery);
            List<User> users = UserAdapter.createUsers(resultSet);
            DBConnection.getInstance().closeConnection(connection);
            return users;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findUserByPhone(String phone) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "select * from users where phone = " + phone;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            User user = UserAdapter.createUser(resultSet);
            DBConnection.getInstance().closeConnection(connection);
            return user;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }



    public User login(String phone, String password) throws SQLException {
        String query = "select * from users where phone = " + phone +
                " and password = " + password;
        Connection connection = DBConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        User user = UserAdapter.createUser(resultSet);
        DBConnection.getInstance().closeConnection(connection);
        return user;
    }

    @Override
    public User register(User user, String password) throws SQLException{
        if(findUserByPhone(user.getPhone()) != null) {
            String query = "insert into users (first_name, last_name, user_status, email, password, phone)" +
                    " values (?, ?, ?, ?, ?, ?)";
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, user.getFirstName());
                preparedStatement.setString(2, user.getLastName());
                preparedStatement.setInt(3, UserStatus.ONLINE);
                preparedStatement.setString(4, user.getEmail());
                preparedStatement.setString(5, password);
                preparedStatement.setString(6, user.getPhone());
                preparedStatement.executeUpdate();
                ResultSet tableKeys = preparedStatement.getGeneratedKeys();
                tableKeys.next();
                user.setId(tableKeys.getInt(1));
                DBConnection.getInstance().closeConnection(connection);
                return user;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            throw new DuplicatePhoneException("phone is already used");
        }
    }


    public static void main(String[] args) {
        UserDAO service = new UserDAOImpl();
        FriendRequestDAO friendRequestDAO = new FriendRequestDAOImpl();
    }
}
