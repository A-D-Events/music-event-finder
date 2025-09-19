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
    private final Gson gson;

    public TicketmasterApiClient() {
        String accessToken = ConfigLoader.getProperty("ticketmaster.api.key");
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token is a null or is empty.");
        }
        this.gson = new Gson();
    }

    public JsonObject getEvents() throws IOException {
        String baseUrl = ConfigLoader.getProperty("ticketmaster.api.url");
        String endpointPath = ConfigLoader.getProperty("ticketmaster.api.endpoint");
        String params = ConfigLoader.getProperty("ticketmaster.api.parameters");
        String endpoint = (baseUrl != null ? baseUrl : "") + (endpointPath != null ? endpointPath : "") + (params != null ? params : "");
        return sendGetRequest(endpoint);
    }

    public JsonObject getEvents(String params) throws IOException {
        String baseUrl = ConfigLoader.getProperty("ticketmaster.api.url");
        String endpointPath = ConfigLoader.getProperty("ticketmaster.api.endpoint");
        String endpoint = (baseUrl != null ? baseUrl : "") + (endpointPath != null ? endpointPath : "") + (params != null ? params : "");
        return sendGetRequest(endpoint);
    }

    private JsonObject sendGetRequest(String endpoint) throws IOException {
    int attempts = 0;
    int maxAttempts = 4;
    long backoffMilliseconds = 500L;
        IOException last = null;
    while (attempts < maxAttempts) {
            attempts++;
            URL url = URI.create(endpoint).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    return gson.fromJson(reader, JsonObject.class);
                }
            }
            if (statusCode == 429 || (statusCode >= 500 && statusCode < 600)) {
                pause(backoffMilliseconds);
                backoffMilliseconds = Math.min(4000L, backoffMilliseconds * 2);
                continue;
            }
            last = new IOException("Request error: " + statusCode);
            break;
        }
        if (last != null) throw last;
        throw new IOException("Request failed after retries");
    }

    private static void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
