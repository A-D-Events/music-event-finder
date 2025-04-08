package es.ulpgc.spotify;

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
        String parameters = "grant_type=refresh_token&refresh_token=" + ConfigLoader.get("spotify.refresh.token") +
                            "&client_id=" + ConfigLoader.get("spotify.client.id") +
                            "&client_secret=" + ConfigLoader.get("spotify.client.secret");

        URL url = URI.create(ConfigLoader.get("spotify.api.token.endpoint")).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(parameters.getBytes());
        }

        if (conn.getResponseCode() == 200) {
            try (Scanner scanner = new Scanner(conn.getInputStream())) {
                String response = scanner.useDelimiter("\\A").next();
                return new Gson().fromJson(response, JsonObject.class).get("access_token").getAsString();
            }
        } else {
            throw new IOException("Error al renovar el token: " + conn.getResponseCode());
        }
    }
}
