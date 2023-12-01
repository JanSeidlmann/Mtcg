package at.technikum.apps.mtcg.Exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String username) {
        super("User with username " + username + " already exists");
    }
}

