package service;

import model.Artist;
import repository.ArtistRepository;

import java.util.List;
import java.util.Optional;

public class ArtistService {
    private final ArtistRepository repository = new ArtistRepository();

    public List<Artist> getList() {
        return repository.getAll();
    }

    public Artist create(Artist artist) {
        return repository.create(artist);
    }

    public Optional<Artist> get(String id) {
        return repository.getById(id);
    }

    public boolean update(Artist artist) {
        return repository.update(artist);
    }

    public boolean delete(String id) {
        return repository.delete(id);
    }
}
