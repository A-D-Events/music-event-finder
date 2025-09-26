package es.ulpgc.model;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TicketmasterEvent {
    private LocalDateTime ts;
    private String ss;
    private String id;
    private String name;
    private String url;
    private String date;
    private String venue;
    private String city;

    public TicketmasterEvent(LocalDateTime ts, String ss, String id, String name, String url, String date,
                             String venue, String city) {
        this.ts = ts;
        this.ss = ss;
        this.id = id;
        this.name = name;
        this.url = url;
        this.date = date;
        this.venue = venue;
        this.city = city;
    }

    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(this);
        } catch (IOException e) {
            System.err.println("Error converting the event to JSON: " + e.getMessage());
            return null;
        }
    }

    public static TicketmasterEvent deserialize(String eventJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(eventJson, TicketmasterEvent.class);
        } catch (IOException e) {
            System.err.println("Error deserializing the JSON event: " + e.getMessage());
            return null;
        }
    }

    public LocalDateTime getTs() { return ts; }
    public void setTs(LocalDateTime ts) { this.ts = ts; }

    public String getSs() { return ss; }
    public void setSs(String ss) { this.ss = ss; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}