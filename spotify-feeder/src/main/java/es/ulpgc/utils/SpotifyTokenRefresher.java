package es.ulpgc.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import es.ulpgc.config.ConfigLoader;

public class SpotifyTokenRefresher {
    public static String refreshAccessToken() throws IOException {
        String parameters = "grant_type=refresh_token&refresh_token=" + ConfigLoader.getProperty("spotify.refresh.token") +
                            "&client_id=" + ConfigLoader.getProperty("spotify.client.id") +
                            "&client_secret=" + ConfigLoader.getProperty("spotify.client.secret");

        URL url = URI.create(ConfigLoader.getProperty("spotify.api.token.endpoint")).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(parameters.getBytes());
        }

        if (connection.getResponseCode() == 200) {
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                String response = scanner.useDelimiter("\\A").next();
                return new Gson().fromJson(response, JsonObject.class).get("access_token").getAsString();
            }
        } else {
            throw new IOException("Error refreshing token: " + connection.getResponseCode());
        }
    }
}
