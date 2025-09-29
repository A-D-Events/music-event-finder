package es.ulpgc.datamart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import es.ulpgc.model.ArtistEvent;
import es.ulpgc.model.TicketmasterEvent;

public class DatamartStore {

    public static void saveArtistToDatamart(ArtistEvent artist) {
        String insertArtistQuery = "INSERT OR REPLACE INTO artists (id, name, popularity, genres, followers, image_url) " +
                                    "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatamartInitializer.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertArtistQuery)) {
                preparedStatement.setString(1, artist.getId());
                preparedStatement.setString(2, artist.getName());
                preparedStatement.setInt(3, artist.getPopularity());
                preparedStatement.setString(4, String.join(",", artist.getGenres()));
                preparedStatement.setInt(5, artist.getFollowers());
                preparedStatement.setString(6, artist.getImageUrl());

                preparedStatement.addBatch();
            

            preparedStatement.executeBatch();
            System.out.println("Artist saved successfully to the database.");

        } catch (SQLException exception) {
            System.err.println("Error saving artist to the database: " + exception.getMessage());
        }
    }

    public static void saveEventToDatamart(TicketmasterEvent event) {
        String insertEventQuery = "INSERT OR REPLACE INTO events (id, name, url, date, venue, city, image) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatamartInitializer.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertEventQuery)) {
            preparedStatement.setString(1, event.getId());
            preparedStatement.setString(2, event.getName());
            preparedStatement.setString(3, event.getUrl());
            preparedStatement.setString(4, event.getDate());
            preparedStatement.setString(5, event.getVenue());
            preparedStatement.setString(6, event.getCity());
            preparedStatement.setString(7, event.getImage());

            preparedStatement.addBatch();

            preparedStatement.executeBatch();
            System.out.println("Event saved successfully to the database.");

        } catch (SQLException exception) {
            System.err.println("Error saving event to the database: " + exception.getMessage());
        }
    }
}
