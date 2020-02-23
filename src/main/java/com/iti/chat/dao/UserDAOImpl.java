package com.iti.chat.dao;

import com.iti.chat.exception.DuplicatePhoneException;
import com.iti.chat.model.User;
import com.iti.chat.model.UserStatus;
import com.iti.chat.service.DBConnection;
import com.iti.chat.util.StringUtil;
import com.iti.chat.util.adapter.UserAdapter;

import java.sql.*;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static UserDAOImpl instance;

    private UserDAOImpl() {

    }

    public static UserDAOImpl getInstance() {
        if (instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }

    @Override
    public List<User> searchByName(String searchQuery) throws SQLException {
        searchQuery = "%" + searchQuery + "%";
        searchQuery = StringUtil.addSingleQuotes(searchQuery);
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String nameQuery = "select * from users where first_name like " + searchQuery +
                    " or last_name like " + searchQuery;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(nameQuery);
            List<User> users = UserAdapter.createUsers(resultSet);
            DBConnection.getInstance().closeConnection(connection);
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateInfo(User user) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String updateQuery = "UPDATE chatty.users " +
                    "SET first_name = '" + user.getFirstName() + "', last_name = '" + user.getLastName()
                    + "' , phone = '" + user.getPhone() + "', email = '" + user.getPhone() +
                    "', country = '" + user.getCountry() +
                    "' where user_id = " + user.getId();
            //System.out.println(updateQuery);
            Statement statement = connection.createStatement();
            statement.execute(updateQuery);
            //statement.executeUpdate(updateQuery);
            DBConnection.getInstance().closeConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void updateUserPassword(User user) throws SQLException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String updateQuery = "UPDATE chatty.users " +
                    "SET password = '" + user.getPassword() +
                    "' where user_id = " + user.getId();
            //System.out.println(updateQuery);
            Statement statement = connection.createStatement();
            statement.execute(updateQuery);
            //statement.executeUpdate(updateQuery);
            DBConnection.getInstance().closeConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateImage(String url, User user) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String updateQuery = "UPDATE chatty.users " +
                "SET image_uri = '" + url +
                "' where phone = " + user.getPhone();
        Statement statement = connection.createStatement();
        statement.execute(updateQuery);
        DBConnection.getInstance().closeConnection(connection);

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
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    public User findUserById(int id)  throws SQLException{
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "select * from users where user_id = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            User user = UserAdapter.createUser(resultSet);
            DBConnection.getInstance().closeConnection(connection);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    public User login(String phone, String password) throws SQLException {
        phone = StringUtil.addSingleQuotes(phone);
        password = StringUtil.addSingleQuotes(password);
        String query = "select * from users where phone = " + phone +
                " and password = " + password;
        Connection connection = DBConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        User user = UserAdapter.createUser(resultSet);
        if (user != null) {
            FriendRequestDAO friendRequestDAO = FriendRequestDAOImpl.getInstance();
            user.setFriends(friendRequestDAO.getFriends(user));
            user.setStatus(UserStatus.ONLINE);
        }
        DBConnection.getInstance().closeConnection(connection);
        return user;
    }

    @Override
    public User register(User user, String password) throws SQLException {
        if (findUserByPhone(user.getPhone()) == null) {
            String query = "insert into users (first_name, last_name, email, password, phone, gender, country)" +
                    " values (?, ?, ?, ?, ?, ?, ?)";
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, user.getFirstName());
                preparedStatement.setString(2, user.getLastName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, password);
                preparedStatement.setString(5, user.getPhone());
                preparedStatement.setInt(6, user.getGender());
                preparedStatement.setString(7, user.getCountry());
                preparedStatement.executeUpdate();
                ResultSet tableKeys = preparedStatement.getGeneratedKeys();
                tableKeys.next();
                user.setId(tableKeys.getInt(1));
                System.out.println(user.getId());
                DBConnection.getInstance().closeConnection(connection);
                return user;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            throw new DuplicatePhoneException("phone is already used");
        }
    }


    @Override
    public List<User> getAllUsers() throws SQLException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String nameQuery = "select * from users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(nameQuery);
            List<User> users = UserAdapter.createUsers(resultSet);
            DBConnection.getInstance().closeConnection(connection);
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> searchByPhone(String searchQuery) throws SQLException {
        searchQuery = "%" + searchQuery + "%";
        searchQuery = StringUtil.addSingleQuotes(searchQuery);
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String phoneQuery = "select * from users where phone like " + searchQuery ;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(phoneQuery);
            List<User> users = UserAdapter.createUsers(resultSet);
            DBConnection.getInstance().closeConnection(connection);
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
