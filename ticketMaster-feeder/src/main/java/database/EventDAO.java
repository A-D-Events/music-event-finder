package database;

import model.Event;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
	private static final List<Event> store = new ArrayList<>();

	public static boolean isDatabaseInitialized() {
		return !store.isEmpty();
	}

	public static void initializeDatabase() {
		System.out.println("Mock database initialized.");
	}

	public static void saveEvent(Event event) {
		store.add(event);
	}

	public static List<Event> getAllEvents() {
		return new ArrayList<>(store);
	}
}
