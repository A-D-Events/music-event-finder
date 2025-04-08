package es.ulpgc;

import java.io.IOException;

import es.ulpgc.config.ConfigLoader;
import es.ulpgc.control.*;

import es.ulpgc.database.DatabaseManager;


public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Initializing application...");
        System.out.println("Checking for an existing database...");
        if (!DatabaseManager.isDatabaseInitialized()) {
            System.out.println("No existing database found. Initializing a new one...");
            DatabaseManager.initializeDatabase();
        } else {
            System.out.println("Existing database found. No need to initialize.");
        }
        System.out.println("Application initialized successfully.");
        System.out.println("Please, introduce your Spotify Refresh Token:");
        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            String refreshToken = scanner.nextLine();
            if (refreshToken == null || refreshToken.isEmpty()) {
                System.out.println("The refresh token cannot be null or empty.");
                return;
            }
            System.out.println("Your Spotify Refresh Token is: " + refreshToken);
            System.out.println("Saving the token to the configuration file...");
            ConfigLoader.setProperty("spotify.refresh.token", refreshToken);
        }
        System.out.println("Refreshing Spotify access token...");
        try {
            String accessToken = SpotifyTokenRefresher.refreshAccessToken();
            System.out.println("Spotify Access Token: " + accessToken);
            ConfigLoader.setProperty("spotify.access.token", accessToken);
            System.out.println("Access token saved to configuration file.");
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error refreshing Spotify access token: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
        System.out.println("Fetching Spotify user data...");
        SpotifyApiClient spotifyApiClient = new SpotifyApiClient();
        String TopArtists = spotifyApiClient.getTopArtists().toString();
        System.out.println("Spotify user data fetched successfully: ");
        System.out.println("Parsing Spotify response and saving Spotify data to the database...");
        SpotifyStore.saveArtistsToDatabase(SpotifyParser.parseSpotifyResponse(TopArtists).getArtists());
        System.out.println("Application finished.");
    }
}