package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.Exception.DuplicateUserException;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.MemoryRepository;
import at.technikum.apps.mtcg.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final Repository repository;

    public UserService() { this.repository = new MemoryRepository(); }

    public Optional<User> findUser(int id) { return Optional.empty(); }

    public Optional<User> findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }


    public User save(User user) {

        if (findUserByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUserException(user.getUsername());
        }

        user.setId(UUID.randomUUID().toString());
        return repository.saveUser(user);
    }
}
