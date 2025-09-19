package es.ulpgc.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import es.ulpgc.model.SpotifyResponse;

public class SpotifyParser {

    public static SpotifyResponse parseSpotifyResponse(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, SpotifyResponse.class);
        } catch (JsonSyntaxException exception) {
            System.err.println("Error parsing Spotify response: " + exception.getMessage());
            return null;
        }
    }
}
