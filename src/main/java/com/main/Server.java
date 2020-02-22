package com.main;

import com.iti.chat.dao.FriendRequestDAOImpl;
import com.iti.chat.dao.UserDAO;
import com.iti.chat.dao.UserDAOImpl;
import com.iti.chat.model.User;

import java.sql.SQLException;

public class Server {

    public static void main(String[] args) throws SQLException {
        new Server();
     /* User shimaa= UserDAOImpl.getInstance().findUserByPhone("01005953821");
       User alaa=UserDAOImpl.getInstance().findUserByPhone("01006863721");
        //User alyaa=UserDAOImpl.getInstance().findUserByPhone("01111665936");
           FriendRequestDAOImpl.getInstance().sendFriendRequest(
               shimaa,alaa);
       FriendRequestDAOImpl.getInstance().sendFriendRequest(alaa,shimaa);
       /* FriendRequestDAOImpl.getInstance().sendFriendRequest(
                alyaa,alaa);
        FriendRequestDAOImpl.getInstance().sendFriendRequest(alaa,alyaa);

*/
        Main.main(args);
    }
}
