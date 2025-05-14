import control.TicketmasterController;
import model.Event;
import utils.TicketmasterStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class pruebaMain {
	public static void main(String[] args) {
		// Step 1: Initialize event database
		TicketmasterController.initializeDatabase();

		// Step 2: Lista de prueba de artistas
		List<String> artists = Arrays.asList(
				"Bad Bunny",
				"Fito & Fitipaldis",
				"André Rieu",
				"Red Moon Yard",
				"AC/DC",
				"Trueno",
				"Manuel Carrasco",
				"Juan Luis Guerra"
		);

		// Lista para guardar todos los eventos
		List<Event> allEvents = new ArrayList<>();

		// Step 3: Ejecutar una sola vez (para prueba)
		System.out.println("🔍 Starting single-run test for predefined artists...");

		for (String artist : artists) {
			try {
				System.out.println("🎵 Searching events for artist: " + artist);
				List<Event> events = TicketmasterController.fetchAndReturnEvents(
						"Music",     // segment
						null,        // genre (optional)
						"ES",        // countryCode
						null,        // city (optional)
						artist,      // keyword (artist name)
						1            // just 1 page for testing
				);
				allEvents.addAll(events);
			} catch (Exception e) {
				System.err.println("❌ Error fetching events for '" + artist + "': " + e.getMessage());
			}
		}

		// Step 4: Exportar a CSV
		TicketmasterStore.exportEventsToCsv(allEvents, "exported_events.csv");

		System.out.println("✅ Test completed. Check exported_events.csv");
	}
}
