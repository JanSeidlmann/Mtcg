package at.technikum.apps.mtcg.entity;

import java.util.Arrays;
import java.util.List;

public class Card {

    private String id;
    private String name;
    private int damage;
    private boolean isSpell;

    public void setId(String id) { this.id = id; }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isSpell() {
        return isSpell;
    }

    public void setSpell(boolean spell) {
        isSpell = spell;
    }
}
