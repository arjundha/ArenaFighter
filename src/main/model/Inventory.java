package model;

import exceptions.InvalidEquipmentException;

import java.util.ArrayList;
import java.util.List;

/**
 * The Inventory class represents a collection of Equipment objects. An inventory can have as few as
 * 0 Equipment, to infinitely many. There are methods for getting an Equipment at a certain index, adding equipment,
 * removing equipment and getting the number of Equipment in an inventory. There are also methods to get the summation
 * of certain Equipment values of an inventory, like strength, endurance, dexterity or gold.
 *
 * @author Arjun
 */

// Represents all the equipment currently owned by a player (their inventory of equipment)
public class Inventory {
    private final List<Equipment> equipment;

    // EFFECTS: constructs an empty inventory
    public Inventory() {
        this.equipment = new ArrayList<>();
    }


    // EFFECTS: produce a specific equipment at a specified index, else throws InvalidEquipmentException if unable to
    public Equipment getEquipment(int index) throws InvalidEquipmentException {
        if (0 <= index && index < getInventorySize()) {
            return this.equipment.get(index);
        } else {
            throw new InvalidEquipmentException("That equipment is out of bounds.");
        }
    }

    // EFFECTS: produce the total strength modification value from elements in the Inventory
    public int getTotalStrength() {
        int strength = 0;
        for (Equipment equipment : this.equipment) {
            strength += equipment.getStrength();
        }
        return strength;
    }

    // EFFECTS: produce the total endurance modification value from elements in the Inventory
    public int getTotalEndurance() {
        int endurance = 0;
        for (Equipment equipment : this.equipment) {
            endurance += equipment.getEndurance();
        }
        return endurance;
    }

    // EFFECTS: produce the total dexterity modification value from elements in the Inventory
    public int getTotalDexterity() {
        int dexterity = 0;
        for (Equipment equipment : this.equipment) {
            dexterity += equipment.getDexterity();
        }
        return dexterity;
    }

    // EFFECTS: produce the total speed modification value from elements in the Inventory
    public int getTotalSpeed() {
        int speed = 0;
        for (Equipment equipment : this.equipment) {
            speed += equipment.getSpeed();
        }
        return speed;
    }

    // EFFECTS: produce the size of an inventory (number of items in it)
    public int getInventorySize() {
        return this.equipment.size();
    }

    // MODIFIES: this
    // EFFECTS: adds a new equipment to the inventory of equipment
    public void addEquipment(Equipment item) {
        this.equipment.add(item);
    }

    // MODIFIES: this
    // EFFECTS: remove an equipment from an Inventory if it is in the Inventory, else throw InvalidEquipmentException
    public void removeEquipment(int index) throws InvalidEquipmentException {
        if (0 <= index && index < getInventorySize()) {
            this.equipment.remove(index);
        } else {
            throw new InvalidEquipmentException("That equipment is out of bounds.");
        }
    }


}
