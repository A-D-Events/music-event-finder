package es.ulpgc.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import es.ulpgc.config.ConfigLoader;

public class TicketmasterApiClient {
    private final String accessToken;
    private final Gson gson;

    public TicketmasterApiClient() {
        this.accessToken = ConfigLoader.getProperty("ticketmaster.api.key");
        if (this.accessToken == null || this.accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token is a null or is empty.");
        }
        this.gson = new Gson();
    }

    public JsonObject getEvents(String params) throws IOException {
        String baseUrl = ConfigLoader.getProperty("ticketmaster.api.url");
        String endpointPath = ConfigLoader.getProperty("ticketmaster.api.endpoint");
        String endpoint = baseUrl + endpointPath + params;
        return sendGetRequest(endpoint);
    }

    private JsonObject sendGetRequest(String endpoint) throws IOException {
        URL url = URI.create(endpoint).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        if (connection.getResponseCode() == 200) {
            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                return gson.fromJson(reader, JsonObject.class);
            }
        } else {
            throw new IOException("Request error: " + connection.getResponseCode());
        }
    }
}
