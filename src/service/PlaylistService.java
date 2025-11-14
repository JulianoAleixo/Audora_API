package service;

import model.Playlist;
import repository.PlaylistRepository;

import java.util.List;
import java.util.Optional;

public class PlaylistService {
    private final PlaylistRepository repository = new PlaylistRepository();

    public List<Playlist> getList() {
        return repository.getAll();
    }

    public Playlist create(Playlist playlist) {
        return repository.create(playlist);
    }

    public Optional<Playlist> get(String id) {
        return repository.getById(id);
    }

    public boolean update(Playlist playlist) {
        return repository.update(playlist);
    }

    public boolean delete(String id) {
        return repository.delete(id);
    }
}
