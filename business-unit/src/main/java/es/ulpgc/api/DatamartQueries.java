package es.ulpgc.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ulpgc.datamart.DatamartInitializer;

public class DatamartQueries {

    public static List<Map<String,Object>> topArtists(int limit) {
        String sql = "SELECT id, name, popularity, followers, genres, image_url FROM artists ORDER BY popularity DESC LIMIT ?";
        return queryList(sql, preparedStatement -> preparedStatement.setInt(1, limit));
    }

    public static List<Map<String,Object>> topEvents(int limit) {
        String sql = "SELECT id, name, date, venue, city, url, image FROM events ORDER BY date ASC LIMIT ?";
        return queryList(sql, preparedStatement -> preparedStatement.setInt(1, limit));
    }

    public static Map<String,Object> getArtist(String id) {
        String sql = "SELECT id, name, popularity, followers, genres, image_url FROM artists WHERE id=?";
        return queryOne(sql, preparedStatement -> preparedStatement.setString(1, id));
    }

    public static Map<String,Object> getEvent(String id) {
        String sql = "SELECT id, name, date, venue, city, url, image FROM events WHERE id=?";
        return queryOne(sql, preparedStatement -> preparedStatement.setString(1, id));
    }

    public static List<Map<String,Object>> searchArtists(String q, int limit) {
        String sql = "SELECT id, name, popularity, followers, genres, image_url FROM artists WHERE LOWER(name) LIKE ? ORDER BY popularity DESC LIMIT ?";
        return queryList(sql, preparedStatement -> {
            preparedStatement.setString(1, "%" + q.toLowerCase() + "%");
            preparedStatement.setInt(2, limit);
        });
    }

    public static List<Map<String,Object>> searchEvents(String q, int limit) {
        String sql = "SELECT id, name, date, venue, city, url, image FROM events WHERE LOWER(name) LIKE ? ORDER BY date ASC LIMIT ?";
        return queryList(sql, preparedStatement -> {
            preparedStatement.setString(1, "%" + q.toLowerCase() + "%");
            preparedStatement.setInt(2, limit);
        });
    }

    public static Map<String,Object> stats() {
        String sqlArtists = "SELECT COUNT(*) AS c FROM artists";
        String sqlEvents = "SELECT COUNT(*) AS c FROM events";
        Map<String,Object> map = new HashMap<>();
        try (Connection connection = DatamartInitializer.getConnection(); Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sqlArtists)) { if (resultSet.next()) map.put("artists", resultSet.getInt("c")); }
            try (ResultSet resultSet = statement.executeQuery(sqlEvents)) { if (resultSet.next()) map.put("events", resultSet.getInt("c")); }
        } catch (SQLException exception) { map.put("error", exception.getMessage()); }
        return map;
    }

    private interface PrepareStatementApplier { void apply(PreparedStatement preparedStatement) throws SQLException; }

    private static List<Map<String,Object>> queryList(String sql, PrepareStatementApplier prepareStatementApplier) {
        List<Map<String,Object>> list = new ArrayList<>();
        try (Connection connection = DatamartInitializer.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            prepareStatementApplier.apply(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columns = metaData.getColumnCount();
                while (resultSet.next()) {
                    Map<String,Object> row = new HashMap<>();
                    for (int i=1;i<=columns;i++) row.put(metaData.getColumnLabel(i), resultSet.getObject(i));
                    list.add(row);
                }
            }
        } catch (SQLException exception) {
            Map<String,Object> error = new HashMap<>();
            error.put("error", exception.getMessage());
            list.add(error);
        }
        return list;
    }

    private static Map<String,Object> queryOne(String sql, PrepareStatementApplier prepareStatementApplier) {
        List<Map<String,Object>> list = queryList(sql, prepareStatementApplier);
        return list.isEmpty() ? null : list.get(0);
    }
}
