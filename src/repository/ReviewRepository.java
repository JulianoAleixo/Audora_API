package repository;

import database.ConnectionFactory;
import model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewRepository {

    public Review create(Review review) {
        String sql = "INSERT INTO reviews (id, rating, comment, date, users) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, review.getId());
            stmt.setString(2, Integer.toString(review.getRating()));
            stmt.setString(3, review.getComment());
            stmt.setString(4, review.getDate().toString());
            stmt.setString(5, review.getUsers());
            stmt.executeUpdate();
            return review;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Review> getById(String id) {
        String sql = "SELECT * FROM Reviews WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Review(
                        rs.getString("id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("date"),
                        rs.getString("users")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Review> getAll() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getString("id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("date"),
                        rs.getString("users")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public boolean update(Review review) {
        String sql = "UPDATE Reviews SET rating = ?, comment = ?, date = ?, users = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, Integer.toString(review.getRating()));
            stmt.setString(2, review.getComment());
            stmt.setString(3, review.getDate().toString());
            stmt.setString(4, review.getUsers());
            stmt.setString(5, review.getId());

            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Reviews WHERE id = ?";
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
