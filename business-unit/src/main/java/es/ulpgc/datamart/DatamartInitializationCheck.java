package es.ulpgc.datamart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatamartInitializationCheck {
    private static final String DB_PATH = "event_datamart.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
    }

    public static boolean isDatabaseInitialized() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String testQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='artists';";
            return statement.executeQuery(testQuery).next();

        } catch (SQLException exception) {
            System.err.println("Error checking for an existing database: " + exception.getMessage());
            return false;
        }
    }
}
