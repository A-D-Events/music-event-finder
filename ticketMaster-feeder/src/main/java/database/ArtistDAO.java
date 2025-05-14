package database;

import config.ConfigLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArtistDAO {

	public static List<String> getAllArtists() {
		List<String> artists = new ArrayList<>();
		String dbUrl = ConfigLoader.getProperty("db.url");

		try (Connection conn = DriverManager.getConnection(dbUrl);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT name FROM artists")) {

			while (rs.next()) {
				artists.add(rs.getString("name"));
			}

		} catch (Exception e) {
			System.err.println("❌ Error reading artists from database: " + e.getMessage());
		}

		return artists;
	}
}
