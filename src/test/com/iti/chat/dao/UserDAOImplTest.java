package com.iti.chat.dao;

import com.iti.chat.exception.DuplicatePhoneException;
import com.iti.chat.model.User;
import com.iti.chat.service.DBConnection;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertTrue;

class UserDAOImplTest {
    UserDAOImpl userDAO = new UserDAOImpl();

    @Test
    void searchByName() throws SQLException {
        List<User> resultList1 = userDAO.searchByName("hai");
        List<User> resultList2 = userDAO.searchByName("z");

        Assert.assertThat(resultList1, CoreMatchers.hasItems(new User("shaimaa", "saied", 1, "shai@", "0")));
        assertTrue(resultList2.isEmpty());
    }

    @Test
    void updateInfo() throws SQLException {
        userDAO.updateInfo(new User("hend", "muhammed", 2, "hend", "01011"));
        User user = userDAO.findUserById(2);
        Assert.assertEquals("muhammed", user.getLastName());
        userDAO.updateInfo(new User("hend", "mohamed", 2, "hend", "01011"));
    }

    @Test
    void updateUserPassword() throws SQLException { //heeeeeeeeeeeeeeeeeeeeeeeeeeeeere
        User user1 = new User("hend", "muhammed", 2, "hend", "01011");
        user1.setPassword("12345678910");

        userDAO.updateUserPassword(user1);

        String query = "select * from users where user_id = " + user1.getId();
        Connection connection = null;
        String password = "";
        try {
            connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            password = resultSet.getString("password");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBConnection.getInstance().closeConnection(connection);
        Assert.assertEquals("12345678910",password);

        user1.setPassword("123456789");
        userDAO.updateUserPassword(user1);

    }

    @Test
    void updateImage() throws SQLException {
        User user1 = new User("hend", "muhammed", 2, "hend", "01011");

        userDAO.updateImage("/photos/album/photo.png",user1);

        User user2 = userDAO.findUserById(2);

        Assert.assertEquals("/photos/album/photo.png",user2.getRemoteImagePath());

    }

    @Test
    void findUserByPhone() {
        User user1 = userDAO.findUserByPhone("01011");
        User user2 = userDAO.findUserByPhone("01017265265");

        Assert.assertEquals("01011", user1.getPhone());
        Assert.assertNull(user2);
    }

    @Test
    void findUserById() throws SQLException {
        User user1 = userDAO.findUserById(1);
        User user2 = userDAO.findUserById(5);

        Assert.assertEquals(1, user1.getId());
        Assert.assertNull(user2);
    }

    @Test
    void login() throws SQLException {
        User user1 = userDAO.login("010", "55555");
        User user2 = userDAO.login("01011", "99999");
        User user3 = userDAO.login("01", "123456789");

        User user4=new User("rania", "gamal", 3, "@rania", "010");
        Assert.assertEquals(user4, user1);
        Assert.assertNull(user2);
        Assert.assertNull(user3);
    }

    @Test/*(expected = DuplicatePhoneException.class)*/
    void register() throws SQLException { //test exception is thrown
        User user1 = new User("ahmed", "medhat", "011", "@med", 0, "Egypt");
        try {
            userDAO.register(user1, "123123123");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User user2 = userDAO.findUserByPhone("011");
        Assert.assertEquals(user2, user1);

        String query = "delete from users where phone = " + user1.getPhone();
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnection.getInstance().closeConnection(connection);


        User user3 = new User("ahmed", "medhat", "0", "@med", 0, "Egypt");

        DuplicatePhoneException ex =Assertions.assertThrows(DuplicatePhoneException.class, () -> {
            userDAO.register(user3, "123123123");
        });

        Assertions.assertTrue(ex.getMessage().contains("phone is already used"));

    }

    @Test
    void getAllUsers() throws SQLException {
        List<User> usersList =userDAO.getAllUsers();
        Assert.assertEquals(3,usersList.size());
    }

    @Test
    void searchByPhone() throws SQLException {
        List<User> resultList1 = userDAO.searchByPhone("010");
        List<User> resultList2 = userDAO.searchByPhone("3");

        Assert.assertThat(resultList1, CoreMatchers.hasItems(new User("hend", "mohamed", 2, "hend", "01011"),new User("rania", "gamal", 3, "@rania", "010")));
        assertTrue(resultList2.isEmpty());
    }
}