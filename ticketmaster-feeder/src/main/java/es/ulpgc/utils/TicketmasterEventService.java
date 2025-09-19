package es.ulpgc.utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;

import es.ulpgc.model.TicketmasterEvent;
import es.ulpgc.model.TicketmasterResponse;

public class TicketmasterEventService {
    private final Set<String> issuedKeywords = new HashSet<>();
    private final Set<String> publishedEventKeys = new HashSet<>();

    public void processKeyword(String keyword, String sourceSystem) {
        String normalized = keyword.trim().toLowerCase();
        if (!issuedKeywords.add(normalized)) return;

    String requestParameters = TicketmasterParamsBuilder.build(keyword, 20, 0, null, null);
    System.out.println("[TM] Params for '" + keyword + "': " + requestParameters);

        TicketmasterApiClient apiClient = new TicketmasterApiClient();
        JsonObject apiResponse;
        try {
            apiResponse = apiClient.getEvents(requestParameters);
        } catch (IOException io) {
            System.err.println("API request failed for keyword '" + keyword + "': " + io.getMessage());
            return;
        }

        List<TicketmasterResponse> ticketmasterResponses = TicketmasterParser.parseTicketmasterResponses(apiResponse.toString());
        if (ticketmasterResponses == null || ticketmasterResponses.isEmpty()) {
            System.out.println("[TM] No events for keyword: " + keyword);
            return;
        }
        int publishedCount = 0;
        for (TicketmasterResponse response : ticketmasterResponses) {
            String deduplicationKey = buildDeduplicationKey(response);
            if (!publishedEventKeys.add(deduplicationKey)) continue;
            System.out.println("[TM] Parsed -> name=" + response.name + ", link=" + response.link + ", venue=" + response.venue + ", date=" + response.date + ", city=" + response.address);
            TicketmasterEvent event = TicketmasterEventCreator.fromResponse(response, sourceSystem);
            String eventJson = event.toJson();
            if (eventJson != null) {
                EventPublisher.publishEvent(eventJson);
                publishedCount++;
            }
        }
        System.out.println("[TM] Published " + publishedCount + " events for keyword: " + keyword);
    }

    private static String buildDeduplicationKey(TicketmasterResponse response) {
        String link = response.link != null ? response.link.trim().toLowerCase() : null;
        if (link != null && !link.isBlank()) return "url:" + link;
        String normalizedName = response.name != null ? response.name.trim().toLowerCase() : "";
        String normalizedDate = response.date != null ? response.date.trim().toLowerCase() : "";
        String normalizedVenue = response.venue != null ? response.venue.trim().toLowerCase() : "";
        return String.join("|", "name-date-venue", normalizedName, normalizedDate, normalizedVenue);
    }
}
