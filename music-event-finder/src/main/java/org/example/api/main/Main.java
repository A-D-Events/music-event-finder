package org.example;

import org.example.config.ConfigKey;
import org.example.db.EventDAO;
import org.example.api.EventService;


public class Main {
	public static void main(String[] args) {
		String apiKey = ConfigKey.getApiKey();

		if (apiKey != null && !apiKey.isEmpty()) {
			EventDAO.initializeTable();

			List<String> topArtists = Arrays.asList("Arctic Monkeys", "Bad Bunny", "Peso Pluma");

			for (String artist : topArtists) {
				System.out.println("\n🔎 Searching events for: " + artist);
				EventService.fetchAndStoreEvents(apiKey, "music", null, "ES", "Madrid", artist);
			}

			System.out.println("\n✅ All events have been stored in the database.");
		} else {
			System.out.println("❌ API Key not found.");
		}
	}
}
