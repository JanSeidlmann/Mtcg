package at.technikum.apps.mtcg.entity;

import java.util.Arrays;
import java.util.List;

public class Card {

    private int package_id;
    private String card_id;
    private String name;
    private int damage;
    private String type;
    private boolean isSpell;

    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSpell() {
        return isSpell;
    }

    public void setSpell(boolean spell) {
        isSpell = spell;
    }
}
