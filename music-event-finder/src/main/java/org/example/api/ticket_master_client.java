package org.example.api;

import org.example.api.EventSearchFilter;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TicketmasterClient {
	private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json";

	/**
	 * Método original que construye la URL manualmente con parámetros sueltos.
	 */
	public static JSONObject fetchEvents(String apiKey, String segment, String genre, String countryCode, String city, String keyword) throws Exception {
		StringBuilder urlBuilder = new StringBuilder(BASE_URL)
				.append("?apikey=").append(apiKey)
				.append("&segmentName=").append(segment)
				.append("&countryCode=").append(countryCode)
				.append("&city=").append(city)
				.append("&size=10&sort=date,asc");

		if (genre != null) urlBuilder.append("&genreName=").append(genre);
		if (keyword != null && !keyword.isEmpty()) urlBuilder.append("&keyword=").append(keyword);

		return fetchJsonFromUrl(urlBuilder.toString());
	}

	/**
	 * 🆕 Nuevo método que usa un EventSearchFilter + soporte de paginación.
	 */
	public static JSONObject fetchEvents(String apiKey, EventSearchFilter filter, int page) throws Exception {
		StringBuilder urlBuilder = new StringBuilder(BASE_URL)
				.append("?apikey=").append(apiKey)
				.append("&segmentName=").append(filter.segment)
				.append("&countryCode=").append(filter.countryCode)
				.append("&city=").append(filter.city)
				.append("&page=").append(page)
				.append("&size=10&sort=date,asc");

		if (filter.genre != null) urlBuilder.append("&genreName=").append(filter.genre);
		if (filter.keyword != null && !filter.keyword.isEmpty()) urlBuilder.append("&keyword=").append(filter.keyword);
		if (filter.startDateTime != null) urlBuilder.append("&startDateTime=").append(filter.startDateTime);
		if (filter.endDateTime != null) urlBuilder.append("&endDateTime=").append(filter.endDateTime);

		return fetchJsonFromUrl(urlBuilder.toString());
	}

	/**
	 * Método auxiliar común para ambos fetchEvents
	 */
	private static JSONObject fetchJsonFromUrl(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		int responseCode = conn.getResponseCode();
		if (responseCode != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + responseCode);
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				response.append(line);
			}
			return new JSONObject(response.toString());
		}
	}
}
