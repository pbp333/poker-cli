package com.isec.pokercli.services.persistence.session;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbSessionManager {

    private static final String URL = "jdbc:mysql://localhost:3306/pokercli";
    private static final String USERNAME = "pokercli";
    private static final String PASSWORD = "pokercli";

    private static Connection connection;

    private static UnitOfWork unitOfWork = UnitOfWork.getInstance();

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

    public static UnitOfWork getUnitOfWork() {
        return unitOfWork;
    }
}
