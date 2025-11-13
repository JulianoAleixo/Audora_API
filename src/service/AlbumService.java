package service;

import model.Album;
import repository.AlbumRepository;

import java.util.List;
import java.util.Optional;

public class AlbumService {
    private final AlbumRepository repository = new AlbumRepository();

    public List<Album> getList() {
        return repository.getAll();
    }

    public Album create(Album album) {
        return repository.create(album);
    }

    public Optional<Album> get(String id) {
        return repository.getById(id);
    }

    public boolean update(Album album) {
        return repository.update(album);
    }

    public boolean delete(String id) {
        return repository.delete(id);
    }
}
