package com.iti.chat.dao;

import com.iti.chat.model.Notification;
import com.iti.chat.model.NotificationType;
import com.iti.chat.model.User;
import com.iti.chat.service.DBConnection;
import com.iti.chat.util.StringUtil;
import com.iti.chat.util.adapter.UserAdapter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationDAOImpl implements NotificationDAO {
    private static NotificationDAOImpl instance;

    public NotificationDAOImpl() {

    }

    public static  NotificationDAOImpl  getInstance() {
        if(instance == null) {
            instance = new NotificationDAOImpl();
        }
        return instance;
    }
    @Override
    public void createNotification(User sender, User receiver, int notificationType) throws SQLException {

            try {
                Connection connection = DBConnection.getInstance().getConnection();
                String query = "insert into notifications (source_id, receiver_id,notification_status) values (?,?,?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, sender.getId());
                statement.setInt(2, receiver.getId());
                statement.setInt(3,notificationType);

                statement.execute();
                DBConnection.getInstance().closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    @Override
    public List<Notification> displayNotification(User user) throws SQLException   {

        try {
            List<Notification> notificationList=new ArrayList<>();
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "select n.* from notifications n, users u where n.receiver_id = " + user.getId();
            Statement statement = connection.createStatement();
          //  ResultSet resultSet = statement.executeQuery(query);
            PreparedStatement preparedStatement=connection.prepareStatement(query);
           // ResultSet resultSet=preparedStatement.getResultSet();
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){

                  notificationList.add(new Notification(UserDAOImpl.getInstance().findUserById(resultSet.getInt("source_id")),UserDAOImpl.getInstance().findUserById(resultSet.getInt("receiver_id")),resultSet.getInt("notification_status")));

            }
            //return notificationList;
            /*while(
            resultSet.next()) {
                int cols = resultSet.getMetaData().getColumnCount();
                Notification[] arr = new Notification[cols];
                for (int i = 0; i < cols; i++) {
                    notificationList.add((Notification) resultSet.getObject(i + 1));
                }

            }

             */
            DBConnection.getInstance().closeConnection(connection);
            return notificationList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createNotification(Notification notification) throws SQLException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "insert into notifications (notification_type,source_id, receiver_id,notification_status) values (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, notification.getNotificationType());
            statement.setInt(2, notification.getSource().getId());
            statement.setInt(3, notification.getReceiver().getId());
            statement.setInt(4,notification.getNotificationType());
            statement.execute();
            DBConnection.getInstance().closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
