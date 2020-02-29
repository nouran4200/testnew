package com.iti.chat.util.adapter;

import com.iti.chat.model.User;
import com.iti.chat.service.SessionServiceProvider;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter {
    public static User createUser(ResultSet resultSet) throws SQLException, RemoteException {
        if(resultSet.next()) {
            int id = resultSet.getInt("user_id");
            if(SessionServiceProvider.getInstance().getUser(id) != null) {
                return SessionServiceProvider.getInstance().getUser(id);
            }
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
            int gender = resultSet.getInt("gender");
            String country = resultSet.getString("country");
            String url = resultSet.getString("image_uri");
            String bio = resultSet.getString("bio");
            String birthDate = resultSet.getString("birthDate");
            int isAddedFromServer = resultSet.getInt("isServer");
            User user = new User();
            user.setCountry(country);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setId(id);
            user.setGender(gender);
            user.setRemoteImagePath(url);
            user.setBio(bio);
            user.setIsAddedFromServer(isAddedFromServer);
            if(birthDate!=null)
                user.setBirthDate(LocalDate.parse(birthDate));
            return user;
        }
        return null;
    }

    public static List<User> createUsers(ResultSet resultSet) throws SQLException, RemoteException {
        List<User> users = new ArrayList<>();
        User user;
        while ((user = createUser(resultSet)) != null) {
            users.add(user);
        }
        return users;
    }
}
