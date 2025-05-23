package es.ulpgc.database;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import es.ulpgc.config.ConfigLoader;

public class DatabaseManager {
    private static final String DB_PATH = ConfigLoader.getProperty("db.path");
    private static final String INIT_SCRIPT = "spotify-feeder/src/main/resources/init_db.sql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
    }

    public static void initializeDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String sql = new String(Files.readAllBytes(Paths.get(INIT_SCRIPT)), StandardCharsets.UTF_8);
            statement.executeUpdate(sql);
            System.out.println("Databse succesfully initialized.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading initialization script: " + e.getMessage());
        }
    }

    public static boolean isDatabaseInitialized() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String testQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='artists';";
            return statement.executeQuery(testQuery).next();

        } catch (SQLException e) {
            System.err.println("Error checking for an existing database: " + e.getMessage());
            return false;
        }
    }
}
