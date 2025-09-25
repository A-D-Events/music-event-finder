package es.ulpgc.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import es.ulpgc.model.TicketmasterResponse;

public class TicketmasterParser {

    public static TicketmasterResponse parseTicketmasterResponse(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, TicketmasterResponse.class);
        } catch (JsonSyntaxException exception) {
            System.err.println("Error parsing Ticketmaster response: " + exception.getMessage());
            return null;
        }
    }

        public static List<TicketmasterResponse> parseTicketmasterResponses(String json) {
            List<TicketmasterResponse> results = new ArrayList<>();
            try {
                JsonObject root = JsonParser.parseString(json).getAsJsonObject();
                if (root == null || !root.has("_embedded")) return results;
                JsonObject embedded = root.getAsJsonObject("_embedded");
                if (embedded == null || !embedded.has("events")) return results;

                JsonArray events = embedded.getAsJsonArray("events");
                if (events == null || events.size() == 0) return results;

                for (JsonElement event : events) {
                    TicketmasterResponse response = parseTicketmasterResponse(event.toString());
                    if (response != null) results.add(response);
                }
            } catch (JsonSyntaxException exception) {
                System.err.println("Error parsing Ticketmaster responses: " + exception.getMessage());
            }
            return results;
        }
}
