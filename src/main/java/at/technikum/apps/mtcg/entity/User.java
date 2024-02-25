package at.technikum.apps.mtcg.entity;

import java.util.Objects;
import java.util.UUID;

public class User {

    private String id;

    public String Username;

    public String Password;

    public int coins;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return coins == user.coins && Objects.equals(id, user.id) && Objects.equals(Username, user.Username) && Objects.equals(Password, user.Password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Username, Password, coins);
    }

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
