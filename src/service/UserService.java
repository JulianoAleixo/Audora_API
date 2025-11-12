package service;

import model.User;
import repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository repository = new UserRepository();

    public List<User> getList() {
        return repository.getAll();
    }

    public User create(User user) {
        return repository.create(user);
    }

    public Optional<User> get(String id) {
        return repository.getById(id);
    }

    public boolean update(User user) {
        return repository.update(user);
    }

    public boolean delete(String id) {
        return repository.delete(id);
    }
}
