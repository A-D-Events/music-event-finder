package control;

import config.ConfigLoader;
import database.EventDAO;
import model.Event;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

public class TicketmasterController {

	private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json";

	public static void initializeDatabase() {
		if (!EventDAO.isDatabaseInitialized()) {
			EventDAO.initializeDatabase();
			System.out.println("✅ Database initialized.");
		} else {
			System.out.println("ℹ️ Database was already initialized.");
		}
	}

	public static void setApiKey(String apiKey) {
		ConfigLoader.setProperty("ticketmaster.api.key", apiKey);
	}

	public static void fetchAndStoreEvents(String segment, String genre, String countryCode, String city, String keyword, int pages) {
		String apiKey = ConfigLoader.getProperty("api.key"); // lee desde config.properties

		if (apiKey == null || apiKey.isEmpty()) {
			System.err.println("❌ API key not found in config.");
			return;
		}

		for (int page = 0; page < pages; page++) {
			try {
				String url = buildUrl(apiKey, segment, genre, countryCode, city, keyword, page);
				JSONObject json = fetchJsonFromUrl(url);
				List<Event> events = parseEvents(json);

				if (events.isEmpty()) {
					System.out.println("😕 No events found on page " + page);
					continue;
				}

				for (Event event : events) {
					EventDAO.saveEvent(event);
					System.out.println("✅ Saved: " + event.name + " by " + event.artist);
				}

			} catch (Exception e) {
				System.err.println("❌ Error on page " + page + ": " + e.getMessage());
			}
		}
	}

	private static String buildUrl(String apiKey, String segment, String genre, String countryCode, String city, String keyword, int page) {
		StringBuilder urlBuilder = new StringBuilder(BASE_URL)
				.append("?apikey=").append(apiKey)
				.append("&size=10")
				.append("&sort=date,asc")
				.append("&page=").append(page);

		if (segment != null) urlBuilder.append("&segmentName=").append(segment);
		if (genre != null) urlBuilder.append("&genreName=").append(genre);
		if (countryCode != null) urlBuilder.append("&countryCode=").append(countryCode);
		if (city != null) urlBuilder.append("&city=").append(city);
		if (keyword != null && !keyword.isEmpty()) urlBuilder.append("&keyword=").append(keyword);

		return urlBuilder.toString();
	}

	private static JSONObject fetchJsonFromUrl(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("HTTP error code: " + conn.getResponseCode());
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			return new JSONObject(result.toString());
		}
	}

	private static List<Event> parseEvents(JSONObject json) {
		List<Event> eventList = new ArrayList<>();

		if (!json.has("_embedded")) return eventList;

		JSONArray events = json.getJSONObject("_embedded").getJSONArray("events");

		for (int i = 0; i < events.length(); i++) {
			JSONObject e = events.getJSONObject(i);

			String name = e.optString("name", "Unknown");
			String date = e.getJSONObject("dates").getJSONObject("start").optString("localDate", "N/A");
			String time = e.getJSONObject("dates").getJSONObject("start").optString("localTime", "N/A");

			JSONObject venueObj = e.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
			String venue = venueObj.optString("name", "Unknown venue");
			String address = venueObj.getJSONObject("address").optString("line1", "No address");

			String link = e.optString("url", "No link");

			JSONArray attractions = e.getJSONObject("_embedded").optJSONArray("attractions");
			String artist = (attractions != null && attractions.length() > 0)
					? attractions.getJSONObject(0).optString("name", "Unknown artist")
					: "Unknown artist";

			JSONArray images = e.optJSONArray("images");
			String image = (images != null && images.length() > 0)
					? images.getJSONObject(0).optString("url", "")
					: "";

			JSONArray priceRanges = e.optJSONArray("priceRanges");
			String price = "Unavailable";
			if (priceRanges != null && priceRanges.length() > 0) {
				JSONObject priceObj = priceRanges.getJSONObject(0);
				double min = priceObj.optDouble("min", -1);
				double max = priceObj.optDouble("max", -1);
				String currency = priceObj.optString("currency", "€");
				price = min + " - " + max + " " + currency;
			}

			JSONObject classification = e.optJSONArray("classifications").getJSONObject(0);
			String segment = classification.getJSONObject("segment").optString("name", "Unknown");
			String genre = classification.getJSONObject("genre").optString("name", "Unknown");

			if (name.equals("Unknown") || artist.equals("Unknown artist") || date.equals("N/A")) continue;

			boolean alreadyExists = eventList.stream().anyMatch(ev ->
					ev.name.equals(name) && ev.artist.equals(artist) && ev.date.equals(date));
			if (alreadyExists) continue;

			Event event = new Event(name, artist, date, time, venue, address, price, segment, genre, link, image);
			eventList.add(event);
		}

		return eventList;
	}
	public static List<Event> fetchAndReturnEvents(String segment, String genre, String countryCode, String city, String keyword, int pages) {
		List<Event> collected = new ArrayList<>();
		String apiKey = ConfigLoader.getProperty("api.key");

		if (apiKey == null || apiKey.isEmpty()) {
			System.err.println("❌ API key not found in config.");
			return collected;
		}

		for (int page = 0; page < pages; page++) {
			try {
				String url = buildUrl(apiKey, segment, genre, countryCode, city, keyword, page);
				JSONObject json = fetchJsonFromUrl(url);
				List<Event> events = parseEvents(json);
				collected.addAll(events);
			} catch (Exception e) {
				System.err.println("❌ Error on page " + page + ": " + e.getMessage());
			}
		}

		return collected;
	}

}
