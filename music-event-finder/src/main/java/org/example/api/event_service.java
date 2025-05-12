package org.example.api;

import org.example.api.EventParser;
import org.example.api.TicketmasterClient;
import org.example.db.EventDAO;
import org.example.model.Event;
import org.json.JSONObject;

import java.util.List;

public class EventService {

	/**
	 * Método original: busca y guarda eventos según parámetros específicos.
	 */
	public static void fetchAndStoreEvents(String apiKey, String segment, String genre, String countryCode, String city, String keyword) {
		try {
			JSONObject json = TicketmasterClient.fetchEvents(apiKey, segment, genre, countryCode, city, keyword);
			List<Event> events = EventParser.parseEvents(json);

			if (events.isEmpty()) {
				System.out.println("😕 No events found.");
				return;
			}

			for (Event e : events) {
				System.out.println(e); // override toString() in Event
				EventDAO.saveEvent(e);
			}

		} catch (Exception e) {
			System.err.println("❌ Error during API processing:");
			e.printStackTrace();
		}
	}

	/**
	 * 🆕 Nuevo método: realiza búsqueda paginada de eventos.
	 */
	public static void fetchAndStorePaginatedEvents(String apiKey, EventSearchFilter filter, int pages) {
		for (int page = 0; page < pages; page++) {
			try {
				JSONObject json = TicketmasterClient.fetchEvents(apiKey, filter, page);
				List<Event> events = EventParser.parseEvents(json);

				if (events.isEmpty()) {
					System.out.println("😕 No events found on page " + page);
					continue;
				}

				for (Event e : events) {
					System.out.println(e);
					EventDAO.saveEvent(e);
				}

			} catch (Exception e) {
				System.err.println("❌ Error on page " + page + ":");
				e.printStackTrace();
			}
		}
	}
}
