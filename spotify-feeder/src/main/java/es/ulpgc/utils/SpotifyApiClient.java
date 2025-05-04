package es.ulpgc.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import es.ulpgc.config.ConfigLoader;

public class SpotifyApiClient {
    private static final String BASE_URL = ConfigLoader.get("spotify.api.url");
    private static final String ENDPOINT = ConfigLoader.get("spotify.api.endpoint");
    private static final String PARAMETERS = ConfigLoader.get("spotify.api.parameters");

    private final String accessToken;
    private final Gson gson;

    public SpotifyApiClient() {
        this.accessToken = ConfigLoader.get("spotify.access.token");
        if (this.accessToken == null || this.accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token is a null or is empty.");
        }
        this.gson = new Gson();
    }

    public JsonObject getTopArtists() throws IOException {
        String endpoint = BASE_URL + ENDPOINT + PARAMETERS;
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
