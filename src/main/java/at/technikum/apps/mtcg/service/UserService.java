package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.Exception.DuplicateUserException;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseRepository;
import at.technikum.apps.mtcg.repository.Repository;
import at.technikum.server.http.Request;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final Repository repository;
    private final PackageService packageService;

    public UserService() {
        this.repository = new DatabaseRepository();
        this.packageService = new PackageService();
    }

    public Optional<User> findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    public Optional<User> updateUserByUsername(String username, Request request) {
        return repository.updateUserByUsername(username, request);
    }

    public Optional<User> login(String username, String password) {
        Optional<User> userOptional = findUserByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Check if the provided password matches the stored password
            if (password.equals(user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }


    public User save(User user) {

        if (findUserByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUserException(user.getUsername());
        }

        user.setId(UUID.randomUUID().toString());
        return repository.saveUser(user);
    }
}
