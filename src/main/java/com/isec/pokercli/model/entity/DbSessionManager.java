package com.isec.pokercli.model.entity;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbSessionManager {

    private static final String URL = "jdbc:mysql://localhost:3306/pokercli";
    private static final String USERNAME = "pokercli";
    private static final String PASSWORD = "pokercli";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return connection;
    }
}