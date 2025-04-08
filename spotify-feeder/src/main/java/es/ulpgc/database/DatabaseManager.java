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
    private static final String DB_PATH = ConfigLoader.get("db.path");
    private static final String INIT_SCRIPT = "src/main/resources/init_db.sql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = new String(Files.readAllBytes(Paths.get(INIT_SCRIPT)), StandardCharsets.UTF_8);
            stmt.executeUpdate(sql);
            System.out.println("Base de datos inicializada correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("No se pudo leer el archivo de inicialización: " + e.getMessage());
        }
    }

    public static boolean isDatabaseInitialized() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String testQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='artists';";
            return stmt.executeQuery(testQuery).next();

        } catch (SQLException e) {
            System.err.println("Error al comprobar si la base de datos está inicializada: " + e.getMessage());
            return false;
        }
    }
}
