package at.technikum.apps.mtcg.entity;

public class Card {

    public String Id;
    public String Name;
    public int Damage;
    private String type;

    public String extractTypeFromName() {
        if (this.Name.contains("Water")) {
            return "Water";
        } else if (this.Name.contains("Fire")) {
            return "Fire";
        } else if (this.Name.contains("Regular")){
            return "Regular";
        } else {
            return "Unknown";
        }
    }

    @Override
    public String toString() {
        return "Card{" +
          "Id='" + Id + '\'' +
          ", Name='" + Name + '\'' +
          ", Damage=" + Damage +
          ", type='" + type + '\'' +
          '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public int getDamage() {
        return Damage;
    }

    public void setDamage(int damage) {
        this.Damage = damage;
    }
}
