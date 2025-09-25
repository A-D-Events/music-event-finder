package es.ulpgc.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import es.ulpgc.model.ArtistEvent;
import es.ulpgc.utils.TicketmasterEventService;

public class TicketmasterController {
    private static final String EVENTS_DIR = "eventstore/spotify-events/spotify-feeder/";

    public static void processSpotifyEvents(String sourceSystem) {
        try {
            Path eventsDirPath = Paths.get(EVENTS_DIR);
            if (!Files.isDirectory(eventsDirPath)) {
                System.out.println("No Spotify events directory found at: " + EVENTS_DIR);
                return;
            }
            List<Path> eventFiles = Files.list(eventsDirPath)
                    .filter(path -> path.toString().endsWith(".events"))
                    .collect(Collectors.toList());
            if (eventFiles.isEmpty()) {
                System.out.println("No Spotify .events files found under: " + EVENTS_DIR);
                return;
            }

            TicketmasterEventService service = new TicketmasterEventService();

            for (Path file : eventFiles) {
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        ArtistEvent artistEvent = ArtistEvent.deserialize(line);
                        if (artistEvent == null) continue;

                        String artistName = artistEvent.getName();
                        if (artistName != null && !artistName.isBlank()) {
                            service.processKeyword(artistName, sourceSystem);
                        }

                        List<String> genres = artistEvent.getGenres();
                        if (genres != null) {
                            for (String genre : genres) {
                                if (genre != null && !genre.isBlank()) {
                                    service.processKeyword(genre, sourceSystem);
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Spotify events processing completed.");
        } catch (IOException exception) {
            System.err.println("Error processing event files: " + exception.getMessage());
        }
    }
}
