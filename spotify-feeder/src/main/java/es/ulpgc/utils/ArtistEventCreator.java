package es.ulpgc.utils;

import java.time.LocalDateTime;

import es.ulpgc.model.Artist;
import es.ulpgc.model.ArtistEvent;

public class ArtistEventCreator {
    public static ArtistEvent fromArtist(Artist artist, String sourceSystem) {
        String imageUrl = artist.getImages() != null && !artist.getImages().isEmpty()
                ? artist.getImages().get(0).getUrl()
                : null;

        return new ArtistEvent(
                LocalDateTime.now(),
                sourceSystem,
                artist.getId(),
                artist.getName(),
                artist.getPopularity(),
                artist.getGenres(),
                artist.getFollowers() != null ? artist.getFollowers().getTotal() : 0,
                imageUrl
        );
    }
}
