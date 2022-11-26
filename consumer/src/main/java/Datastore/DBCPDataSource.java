package Datastore;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// creating the shared pool datasource for mysql

public class DBCPDataSource {
    private static BasicDataSource dataSource;
    private static String url = "jdbc:mysql://localhost:3306/6650db";
    private static String user = "root";
    private static String password ="password";


    static {
        dataSource = new BasicDataSource();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //DriverManager.getConnection(url, user, password);
            //System.out.println("connection is live!");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
    }

    public static BasicDataSource getDataSource() {
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}