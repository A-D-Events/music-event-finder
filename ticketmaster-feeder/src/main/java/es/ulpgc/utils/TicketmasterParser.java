package es.ulpgc.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import es.ulpgc.model.TicketmasterResponse;

public class TicketmasterParser {

    public static TicketmasterResponse parseTicketmasterResponse(String json) {
    try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();

            if (root == null || !root.has("_embedded")) return null;
            JsonObject embedded = root.getAsJsonObject("_embedded");
            if (embedded == null || !embedded.has("events")) return null;

            JsonArray events = embedded.getAsJsonArray("events");
            if (events == null || events.size() == 0) return null;

            // Find the first event that has at least a name or url
            JsonObject event = null;
            for (JsonElement el : events) {
                JsonObject candidate = el.getAsJsonObject();
                String candidateName = getAsString(candidate, "name");
                String candidateUrl = getAsString(candidate, "url");
                if ((candidateName != null && !candidateName.isBlank()) || (candidateUrl != null && !candidateUrl.isBlank())) {
                    event = candidate;
                    break;
                }
            }
            if (event == null) return null;

            String name = getAsString(event, "name");
            String url = getAsString(event, "url");

            // Dates
            String date = null;
            String time = null;
            if (event.has("dates")) {
                JsonObject dates = event.getAsJsonObject("dates");
                if (dates.has("start")) {
                    JsonObject start = dates.getAsJsonObject("start");
                    date = getAsString(start, "localDate");
                    time = getAsString(start, "localTime");
                }
            }

            // Venue and city
            String venue = null;
            String city = null;
            if (event.has("_embedded")) {
                JsonObject evEmbedded = event.getAsJsonObject("_embedded");
                if (evEmbedded.has("venues")) {
                    JsonArray venues = evEmbedded.getAsJsonArray("venues");
                    if (venues.size() > 0) {
                        JsonObject v = venues.get(0).getAsJsonObject();
                        venue = getAsString(v, "name");
                        if (v.has("city")) {
                            city = getAsString(v.getAsJsonObject("city"), "name");
                        }
                        if (city == null && v.has("address")) {
                            city = getAsString(v.getAsJsonObject("address"), "line1");
                        }
                    }
                }
            }

            // Artist (attraction) name
            String artist = null;
            if (event.has("_embedded")) {
                JsonObject evEmbedded = event.getAsJsonObject("_embedded");
                if (evEmbedded.has("attractions")) {
                    JsonArray attractions = evEmbedded.getAsJsonArray("attractions");
                    if (attractions.size() > 0) {
                        artist = getAsString(attractions.get(0).getAsJsonObject(), "name");
                    }
                }
            }
            if (artist == null) artist = name; // fallback

            // Classifications
            String segment = null;
            String genre = null;
            if (event.has("classifications")) {
                JsonArray classifications = event.getAsJsonArray("classifications");
                if (classifications.size() > 0) {
                    JsonObject c = classifications.get(0).getAsJsonObject();
                    if (c.has("segment")) segment = getAsString(c.getAsJsonObject("segment"), "name");
                    if (c.has("genre")) genre = getAsString(c.getAsJsonObject("genre"), "name");
                }
            }

            // Price
            String price = null;
            if (event.has("priceRanges")) {
                JsonArray ranges = event.getAsJsonArray("priceRanges");
                if (ranges.size() > 0) {
                    JsonObject pr = ranges.get(0).getAsJsonObject();
                    String min = getAsString(pr, "min");
                    String max = getAsString(pr, "max");
                    String currency = getAsString(pr, "currency");
                    StringBuilder sb = new StringBuilder();
                    if (min != null) sb.append(min);
                    if (max != null) sb.append("-").append(max);
                    if (currency != null) sb.append(" ").append(currency);
                    price = sb.length() > 0 ? sb.toString() : null;
                }
            }

            // Image
            String image = null;
            if (event.has("images")) {
                JsonArray images = event.getAsJsonArray("images");
                if (images.size() > 0) {
                    image = getAsString(images.get(0).getAsJsonObject(), "url");
                }
            }

            TicketmasterResponse result = new TicketmasterResponse(
                    name,
                    artist,
                    date,
                    time,
                    venue,
                    city,
                    price,
                    segment,
                    genre,
                    url,
                    image
            );
            // Consider invalid if both name and url are null
            if ((result.name == null || result.name.isBlank()) && (result.link == null || result.link.isBlank())) {
                return null;
            }
            return result;
        } catch (JsonSyntaxException exception) {
            System.err.println("Error parsing Ticketmaster response: " + exception.getMessage());
            return null;
        }
    }

        public static List<TicketmasterResponse> parseTicketmasterResponses(String json) {
            List<TicketmasterResponse> results = new ArrayList<>();
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            if (root == null || !root.has("_embedded")) return results;
            JsonObject embedded = root.getAsJsonObject("_embedded");
            if (embedded == null || !embedded.has("events")) return results;

            JsonArray events = embedded.getAsJsonArray("events");
            if (events == null || events.size() == 0) return results;

            for (JsonElement el : events) {
                JsonObject event = el.getAsJsonObject();
                String name = getAsString(event, "name");
                String url = getAsString(event, "url");

                String date = null;
                String time = null;
                if (event.has("dates")) {
                    JsonObject dates = event.getAsJsonObject("dates");
                    if (dates.has("start")) {
                        JsonObject start = dates.getAsJsonObject("start");
                        date = getAsString(start, "localDate");
                        time = getAsString(start, "localTime");
                    }
                }

                String venue = null;
                String city = null;
                if (event.has("_embedded")) {
                    JsonObject evEmbedded = event.getAsJsonObject("_embedded");
                    if (evEmbedded.has("venues")) {
                        JsonArray venues = evEmbedded.getAsJsonArray("venues");
                        if (venues.size() > 0) {
                            JsonObject v = venues.get(0).getAsJsonObject();
                            venue = getAsString(v, "name");
                            if (v.has("city")) {
                                city = getAsString(v.getAsJsonObject("city"), "name");
                            }
                            if (city == null && v.has("address")) {
                                city = getAsString(v.getAsJsonObject("address"), "line1");
                            }
                        }
                    }
                }

                String artist = null;
                if (event.has("_embedded")) {
                    JsonObject evEmbedded = event.getAsJsonObject("_embedded");
                    if (evEmbedded.has("attractions")) {
                        JsonArray attractions = evEmbedded.getAsJsonArray("attractions");
                        if (attractions.size() > 0) {
                            artist = getAsString(attractions.get(0).getAsJsonObject(), "name");
                        }
                    }
                }
                if (artist == null) artist = name;

                String segment = null;
                String genre = null;
                if (event.has("classifications")) {
                    JsonArray classifications = event.getAsJsonArray("classifications");
                    if (classifications.size() > 0) {
                        JsonObject c = classifications.get(0).getAsJsonObject();
                        if (c.has("segment")) segment = getAsString(c.getAsJsonObject("segment"), "name");
                        if (c.has("genre")) genre = getAsString(c.getAsJsonObject("genre"), "name");
                    }
                }

                String price = null;
                if (event.has("priceRanges")) {
                    JsonArray ranges = event.getAsJsonArray("priceRanges");
                    if (ranges.size() > 0) {
                        JsonObject pr = ranges.get(0).getAsJsonObject();
                        String min = getAsString(pr, "min");
                        String max = getAsString(pr, "max");
                        String currency = getAsString(pr, "currency");
                        StringBuilder sb = new StringBuilder();
                        if (min != null) sb.append(min);
                        if (max != null) sb.append("-").append(max);
                        if (currency != null) sb.append(" ").append(currency);
                        price = sb.length() > 0 ? sb.toString() : null;
                    }
                }

                String image = null;
                if (event.has("images")) {
                    JsonArray images = event.getAsJsonArray("images");
                    if (images.size() > 0) {
                        image = getAsString(images.get(0).getAsJsonObject(), "url");
                    }
                }

                TicketmasterResponse r = new TicketmasterResponse(
                        name, artist, date, time, venue, city, price, segment, genre, url, image
                );
                if (!((r.name == null || r.name.isBlank()) && (r.link == null || r.link.isBlank()))) {
                    results.add(r);
                }
            }
            return results;
        }

    private static String getAsString(JsonObject obj, String member) {
        if (obj == null || member == null) return null;
        JsonElement el = obj.get(member);
        return el != null && !el.isJsonNull() ? el.getAsString() : null;
    }

    // Removed emptyResponse; returning null when no valid events
}
