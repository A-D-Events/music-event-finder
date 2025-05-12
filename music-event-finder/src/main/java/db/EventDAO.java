package db;

import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class EventDAO {

	public static void initializeTable() {
		String sql = "CREATE TABLE IF NOT EXISTS events (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT," +
				"artist TEXT," +
				"date TEXT," +
				"time TEXT," +
				"venue TEXT," +
				"address TEXT," +
				"price TEXT," +
				"segment TEXT," +
				"genre TEXT," +
				"link TEXT," +
				"image TEXT);";

		try (Connection conn = DatabaseConnectionManager.getConnection();
			 Statement stmt = conn.createStatement()) {

			stmt.execute(sql);
			System.out.println("🗃️ 'events' table initialized.");

		} catch (Exception e) {
			System.err.println("❌ Error initializing the table:");
			e.printStackTrace();
		}
	}

	public static void saveEvent(Event event) {
		String sql = "INSERT INTO events(name, artist, date, time, venue, address, price, segment, genre, link, image) " +
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnectionManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, event.name);
			pstmt.setString(2, event.artist);
			pstmt.setString(3, event.date);
			pstmt.setString(4, event.time);
			pstmt.setString(5, event.venue);
			pstmt.setString(6, event.address);
			pstmt.setString(7, event.price);
			pstmt.setString(8, event.segment);
			pstmt.setString(9, event.genre);
			pstmt.setString(10, event.link);
			pstmt.setString(11, event.image);

			pstmt.executeUpdate();

		} catch (Exception e) {
			System.err.println("❌ Error saving the event:");
			e.printStackTrace();
		}
	}
}
