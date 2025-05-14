package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DatabaseManager {
	private static final String DB_URL = "jdbc:sqlite:ticketmaster.db";

	// Inicializa la base de datos y crea la tabla si no existe
	public static void inicializarBaseDeDatos() {
		try (Connection conn = DriverManager.getConnection(DB_URL);
			 Statement stmt = conn.createStatement()) {

			String sql = "CREATE TABLE IF NOT EXISTS eventos (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"nombre TEXT," +
					"artista TEXT," +
					"fecha TEXT," +
					"hora TEXT," +
					"lugar TEXT," +
					"direccion TEXT," +
					"precio TEXT," +
					"segmento TEXT," +
					"genero TEXT," +
					"enlace TEXT," +
					"imagen TEXT" +
					");";

			stmt.execute(sql);
			System.out.println("🗃️ Base de datos inicializada.");

		} catch (Exception e) {
			System.err.println("❌ Error al crear la base de datos:");
			e.printStackTrace();
		}
	}

	// Inserta un evento en la tabla
	public static void guardarEvento(String nombre, String artista, String fecha, String hora, String lugar,
									 String direccion, String precio, String segmento, String genero,
									 String enlace, String imagen) {
		String sql = "INSERT INTO eventos(nombre, artista, fecha, hora, lugar, direccion, precio, segmento, genero, enlace, imagen) " +
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(DB_URL);
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, nombre);
			pstmt.setString(2, artista);
			pstmt.setString(3, fecha);
			pstmt.setString(4, hora);
			pstmt.setString(5, lugar);
			pstmt.setString(6, direccion);
			pstmt.setString(7, precio);
			pstmt.setString(8, segmento);
			pstmt.setString(9, genero);
			pstmt.setString(10, enlace);
			pstmt.setString(11, imagen);

			pstmt.executeUpdate();

		} catch (Exception e) {
			System.err.println("❌ Error al guardar el evento:");
			e.printStackTrace();
		}
	}
}
