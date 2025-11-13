package repository;

import database.ConnectionFactory;
import model.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrackRepository {

    public Track create(Track track) {
        String sql = "INSERT INTO tracks (id, title, duration, genres, albums) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, track.getId());
            stmt.setString(2, track.getTitle());
            stmt.setString(3, Integer.toString(track.getDuration()));
            stmt.setString(4, track.getGenres());
            stmt.setString(5, track.getAlbums());
            stmt.executeUpdate();
            return track;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Track> getById(String id) {
        String sql = "SELECT * FROM Tracks WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Track(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getString("genres"),
                        rs.getString("albums")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Track> getAll() {
        List<Track> tracks = new ArrayList<>();
        String sql = "SELECT * FROM Tracks";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tracks.add(new Track(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getString("genres"),
                        rs.getString("albums")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tracks;
    }

    public boolean update(Track track) {
        String sql = "UPDATE Tracks SET title = ?, duration = ?, genres = ?, albums = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, track.getTitle());
            stmt.setString(2, Integer.toString(track.getDuration()));
            stmt.setString(3, track.getGenres());
            stmt.setString(4, track.getAlbums());
            stmt.setString(5, track.getId());

            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Tracks WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
