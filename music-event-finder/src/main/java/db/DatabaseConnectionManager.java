package db;
import java.sql.Connection;
import java.sql.DriverManager;
import config.ConfigKey;


public class DatabaseConnectionManager {
	private static final String DB_URL = ConfigKey.getProperty("db.url");

	public static Connection getConnection() throws Exception {
		return DriverManager.getConnection(DB_URL);
	}
}
