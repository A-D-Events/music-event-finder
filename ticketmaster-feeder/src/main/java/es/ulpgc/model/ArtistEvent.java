package es.ulpgc.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ArtistEvent {
    private LocalDateTime ts;
    private String ss;
    private String id;
    private String name;
    private int popularity;
    private List<String> genres;
    private int followers;
    private String imageUrl;

    public ArtistEvent() {
    }

    public ArtistEvent(LocalDateTime ts, String ss, String id, String name, int popularity, List<String> genres, int followers, String imageUrl) {
        this.ts = ts;
        this.ss = ss;
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.genres = genres;
        this.followers = followers;
        this.imageUrl = imageUrl;
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

    public static ArtistEvent deserialize(String eventJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(eventJson, ArtistEvent.class);
        } catch (IOException e) {
            System.err.println("Error deserializing the JSON event: " + e.getMessage());
            return null;
        }
    }

    public LocalDateTime getTs() {
        return ts;
    }

    public void setTs(LocalDateTime ts) {
        this.ts = ts;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
