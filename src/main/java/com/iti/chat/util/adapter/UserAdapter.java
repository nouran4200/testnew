package com.iti.chat.util.adapter;

import com.iti.chat.model.User;
import com.mysql.cj.jdbc.jmx.LoadBalanceConnectionGroupManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter {
    public static User createUser(ResultSet resultSet) throws SQLException {
        if(resultSet.next()) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
            int id = resultSet.getInt("user_id");
            int gender = resultSet.getInt("gender");
            String country = resultSet.getString("country");
            String url = resultSet.getString("image_uri");
            String bio = resultSet.getString("bio");
            String birthDate = resultSet.getString("birthDate");
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
            if(birthDate!=null)
                user.setBirthDate(LocalDate.parse(birthDate));
            return user;
        }
        return null;
    }

    public static List<User> createUsers(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        User user;
        while ((user = createUser(resultSet)) != null) {
            users.add(user);
        }
        return users;
    }
}
