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

    String requestParameters = TicketmasterParamsBuilder.build(keyword);
    System.out.println("[TicketMaster] Parameters for '" + keyword + "': " + requestParameters);

        TicketmasterApiClient apiClient = new TicketmasterApiClient();
        JsonObject apiResponse;
        try {
            apiResponse = apiClient.getEvents(requestParameters);
        } catch (IOException ioexception) {
            System.err.println("API request failed for keyword '" + keyword + "': " + ioexception.getMessage());
            return;
        }

        List<TicketmasterResponse> ticketmasterResponses = TicketmasterParser.parseTicketmasterResponses(apiResponse.toString());
        if (ticketmasterResponses == null || ticketmasterResponses.isEmpty()) {
            System.out.println("[TicketMaster] No events for keyword: " + keyword);
            return;
        }
        int publishedCount = 0;
        for (TicketmasterResponse response : ticketmasterResponses) {
            String deduplicationKey = buildDeduplicationKey(response);
            if (!publishedEventKeys.add(deduplicationKey)) continue;
            System.out.println("[TicketMaster] Parsed -> id=" + response.id + ", name=" + response.name + ", url=" + response.url + ", venue=" + response.venue + ", date=" + response.date + ", city=" + response.address);
            TicketmasterEvent event = TicketmasterEventCreator.fromResponse(response, sourceSystem);
            String eventJson = event.toJson();
            if (eventJson != null) {
                EventPublisher.publishEvent(eventJson);
                publishedCount++;
            }
        }
        System.out.println("[TicketMaster] Published " + publishedCount + " events for keyword: " + keyword);
    }

    private static String buildDeduplicationKey(TicketmasterResponse response) {

        if (response.id != null && !response.id.isBlank()) {
            return "id:" + response.id.trim().toLowerCase();
        }
        
        if (response.url != null && !response.url.isBlank()) {
            return "url:" + response.url.trim().toLowerCase();
        }

        String name = response.name != null ? response.name.trim().toLowerCase() : "";
        String date = response.date != null ? response.date.trim().toLowerCase() : "";
        String venue = response.venue != null ? response.venue.trim().toLowerCase() : "";

        return String.format("name-date-venue|%s|%s|%s", name, date, venue);
    }
}
