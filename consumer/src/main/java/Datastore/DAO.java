package Datastore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAO {
    protected void execute(String queryStatement) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            preparedStatement = connection.prepareStatement(queryStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}