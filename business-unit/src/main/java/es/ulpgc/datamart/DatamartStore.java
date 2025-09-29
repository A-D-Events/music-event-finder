package es.ulpgc.datamart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import es.ulpgc.model.Artist;

public class DatamartStore {

    public static void saveArtistToDatamart(Artist artist) {
        String insertArtistQuery = "INSERT OR REPLACE INTO artists (id, name, popularity, type, uri, href, genres, followers, image_url) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatamartInitializer.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertArtistQuery)) {
                preparedStatement.setString(1, artist.getId());
                preparedStatement.setString(2, artist.getName());
                preparedStatement.setInt(3, artist.getPopularity());
                preparedStatement.setString(4, artist.getType());
                preparedStatement.setString(5, artist.getUri());
                preparedStatement.setString(6, artist.getHref());
                preparedStatement.setString(7, String.join(",", artist.getGenres()));
                preparedStatement.setInt(8, artist.getFollowers().getTotal());
                preparedStatement.setString(9, artist.getImages().isEmpty() ? null : artist.getImages().get(0).getUrl());

                preparedStatement.addBatch();
            

            preparedStatement.executeBatch();
            System.out.println("Artist saved successfully to the database.");

        } catch (SQLException exception) {
            System.err.println("Error saving artist to the database: " + exception.getMessage());
        }
    }
}
