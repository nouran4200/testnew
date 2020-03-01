/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iti.chat.dao.impl;

import com.iti.chat.dao.StatisticsDAO;
import com.iti.chat.service.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mac
 */
public class StatisticsDAOImpl implements StatisticsDAO {

    private static StatisticsDAOImpl instance;

    private StatisticsDAOImpl() {

    }

    public static StatisticsDAOImpl getInstance() {
        if (instance == null) {
            instance = new StatisticsDAOImpl();
        }
        return instance;
    }

    @Override
    public Map<String, Integer> countriesStats() {
        Map map = new HashMap();
        try {

            Connection connection = DBConnection.getInstance().getConnection();
            String query = "select count(*) as 'count', country from users GROUP BY country ";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int countcol = resultSet.getInt("count");
                String country = resultSet.getString("country");
                map.put(country, countcol);
            }
            DBConnection.getInstance().closeConnection(connection);

        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    @Override

    public Map<Integer, Integer> genderStats() {
        Map map = new HashMap();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "select count(*) as 'count', gender from users GROUP BY gender ";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int countcol = resultSet.getInt("count");
                int gender = resultSet.getInt("gender");
                map.put(gender, countcol);
            }
            DBConnection.getInstance().closeConnection(connection);
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    @Override
    public int allUsersCount() {
        int usersCount = 0;
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "select count(*) as 'count' from users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.first();
            usersCount = resultSet.getInt("count");
            DBConnection.getInstance().closeConnection(connection);

        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usersCount;

    }

}
