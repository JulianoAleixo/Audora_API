package repository;

import database.ConnectionFactory;
import model.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtistRepository {

    public Artist create(Artist artist) {
        String sql = "INSERT INTO artists (id, name, country, main_genre) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artist.getId());
            stmt.setString(2, artist.getName());
            stmt.setString(3, artist.getCountry());
            stmt.setString(4, artist.getMainGenre());
            stmt.executeUpdate();
            return artist;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Artist> getById(String id) {
        String sql = "SELECT * FROM Artists WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Artist(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("main_genre")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Artist> getAll() {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM Artists";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                artists.add(new Artist(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("main_genre")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artists;
    }

    public boolean update(Artist artist) {
        String sql = "UPDATE Artists SET name = ?, country = ?, main_genre = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getCountry());
            stmt.setString(3, artist.getMainGenre());
            stmt.setString(4, artist.getId());

            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Artists WHERE id = ?";
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
