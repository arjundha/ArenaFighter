package model;

// Represents equipment that can be bought and used by a player to modify their stats
public class Equipment {
    private String name;  // The name of a weapon
    private int strength; // The damage modifier of a weapon
    private int endurance;  // the endurance modifier of a weapon
    private int dexterity;  // the dexterity modifier of a weapon
    private int speed;  // the speed modifier of a weapon
    private int worth;  // The amount of gold a weapon is worth

    // EFFECTS: Constructs a weapon with a weapon name, the damage it does, and its worth in gold
    public Equipment(String name, int strength, int endurance, int dexterity, int speed, int worth) {
        this.name = name;
        this.strength = strength;
        this.endurance = endurance;
        this.dexterity = dexterity;
        this.speed = speed;
        this.worth = worth;
    }

    // EFFECTS: returns the name of an equipment item
    public String getName() {
        return name;
    }

    // EFFECTS: returns the strength modifier of an equipment item
    public int getStrength() {
        return strength;
    }

    // EFFECTS: returns the endurance modifier of an equipment item
    public int getEndurance() {
        return endurance;
    }

    // EFFECTS: returns the dexterity modifier of an equipment item
    public int getDexterity() {
        return dexterity;
    }

    // EFFECTS: returns the speed modifier of an equipment item
    public int getSpeed() {
        return speed;
    }

    // EFFECTS: returns the worth in gold of an equipment item
    public int getWorth() {
        return worth;
    }
}
