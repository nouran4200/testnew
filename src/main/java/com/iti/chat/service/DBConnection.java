package com.iti.chat.service;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private MysqlDataSource dataSource;
    private static DBConnection dbConnection;

    private DBConnection() {
        InputStream inputStream = getClass().getResourceAsStream("/db/dbconfig.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataSource = new MysqlDataSource();
        dataSource.setPassword(properties.getProperty("password"));
        dataSource.setUser(properties.getProperty("user"));
        dataSource.setURL(properties.getProperty("url"));
    }

    public static DBConnection getInstance() {
        if(dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection(Connection connection) throws SQLException {
        if(connection != null) {
            connection.close();
        }
    }
}
