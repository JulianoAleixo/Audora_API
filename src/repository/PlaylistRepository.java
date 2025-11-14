package repository;

import database.ConnectionFactory;
import model.Playlist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaylistRepository {

    public Playlist create(Playlist playlist) {
        String sql = "INSERT INTO playlists (id, title, description, users) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, playlist.getId());
            stmt.setString(2, playlist.getTitle());
            stmt.setString(3, playlist.getDescription());
            stmt.setString(4, playlist.getUsers());
            stmt.executeUpdate();
            return playlist;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Playlist> getById(String id) {
        String sql = "SELECT * FROM Playlists WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Playlist(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("users")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Playlist> getAll() {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM Playlists";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                playlists.add(new Playlist(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("users")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playlists;
    }

    public boolean update(Playlist playlist) {
        String sql = "UPDATE Playlists SET title = ?, description = ?, users = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, playlist.getTitle());
            stmt.setString(2, playlist.getDescription());
            stmt.setString(3, playlist.getUsers());
            stmt.setString(4, playlist.getId());

            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Playlists WHERE id = ?";
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
