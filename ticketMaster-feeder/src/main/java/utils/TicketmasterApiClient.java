package utils;

import config.ConfigLoader;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class TicketmasterApiClient {

	private final String apiKey;

	public TicketmasterApiClient() {
		this.apiKey = ConfigLoader.getProperty("api.key");
		if (this.apiKey == null || this.apiKey.isEmpty()) {
			throw new IllegalArgumentException("API key is missing.");
		}
	}

	public JSONObject searchEvents(String keyword, int page) throws Exception {
		String endpoint = "https://app.ticketmaster.com/discovery/v2/events.json" +
				"?apikey=" + apiKey +
				"&keyword=" + keyword +
				"&segmentName=Music&countryCode=ES" +
				"&page=" + page +
				"&size=10&sort=date,asc";

		URL url = URI.create(endpoint).toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		if (connection.getResponseCode() == 200) {
			StringBuilder response = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
			}
			return new JSONObject(response.toString());
		} else {
			throw new RuntimeException("Ticketmaster API error: " + connection.getResponseCode());
		}
	}
}
