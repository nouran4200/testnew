package com.iti.chat.dao;

import com.iti.chat.model.Notification;
import com.iti.chat.model.NotificationType;
import com.iti.chat.model.User;
import com.iti.chat.service.DBConnection;
import com.iti.chat.util.adapter.UserAdapter;

import java.sql.*;
import java.util.List;

public class FriendRequestDAOImpl implements FriendRequestDAO {
    private static FriendRequestDAOImpl instance;

    private FriendRequestDAOImpl() {

    }

    public static FriendRequestDAOImpl getInstance() {
        if(instance == null) {
            instance = new FriendRequestDAOImpl();
        }
        return instance;
    }
    @Override
    public void sendFriendRequest(User sender, User receiver) {
        List<User> pendingFriends = pendingFriendRequests(sender);
        NotificationDAO notificationDAO=new NotificationDAOImpl();

        if(pendingFriends.contains(receiver)) {
            acceptFriendRequest(sender, receiver);
        }
        else {
            try {

                Connection connection = DBConnection.getInstance().getConnection();
                String query = "insert into friend_requests (sender_id, receiver_id) values (?,?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, sender.getId());
                statement.setInt(2, receiver.getId());
                statement.execute();
                DBConnection.getInstance().closeConnection(connection);
                Notification notification = new Notification(sender, receiver, 3);
                notificationDAO.createNotification(notification);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void acceptFriendRequest(User receiver, User sender) {
        try {
            NotificationDAO notificationDAO=new NotificationDAOImpl();
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "update friend_requests set status = 1 where sender_id = ? and receiver_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, sender.getId());
            statement.setInt(2, receiver.getId());
            statement.execute();
            sender.getFriends().add(receiver);
            receiver.getFriends().add(sender);
            DBConnection.getInstance().closeConnection(connection);
            Notification notification = new Notification(sender, receiver, NotificationType.FRIENDSHIP_REQUEST_RECEIVED);
            notificationDAO.createNotification(notification);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rejectFriendRequest(User receiver, User sender) {
        try {
            NotificationDAO notificationDAO=new NotificationDAOImpl();
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "update friend_requests set status = 0 where sender_id = ? and receiver_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, sender.getId());
            statement.setInt(2, receiver.getId());
            statement.execute();
            DBConnection.getInstance().closeConnection(connection);
            Notification notification = new Notification(sender, receiver, NotificationType.FRIENDSHIP_REJECTED);
            notificationDAO.createNotification(notification);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> pendingFriendRequests(User user) {//returns users that sent me requests but i didn't reject or accept
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "select u.* from friend_requests fr, users u where fr.sender_id = u.user_id " +
                    "and fr.receiver_id = " + user.getId() + " and status is null";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<User> friendRequests = UserAdapter.createUsers(resultSet);
            DBConnection.getInstance().closeConnection(connection);
            return friendRequests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> pendingFriendRequestsSent(User user) {//returns users that i sent to them requests but they didn't reject or accept
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "select u.* from friend_requests fr, users u where fr.sender_id =" + user.getId() +
                    " and fr.receiver_id = u.user_id and status is null";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<User> friendRequests = UserAdapter.createUsers(resultSet);
            DBConnection.getInstance().closeConnection(connection);
            return friendRequests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public List<User> getFriends(User user) {
        Connection connection ;
        String query1 = "select u2.* from friend_requests fr, users u1, users u2 where " +
                "u1.user_id = fr.sender_id and fr.status = 1 and fr.receiver_id = u2.user_id" +
                " and u2.user_id <> u1.user_id and u1.user_id = " + user.getId();
        String query2 = "select u2.* from friend_requests fr, users u1, users u2 where " +
                "u1.user_id = fr.receiver_id and fr.status = 1 and u2.user_id = fr.sender_id" +
                " and u2.user_id <> u1.user_id and u1.user_id = " + user.getId();
        String query = query1 + " union " + query2;
        try {
            connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<User> friends = UserAdapter.createUsers(resultSet);
            DBConnection.getInstance().closeConnection(connection);
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
}
