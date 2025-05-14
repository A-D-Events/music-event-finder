package utils;

import model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketmasterParser {

	public static List<Event> parseEvents(JSONObject json) {
		List<Event> events = new ArrayList<>();

		if (!json.has("_embedded")) return events;

		JSONArray jsonEvents = json.getJSONObject("_embedded").getJSONArray("events");

		for (int i = 0; i < jsonEvents.length(); i++) {
			JSONObject e = jsonEvents.getJSONObject(i);

			String name = e.optString("name", "Unknown");
			String date = e.getJSONObject("dates").getJSONObject("start").optString("localDate", "N/A");
			String time = e.getJSONObject("dates").getJSONObject("start").optString("localTime", "N/A");

			JSONObject venue = e.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
			String venueName = venue.optString("name", "Unknown venue");
			String address = venue.getJSONObject("address").optString("line1", "No address");

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

			Event event = new Event(name, artist, date, time, venueName, address, price, segment, genre, link, image);
			events.add(event);
		}

		return events;
	}
}
