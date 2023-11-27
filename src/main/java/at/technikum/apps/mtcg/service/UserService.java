package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.MemoryRepository;
import at.technikum.apps.mtcg.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final Repository repository;

    public UserService() { this.repository = new MemoryRepository(); }

    public List<User> findAllUsers() { return repository.findAllUsers(); }

    public Optional<User> findUser(int id) { return Optional.empty(); }

    public User save(User user) {
        user.setId(UUID.randomUUID().toString());
        return repository.saveUser(user);
    }
}
