package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.Exception.DuplicateUserException;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseRepository;
import at.technikum.apps.mtcg.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final Repository repository;
    private final List<Card> cardList = null;
    private final List<User> userList = null;
    public UserService() { this.repository = new DatabaseRepository(cardList, userList); }

    public Optional<User> findUser(int id) { return Optional.empty(); }

    public Optional<User> findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    public Optional<User> updateUserByUsername(String username, User updatedUser) {
        Optional<User> existingUserOptional = findUserByUsername(username);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // Aktualisiere nur, wenn das Passwort nicht null oder leer ist
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }

            // Aktualisiere nur, wenn der Benutzername nicht null oder leer ist
            if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
                existingUser.setUsername(updatedUser.getUsername());
            }

            save(existingUser); // Die save-Methode wird in jedem Fall aufgerufen, um die Aktualisierung zu speichern

            return Optional.of(existingUser);
        } else {
            return Optional.empty();
        }
    }

    public User save(User user) {

        if (findUserByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUserException(user.getUsername());
        }

        user.setId(UUID.randomUUID().toString());
        return repository.saveUser(user);
    }
}
