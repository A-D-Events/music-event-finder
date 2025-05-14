import control.TicketmasterController;
import database.ArtistDAO;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) {
		// Step 1: Initialize event database
		TicketmasterController.initializeDatabase();

		// Step 2: Schedule periodic update
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		Runnable task = () -> {
			System.out.println("🔄 Starting event update for all artists...");

			List<String> artists = ArtistDAO.getAllArtists();

			for (String artist : artists) {
				try {
					System.out.println("🔍 Searching events for artist: " + artist);
					TicketmasterController.fetchAndStoreEvents(
							"Music",     // segment
							null,        // genre
							"ES",        // countryCode
							null,        // city
							artist,      // keyword
							2            // pages
					);
				} catch (Exception e) {
					System.err.println("❌ Error fetching events for artist '" + artist + "': " + e.getMessage());
				}
			}

			System.out.println("✅ Update completed.");
		};

		scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);
		System.out.println("🟢 Event feeder running. Press Ctrl+C to exit.");
	}
}
