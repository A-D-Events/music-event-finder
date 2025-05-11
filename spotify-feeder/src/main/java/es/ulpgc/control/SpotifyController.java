package es.ulpgc.control;

import java.io.IOException;

import com.google.gson.JsonObject;

import es.ulpgc.config.ConfigLoader;
import es.ulpgc.database.DatabaseManager;
import es.ulpgc.model.SpotifyResponse;
import es.ulpgc.utils.SpotifyApiClient;
import es.ulpgc.utils.SpotifyParser;
import es.ulpgc.utils.SpotifyStore;
import es.ulpgc.utils.SpotifyTokenRefresher;

public class SpotifyController {

    public static void initialize(){
        System.out.println("Checking if database is initialized...");
        if (!DatabaseManager.isDatabaseInitialized()){
            DatabaseManager.initializeDatabase();
            System.out.println("Database initialized successfully.");
        } else {
           System.out.println("Database already initialized.");
        }
    }
    
    public static void addToken(String token){
        ConfigLoader.setProperty("spotify.refresh.token", token);
    }

    public static JsonObject apiRequest() throws IOException{
        try {
            String token = ConfigLoader.getProperty("spotify.refresh.token");
            if (token == null || token.isEmpty()) {
                throw new IllegalArgumentException("Access token is null or empty.");
            }
            try {
                String accessToken = SpotifyTokenRefresher.refreshAccessToken();
                ConfigLoader.setProperty("spotify.access.token", accessToken);
                System.out.println("Access token refreshed successfully.");
            } catch (IOException exception) {
                System.err.println("Error refreshing access token: " + exception.getMessage());
                return null;
            }
            try {
                SpotifyApiClient apiClient = new SpotifyApiClient();
                JsonObject topArtists = apiClient.getTopArtists();
                if (topArtists == null) {
                    throw new IOException("API request returned null.");
                }
            System.out.println("Top artists retrieved");
            return topArtists;
            } catch (IOException exception) {
                System.err.println("Error making API request: " + exception.getMessage());
                return null;
            }

        } catch (IllegalArgumentException exception) {
            System.err.println("Error making API request: " + exception.getMessage());
            return null;
        }
    }

    public static SpotifyResponse apiParser() {
        try {
            JsonObject apiResponse = apiRequest();
            if (apiResponse == null) {
                System.err.println("API request returned null. Cannot parse response.");
                return null;
            }

            String json = apiResponse.toString();
            SpotifyResponse spotifyResponse = SpotifyParser.parseSpotifyResponse(json);
            if (spotifyResponse != null) {
                System.out.println("Spotify response parsed successfully.");
                return spotifyResponse;
            } else {
                System.err.println("Error parsing Spotify response.");
                return null;
            }
        } catch (IOException exception) {
            System.err.println("Error parsing Spotify response: " + exception.getMessage());
            return null;
        }
    }

    public static void updateApi(){
        try {
            SpotifyResponse spotifyResponse = apiParser();
            if (spotifyResponse != null) {
                SpotifyStore.saveArtistsToDatabase(spotifyResponse.getArtists());
                System.out.println("Artists stored in the database successfully.");
            } else {
                System.err.println("Error storing artists in the database.");
            }
        } catch (Exception exception) {
            System.err.println("Error storing data: " + exception.getMessage());
        }
    }
}