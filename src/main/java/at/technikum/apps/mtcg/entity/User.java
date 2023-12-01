package at.technikum.apps.mtcg.entity;

import java.util.UUID;

public class User {

    private String id;

    public String username;

    public String password;

    public void setId(String id) { this.id = id; }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
