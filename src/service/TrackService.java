package service;

import model.Track;
import repository.TrackRepository;

import java.util.List;
import java.util.Optional;

public class TrackService {
    private final TrackRepository repository = new TrackRepository();

    public List<Track> getList() {
        return repository.getAll();
    }

    public Track create(Track track) {
        return repository.create(track);
    }

    public Optional<Track> get(String id) {
        return repository.getById(id);
    }

    public boolean update(Track track) {
        return repository.update(track);
    }

    public boolean delete(String id) {
        return repository.delete(id);
    }
}
