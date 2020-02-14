/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iti.chat.dao;

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
      public Map<String, Integer> countriesStats()
    {
        Map map=new HashMap();
        try {
              
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "select count(*) as 'count', country from users GROUP BY country " ;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                int countcol =   resultSet.getInt("count");
               String countrycol =  resultSet.getString("country");
                map.put(countcol , countrycol);
            }


        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    @Override
   
     public Map<Integer, Integer> genderStats(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int allUsersCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
