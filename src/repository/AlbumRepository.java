package repository;

import database.ConnectionFactory;
import model.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlbumRepository {

    public Album create(Album album) {
        String sql = "INSERT INTO albums (id, title, release_year, cover_url, artists, genres) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, album.getId());
            stmt.setString(2, album.getTitle());
            stmt.setString(3, Integer.toString(album.getReleaseYear()));
            stmt.setString(4, album.getCoverUrl());
            stmt.setString(5, album.getArtists());
            stmt.setString(6, album.getGenres());
            stmt.executeUpdate();
            return album;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Album> getById(String id) {
        String sql = "SELECT * FROM Albums WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Album(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getInt("release_year"),
                        rs.getString("cover_url"),
                        rs.getString("artists"),
                        rs.getString("genres")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Album> getAll() {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM Albums";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                albums.add(new Album(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getInt("release_year"),
                        rs.getString("cover_url"),
                        rs.getString("artists"),
                        rs.getString("genres")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return albums;
    }

    public boolean update(Album album) {
        String sql = "UPDATE Albums SET title = ?, release_year = ?, cover_url = ?, artists = ?, genres = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, album.getTitle());
            stmt.setString(2, Integer.toString(album.getReleaseYear()));
            stmt.setString(3, album.getCoverUrl());
            stmt.setString(4, album.getArtists());
            stmt.setString(5, album.getGenres());
            stmt.setString(6, album.getId());

            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Albums WHERE id = ?";
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
