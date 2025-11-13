package service;

import model.Review;
import repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

public class ReviewService {
    private final ReviewRepository repository = new ReviewRepository();

    public List<Review> getList() {
        return repository.getAll();
    }

    public Review create(Review review) {
        return repository.create(review);
    }

    public Optional<Review> get(String id) {
        return repository.getById(id);
    }

    public boolean update(Review review) {
        return repository.update(review);
    }

    public boolean delete(String id) {
        return repository.delete(id);
    }
}
