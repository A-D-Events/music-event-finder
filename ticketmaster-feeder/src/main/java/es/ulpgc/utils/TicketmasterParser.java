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

    private static class RawEvent {
        String id;
        String name;
        String url;
        RawImages[] images;
        RawDates dates;
        RawEmbedded _embedded;
        RawClassifications[] classifications;
    }
    private static class RawImages { String url; }
    private static class RawDates { RawStart start; }
    private static class RawStart { String localDate; String dateTime; String localTime; }
    private static class RawEmbedded { RawVenue[] venues; RawAttraction[] attractions; }
    private static class RawVenue { String name; RawCity city; RawAddress address; }
    private static class RawCity { String name; }
    private static class RawAddress { String line1; }
    private static class RawAttraction { String name; }
    private static class RawClassifications { RawSegment segment; RawGenre genre; }
    private static class RawSegment { String name; }
    private static class RawGenre { String name; }

    public static TicketmasterResponse parseTicketmasterResponse(String json) {
        Gson gson = new Gson();
        try {
            RawEvent raw = gson.fromJson(json, RawEvent.class);
            if (raw == null) return null;

            String date = null;
            String time = null;
            if (raw.dates != null && raw.dates.start != null) {
                date = firstNonNull(raw.dates.start.localDate,
                        raw.dates.start.dateTime != null ? extractDate(raw.dates.start.dateTime) : null);
                time = firstNonNull(raw.dates.start.localTime,
                        raw.dates.start.dateTime != null ? extractTime(raw.dates.start.dateTime) : null);
            }

            String venue = null;
            String city = null;
            String address = null;
            if (raw._embedded != null && raw._embedded.venues != null && raw._embedded.venues.length > 0) {
                RawVenue rawVenue = raw._embedded.venues[0];
                if (rawVenue != null) {
                    venue = rawVenue.name;
                    if (rawVenue.city != null) city = rawVenue.city.name;
                    if (rawVenue.address != null) address = rawVenue.address.line1;
                }
            }

            String artist = null;
            if (raw._embedded != null && raw._embedded.attractions != null && raw._embedded.attractions.length > 0) {
                artist = raw._embedded.attractions[0] != null ? raw._embedded.attractions[0].name : null;
            }
            if (artist == null) artist = raw.name;

            String image = null;
            if (raw.images != null && raw.images.length > 0) {
                for (RawImages ri : raw.images) { if (ri != null && ri.url != null) { image = ri.url; break; } }
            }

            String segment = null;
            String genre = null;
            if (raw.classifications != null && raw.classifications.length > 0) {
                RawClassifications c = raw.classifications[0];
                if (c != null) {
                    if (c.segment != null) segment = c.segment.name;
                    if (c.genre != null) genre = c.genre.name;
                }
            }

            return new TicketmasterResponse(
                    raw.id,
                    raw.name,
                    artist,
                    date,
                    time,
                    venue,
                    city,
                    address,
                    segment,
                    genre,
                    raw.url,
                    image
            );
        } catch (JsonSyntaxException exception) {
            System.err.println("Error parsing Ticketmaster response: " + exception.getMessage());
            return null;
        }
    }

    private static String firstNonNull(String a, String b) { return a != null ? a : b; }
    private static String extractDate(String dateTime) {
        int time = dateTime.indexOf('T');
        return time > 0 ? dateTime.substring(0, time) : dateTime;
    }
    private static String extractTime(String dateTime) {
        int time = dateTime.indexOf('T');
        if (time > 0 && time + 1 < dateTime.length()) {
            String rest = dateTime.substring(time + 1);
            int zulu = rest.indexOf('Z');
            if (zulu >= 0) rest = rest.substring(0, zulu);
            int plus = rest.indexOf('+'); if (plus >= 0) rest = rest.substring(0, plus);
            return rest;
        }
        return null;
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
