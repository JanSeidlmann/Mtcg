package at.technikum.apps.mtcg.entity;

import java.util.UUID;

public class User {

    private String id;

    public String Username;

    public String Password;

    public int coins;

    public void setId(String id) { this.id = id; }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
