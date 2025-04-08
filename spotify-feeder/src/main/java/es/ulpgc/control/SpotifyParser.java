package es.ulpgc.control;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import es.ulpgc.model.SpotifyResponse;

public class SpotifyParser {

    public static SpotifyResponse parseSpotifyResponse(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, SpotifyResponse.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
