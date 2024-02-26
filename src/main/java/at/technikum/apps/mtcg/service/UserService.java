package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.Exception.DuplicateUserException;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.repository.Repository;
import at.technikum.server.http.Request;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final Repository repository;

    public UserService() {
        this.repository = new UserRepository();
    }
    public UserService(Repository repository) {this.repository = repository;}

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
        repository.setUpStats(user);
        user.setId(UUID.randomUUID().toString());
        return repository.saveUser(user);
    }
}
