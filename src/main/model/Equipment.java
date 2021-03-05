package model;

import org.json.JSONObject;
import persistence.Writable;

/**
 * The Equipment class is a representation of items that can bought and used by a
 * player to modify their stats. Each equipment has a name, and values for their strength,
 * endurance, dexterity, and speed modification values. These items also have a value
 * that represents how much they cost, this value being worth.
 *
 * The modifier values of these equipment items will be added to the stats of a Character object if they are in
 * their inventory.
 *
 * There are methods for getting each of the fields or values of an Equipment.
 *
 * @author Arjun
 */

// Represents equipment that can be bought and used by a player to modify their stats
public class Equipment implements Writable {
    private final String name;  // The name of a weapon
    private final int strength; // The damage modifier of a weapon
    private final int endurance;  // the endurance modifier of a weapon
    private final int dexterity;  // the dexterity modifier of a weapon
    private final int speed;  // the speed modifier of a weapon
    private final int worth;  // The amount of gold a weapon is worth

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
        return this.name;
    }

    // EFFECTS: returns the strength modifier of an equipment item
    public int getStrength() {
        return this.strength;
    }

    // EFFECTS: returns the endurance modifier of an equipment item
    public int getEndurance() {
        return this.endurance;
    }

    // EFFECTS: returns the dexterity modifier of an equipment item
    public int getDexterity() {
        return this.dexterity;
    }

    // EFFECTS: returns the speed modifier of an equipment item
    public int getSpeed() {
        return this.speed;
    }

    // EFFECTS: returns the worth in gold of an equipment item
    public int getWorth() {
        return this.worth;
    }

    // EFFECTS: produces an Equipment as a JSONObject
    // CITATION: The base code can be found at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //           This method is implemented using the CPSC 210 JsonSerializationDemo as it's base code.
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("name", name);
        json.put("strength", strength);
        json.put("endurance", endurance);
        json.put("dexterity", dexterity);
        json.put("speed", speed);
        json.put("worth", worth);

        return json;
    }
}
