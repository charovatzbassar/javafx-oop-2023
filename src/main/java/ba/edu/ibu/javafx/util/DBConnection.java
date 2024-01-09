package ba.edu.ibu.javafx.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;

    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/javafxapp";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "paradigma1230";

    private static Connection connection = null;
    public DBConnection() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
        } catch (SQLException e) {

        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public static Connection getConnection() {
        return connection;
    }


}
