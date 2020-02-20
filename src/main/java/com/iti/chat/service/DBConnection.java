package com.iti.chat.service;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static final String user = "root";
    private static final String password = "password";
    private static final String url = "jdbc:mysql://localhost:3306/chatty";
    private MysqlDataSource dataSource;
    private static DBConnection dbConnection;

    private DBConnection() {
        dataSource = new MysqlDataSource();
        dataSource.setPassword(password);
        dataSource.setUser(user);
        dataSource.setURL(url);
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
